package com.weixin.yixing.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.commons.utils.ResultContent;
import com.commons.utils.ResultPage;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.weixin.yixing.constants.Constants;
import com.weixin.yixing.dao.AuthorInfoMapper;
import com.weixin.yixing.dao.WorksInfoMapper;
import com.weixin.yixing.entity.WorksInfo;
import com.weixin.yixing.entity.WorksList;
import com.weixin.yixing.entity.vo.UploadFileByStringBase64Request;
import com.weixin.yixing.utils.ImageVerifyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.util.List;

@Service
public class WorksServiceImpl {
    private static Logger logger= LoggerFactory.getLogger(WorksServiceImpl.class);

    @Autowired
    private WorksInfoMapper worksInfoMapper;

    @Autowired
    private AuthorInfoMapper authorInfoMapper;

    @Autowired
    private FileServiceImpl fileServiceImpl;

    /**
     * 查询作品列表根据时间排序
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @param token
     * @return
     */
    public ResultPage getWorksListByCreateTime(String keyword, String pageNum, String pageSize, String  token){
        logger.info("开始查询作品列表根据时间排序");

        Page<WorksList> page= PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize)).doSelectPage(()->
                        worksInfoMapper.selectByKeywordOrderByTime(keyword));
        List<WorksList> list = page.getResult();
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("worksList", list);

        return new ResultPage(Constants.REQUEST_SUCCESS, Constants.SUCCESS,list,
                page.getPageSize(), page.getPages(), page.getPageNum(), Integer.valueOf((int) page.getTotal()));
    }

    /**
     *查询作品列表根据票数排序
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @param token
     * @return
     */
    public ResultPage getWorksListByVotes(String keyword, String pageNum, String pageSize, String  token){
        logger.info("开始查询作品列表根据票数排序");

        Page<WorksList> page= PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize)).doSelectPage(()->
                worksInfoMapper.selectByKeywordOrderByVotes(keyword));
        List<WorksList> list = page.getResult();
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("worksList", list);

        return new ResultPage(Constants.REQUEST_SUCCESS, Constants.SUCCESS,list,
                page.getPageSize(), page.getPages(), page.getPageNum(), Integer.valueOf((int) page.getTotal()));
    }

    /**
     * 查询报名人数
     * @param activityId
     * @param token
     * @return
     */
    public ResultContent getNumOfRegistration(String activityId, String token){
        logger.info("查询报名人数");
        int result = authorInfoMapper.selectCountByActivityId(activityId);
        return new ResultContent(Constants.REQUEST_SUCCESS, Constants.SUCCESS, result);
    }

    /**
     * 作品投票
     * @param worksId
     * @param token
     * @return
     */
    public ResultContent addNumOfVotesOnce(String worksId, String token){
        return null;
    }

    /**
     *查询作品详情
     * @param worksId
     * @param token
     * @return
     */
    public ResultContent getWorksInfo(String worksId, String token){
        logger.info("开始查询作品详情");
        WorksInfo worksInfo = worksInfoMapper.selectWorksInfoByWorksId(worksId);

        return new ResultContent(Constants.REQUEST_SUCCESS, Constants.SUCCESS, worksInfo);
    }

    /**
     *新增作品点击量
     * @param worksId
     * @param token
     * @return
     */
    public ResultContent addClicksOfWorksOnce(String worksId, String token){

        return null;
    }

    public ResultContent uploadFile(MultipartFile file){
        logger.info("开始执行上传文件服务");

        if(null==file){
            return new ResultContent(Constants.REQUEST_FAILED,"文件不能为空","");
        }

        if(file.getSize()>5*1048576){
            return new ResultContent(Constants.REQUEST_FAILED,"上传文件大小不能超过5M","");
        }
        String fileName = file.getOriginalFilename();

        if(!ImageVerifyUtils.verifyImageName(fileName)){
            return new ResultContent(Constants.REQUEST_FAILED,"上传文件非法，请上传正确图片","");
        }
        UploadFileByStringBase64Request request = new UploadFileByStringBase64Request();
        request.setOriginalFileName(fileName);
        byte[] data = new byte[0];
        try {
            data = file.getBytes();
        } catch (IOException e) {
            logger.info("上传文件异常");
            e.printStackTrace();
        }
        request.setFileContent(new BASE64Encoder().encode(data));

        ResultContent result=fileServiceImpl.uploadFileByBase64String(request);

        if(result.getContent() == Constants.REQUEST_SUCCESS){
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("fileUuid",result.getContent());
            return new ResultContent(Constants.REQUEST_SUCCESS,Constants.SUCCESS,jsonObject);
        }else {
            return new ResultContent(Constants.REQUEST_FAILED,Constants.FAILED,"{}");
        }
    }
}
