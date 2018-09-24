package com.weixin.yixing.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.commons.utils.ResultContent;
import com.commons.utils.StringUtils;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import com.weixin.yixing.constants.Constants;
import com.weixin.yixing.dao.FileMapper;
import com.weixin.yixing.entity.File;
import com.weixin.yixing.entity.vo.GetWidthResizedImageUrlRequest;
import com.weixin.yixing.entity.vo.UploadFileByStringBase64Request;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import java.io.ByteArrayInputStream;;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class FileServiceImpl {

    @Value("${cos.accesskey}")
    private String accesskey;

    @Value("${cos.secretKey}")
    private String secretKey;

    @Value("${cos.bucket.name}")
    private String bucketName;

    @Value("${cos.bucket.region}")
    private String region;

    @Autowired
    private FileMapper fileMapper;

    public String uploadFile(String key, String originalFileName, InputStream in)throws IOException {
        // 1 初始化用户身份信息(secretId, secretKey)
        COSCredentials cred = new BasicCOSCredentials(accesskey, secretKey);
        // 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        ClientConfig clientConfig = new ClientConfig(new Region(region));
        // 3 生成cos客户端
        COSClient cosClient = new COSClient(cred, clientConfig);

        //创建上传Object的Metadata
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(in.available());
        objectMetadata.setCacheControl("no-cache");
        objectMetadata.setHeader("Pragma", "no-cache");
        if (originalFileName.contains(".")) {
            String fileType = originalFileName.substring(originalFileName.lastIndexOf(".")).replace(".", "");
            objectMetadata.setContentType(getContentType(fileType));
        }
        objectMetadata.setContentDisposition("inline;filename=" + originalFileName);

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, in, objectMetadata);
        try{
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            String etag = putObjectResult.getETag();
            return etag;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }finally {
            cosClient.shutdown();
        }

    }

    public ResultContent uploadFileByBase64String(UploadFileByStringBase64Request request) {
        byte[] buffer = null;
        try {
            buffer = new BASE64Decoder().decodeBuffer(request.getFileContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(buffer.length==0){
            return new ResultContent(Constants.REQUEST_FAILED, Constants.FAILED, "");
        }
        com.weixin.yixing.entity.File record = new com.weixin.yixing.entity.File();
        String fileUuid = UUID.randomUUID().toString().replace("-","");

        record.setFileUuid(fileUuid);
        //分组不形成目录，便于分组修改
        record.setGroupId(request.getGroupId());
        String fileOssKey = "";
        if(request.getOriginalFileName().contains(".")){
            fileOssKey = fileUuid + request.getOriginalFileName().substring(request.getOriginalFileName().lastIndexOf("."));
        } else {
            fileOssKey = fileUuid;
        }
        fileOssKey = DateFormatUtils.format(new Date(),"yyyy-MM-dd").replace("-","/") +"/"+fileOssKey;
        record.setFileOriginalName(request.getOriginalFileName());
        record.setFileOssKey(fileOssKey);
        record.setCreationTime(new Date());
        record.setModifyTime(new Date());

        record.setFileOssBucket(bucketName);
        int res = fileMapper.insertSelective(record);
        if(res>0){
            try {
                uploadFile(fileOssKey, request.getOriginalFileName(), new ByteArrayInputStream(buffer));
            } catch (IOException e) {
                // TODO: 2017/5/22 异常事务处理
                e.printStackTrace();
            }
            return new ResultContent(Constants.REQUEST_SUCCESS, Constants.SUCCESS, fileUuid);
        } else {
            return new ResultContent(Constants.REQUEST_FAILED, Constants.FAILED, "");
        }
    }

    /**
     * Description: 判断OSS服务文件上传时文件的contentType
     *
     * @param FilenameExtension 文件后缀
     * @return String
     */
    public String getContentType(String FilenameExtension) {
        if (FilenameExtension.equalsIgnoreCase("bmp")) {
            return "image/bmp";
        }
        if (FilenameExtension.equalsIgnoreCase("gif")) {
            return "image/gif";
        }
        if (FilenameExtension.equalsIgnoreCase("jpeg") ||
                FilenameExtension.equalsIgnoreCase("jpg") ||
                FilenameExtension.equalsIgnoreCase("png")) {
            return "image/jpeg";
        }
        if (FilenameExtension.equalsIgnoreCase("html")) {
            return "text/html";
        }
        if (FilenameExtension.equalsIgnoreCase("txt")) {
//            return "text/plain";
            //为了兼容前端a标签下载
            return "application/msword";
        }
        if (FilenameExtension.equalsIgnoreCase("vsd")) {
            return "application/vnd.visio";
        }
        if (FilenameExtension.equalsIgnoreCase("pptx") ||
                FilenameExtension.equalsIgnoreCase("ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (FilenameExtension.equalsIgnoreCase("docx") ||
                FilenameExtension.equalsIgnoreCase("doc") ||
                FilenameExtension.equalsIgnoreCase("xls") ||
                FilenameExtension.equalsIgnoreCase("xlsx")) {
            return "application/msword";
        }
        if (FilenameExtension.equalsIgnoreCase("xml")) {
            return "text/xml";
        }
        return "";
    }

    public ResultContent getImageUrl(GetWidthResizedImageUrlRequest request){
        File record = new File();
        record.setFileUuid(request.getFileUuid());

        List<File> res = fileMapper.selectByFileSelective(record);
        if(null!=res && res.size()>0) {
//            String style = "";
//            if (null != request.getImageWidth()&&request.getImageWidth()>0){
//                style = "image/resize,w_"+request.getImageWidth();
//            }
            String url = downloadResizedImage(res.get(0).getFileOssKey(), res.get(0).getFileOssBucket());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("imageUrl", url);
            jsonObject.put("fileUuid", request.getFileUuid());

            return new ResultContent(Constants.REQUEST_SUCCESS, Constants.SUCCESS, jsonObject);
        } else {
            return new ResultContent(Constants.REQUEST_FAILED, Constants.FAILED, "{}");
        }
    }

    /**
     * 获取图片下载地址
     *
     * @param key
     * @return
     */
    public String downloadResizedImage(String key, String bucketName) {
        COSCredentials cred = new BasicCOSCredentials(accesskey, secretKey);
        ClientConfig clientConfig = new ClientConfig(new Region(region));
        COSClient cosClient = new COSClient(cred, clientConfig);

        try {
                // 设置URL过期时间为1小时
                Date expiration = new Date(new Date().getTime() + 3600 * 1000);
                GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucketName, key, HttpMethodName.GET);
                req.setExpiration(expiration);

                return cosClient.generatePresignedUrl(req).toString();
        } catch (Exception ex) {
                throw ex;
        } finally {
            cosClient.shutdown();
        }

    }

    /**
     * 获得url链接
     *
     * @param key
     * @return
     */
    public String getUrl(String key, String bucketName) {
        COSCredentials cred = new BasicCOSCredentials(accesskey, secretKey);
        // 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        ClientConfig clientConfig = new ClientConfig(new Region(region));
        // 3 生成cos客户端
        COSClient cosClient = new COSClient(cred, clientConfig);
        // 设置URL过期时间为2年  3600l* 1000*24*365*2
        Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 2 );
        // 生成URL
        URL url = cosClient.generatePresignedUrl(bucketName, key, expiration);
        if (url != null) {
            return url.toString();
        }
        return null;
    }
}
