package com.weixin.yixing.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.commons.utils.ResultContent;
import com.commons.utils.ResultPage;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sun.org.apache.regexp.internal.RE;
import com.weixin.yixing.constants.Constants;
import com.weixin.yixing.dao.*;
import com.weixin.yixing.entity.*;
import com.weixin.yixing.entity.vo.GetWidthResizedImageUrlRequest;
import com.weixin.yixing.entity.vo.UploadFileByStringBase64Request;
import com.weixin.yixing.utils.ImageVerifyUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.util.*;

@Service
public class WorksServiceImpl {
    private static Logger logger = LoggerFactory.getLogger(WorksServiceImpl.class);

    @Autowired
    private WorksInfoMapper worksInfoMapper;

    @Autowired
    private AuthorInfoMapper authorInfoMapper;

    @Autowired
    private FileServiceImpl fileServiceImpl;

    @Autowired
    private ActivityInfoMapper activityInfoMapper;


    /**
     * 查询PC端作品列表根据时间排序
     *
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @param token
     * @return
     */
    public ResultPage getWorksListByCreateTimeForPC(String activityId, String keyword, String pageNum, String pageSize, String token) {
        logger.info("开始查询PC端作品列表根据时间排序");
        Map<String, Object> map = new HashMap<>();
        map.put("activityId",activityId);
        map.put("keyword",keyword);
        Page<WorksInfo> page = PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize)).doSelectPage(() ->
                worksInfoMapper.selectByKeywordOrderByTimeForPC(map));
        List<WorksInfo> resultList = page.getResult();
        List<WorksList> list = new ArrayList<>();
        for (WorksInfo worksInfo : resultList) {
            WorksList worksList = new WorksList();
            worksList.setAuthorId(worksInfo.getAuthorId());
            AuthorInfo authorInfo = authorInfoMapper.selectAuthorInfoByAuthorId(worksInfo.getAuthorId());
            worksList.setAuthorName(authorInfo.getAuthorName());
            worksList.setNumOfVotes(worksInfo.getNumberOfVotes());
            worksList.setWorksId(worksInfo.getWorksUuid());
            worksList.setWorksName(worksInfo.getWorksName());
            worksList.setStatus(worksInfo.getStatus());
            worksList.setWorksNum(worksInfo.getWorksNum());
            worksList.setStatus(worksInfo.getStatus());

            String imageListStr = JSONArray.toJSONString(getImageUrlList(worksInfo.getImage()));
            worksList.setImage(imageListStr);
            list.add(worksList);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("worksList", list);

        return new ResultPage(Constants.REQUEST_SUCCESS, Constants.SUCCESS, jsonObject,
                page.getPageSize(), page.getPages(), page.getPageNum(), Integer.valueOf((int) page.getTotal()));
    }

    /**
     * 查询PC端作品列表根据人气排序
     *
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @param token
     * @return
     */
    public ResultPage getWorksListByVotesForPC(String activityId, String keyword, String pageNum, String pageSize, String token) {
        logger.info("开始查询PC端作品列表根据人气排序");
        Map<String, Object> map = new HashMap<>();
        map.put("activityId",activityId);
        map.put("keyword",keyword);
        Page<WorksInfo> page = PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize)).doSelectPage(() ->
                worksInfoMapper.selectByKeywordOrderByVotesForPC(map));
        List<WorksInfo> resultList = page.getResult();
        List<WorksList> list = new ArrayList<>();
        for (WorksInfo worksInfo : resultList) {
            WorksList worksList = new WorksList();
            worksList.setAuthorId(worksInfo.getAuthorId());
            AuthorInfo authorInfo = authorInfoMapper.selectAuthorInfoByAuthorId(worksInfo.getAuthorId());
            worksList.setAuthorName(authorInfo.getAuthorName());
            worksList.setNumOfVotes(worksInfo.getNumberOfVotes());
            worksList.setWorksId(worksInfo.getWorksUuid());
            worksList.setWorksName(worksInfo.getWorksName());
            worksList.setStatus(worksInfo.getStatus());
            worksList.setWorksNum(worksInfo.getWorksNum());
            worksList.setStatus(worksInfo.getStatus());

            String imageListStr = JSONArray.toJSONString(getImageUrlList(worksInfo.getImage()));
            worksList.setImage(imageListStr);
            list.add(worksList);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("worksList", list);

        return new ResultPage(Constants.REQUEST_SUCCESS, Constants.SUCCESS, jsonObject,
                page.getPageSize(), page.getPages(), page.getPageNum(), Integer.valueOf((int) page.getTotal()));
    }

    /**
     * 查询作品列表根据时间排序
     *
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @param token
     * @return
     */
    public ResultPage getWorksListByCreateTime(String activityId, String keyword, String pageNum, String pageSize, String token) {
        logger.info("开始查询作品列表根据时间排序");
        Map<String, Object> map = new HashMap<>();
        map.put("activityId",activityId);
        map.put("keyword",keyword);
        Page<WorksInfo> page = PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize)).doSelectPage(() ->
                worksInfoMapper.selectByKeywordOrderByTime(map));
        List<WorksInfo> resultList = page.getResult();
        List<WorksList> list = new ArrayList<>();
        for (WorksInfo worksInfo : resultList) {
            WorksList worksList = new WorksList();
            worksList.setAuthorId(worksInfo.getAuthorId());
            AuthorInfo authorInfo = authorInfoMapper.selectAuthorInfoByAuthorId(worksInfo.getAuthorId());
            worksList.setAuthorName(authorInfo.getAuthorName());
            worksList.setNumOfVotes(worksInfo.getNumberOfVotes());
            worksList.setWorksId(worksInfo.getWorksUuid());
            worksList.setWorksName(worksInfo.getWorksName());
            worksList.setWorksNum(worksInfo.getWorksNum());

            String imageListStr = JSONArray.toJSONString(getImageUrlList(worksInfo.getImage()));
            worksList.setImage(imageListStr);
            list.add(worksList);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("worksList", list);

        return new ResultPage(Constants.REQUEST_SUCCESS, Constants.SUCCESS, jsonObject,
                page.getPageSize(), page.getPages(), page.getPageNum(), Integer.valueOf((int) page.getTotal()));
    }

    /**
     * 查询作品列表根据票数排序
     *
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @param token
     * @return
     */
    public ResultPage getWorksListByVotes(String activityId, String keyword, String pageNum, String pageSize, String token) {
        logger.info("开始查询作品列表根据票数排序");
        Map<String, Object> map = new HashMap<>();
        map.put("activityId",activityId);
        map.put("keyword",keyword);
        Page<WorksInfo> page = PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize)).doSelectPage(() ->
                worksInfoMapper.selectByKeywordOrderByVotes(map));
        List<WorksInfo> resultList = page.getResult();
        List<WorksList> list = new ArrayList<>();
        for (WorksInfo worksInfo : resultList) {
            WorksList worksList = new WorksList();
            worksList.setAuthorId(worksInfo.getAuthorId());
            AuthorInfo authorInfo = authorInfoMapper.selectAuthorInfoByAuthorId(worksInfo.getAuthorId());
            worksList.setAuthorName(authorInfo.getAuthorName());
            worksList.setNumOfVotes(worksInfo.getNumberOfVotes());
            worksList.setWorksId(worksInfo.getWorksUuid());
            worksList.setWorksName(worksInfo.getWorksName());
            worksList.setWorksNum(worksInfo.getWorksNum());

            String imageListStr = JSONArray.toJSONString(getImageUrlList(worksInfo.getImage()));
            worksList.setImage(imageListStr);
            list.add(worksList);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("worksList", list);

        return new ResultPage(Constants.REQUEST_SUCCESS, Constants.SUCCESS, jsonObject,
                page.getPageSize(), page.getPages(), page.getPageNum(), Integer.valueOf((int) page.getTotal()));
    }

    /**
     * 查询未审核作品列表
     *
     * @param pageNum
     * @param pageSize
     * @param token
     * @return
     */
    public ResultPage getUnrReviewedWorksList(String pageNum, String pageSize, String token) {
        logger.info("开始查询未审核作品列表");

        Page<WorksInfo> page = PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize)).doSelectPage(() ->
                worksInfoMapper.selectByUnReviewedWorksForPC());
        List<WorksInfo> resultList = page.getResult();
        List<WorksList> list = new ArrayList<>();
        for (WorksInfo worksInfo : resultList) {
            WorksList worksList = new WorksList();
            worksList.setAuthorId(worksInfo.getAuthorId());
            AuthorInfo authorInfo = authorInfoMapper.selectAuthorInfoByAuthorId(worksInfo.getAuthorId());
            worksList.setAuthorName(authorInfo.getAuthorName());
            worksList.setNumOfVotes(worksInfo.getNumberOfVotes());
            worksList.setWorksId(worksInfo.getWorksUuid());
            worksList.setWorksName(worksInfo.getWorksName());
            worksList.setWorksNum(worksInfo.getWorksNum());
            worksList.setStatus(worksInfo.getStatus());

            String imageListStr = JSONArray.toJSONString(getImageUrlList(worksInfo.getImage()));
            worksList.setImage(imageListStr);
            list.add(worksList);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("worksList", list);

        return new ResultPage(Constants.REQUEST_SUCCESS, Constants.SUCCESS, jsonObject,
                page.getPageSize(), page.getPages(), page.getPageNum(), Integer.valueOf((int) page.getTotal()));
    }

    /**
     * 查询未审核作品数量
     *
     * @param token
     * @return
     */
    public ResultContent getNumOfUnReviewedWorks(String token) {
        logger.info("查询未审核作品数量");
        int result = worksInfoMapper.selectNumOfUnReviewedWorks();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("numOfUnReviewedWorks", result);
        return new ResultContent(Constants.REQUEST_SUCCESS, Constants.SUCCESS, jsonObject);
    }

    /**
     * 查询报名人数
     *
     * @param activityId
     * @param token
     * @return
     */
    public ResultContent getNumOfRegistration(String activityId, String token) {
        logger.info("查询报名人数");
        int result = authorInfoMapper.selectCountByActivityId(activityId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("numOfRegistration", result);
        return new ResultContent(Constants.REQUEST_SUCCESS, Constants.SUCCESS, jsonObject);
    }

    /**
     * 作品投票
     *
     * @param worksId
     * @param token
     * @return
     */
    public ResultContent addNumOfVotesOnce(String worksId, String token) {
        logger.info("开始投票");
        //查询作品得票数
        WorksInfo works = worksInfoMapper.selectWorksInfoByWorksId(worksId);

        WorksInfo worksInfo = new WorksInfo();
        worksInfo.setWorksUuid(worksId);
        worksInfo.setNumberOfVotes(works.getNumberOfVotes() + 1);
        worksInfo.setModifyTime(new Date());
        int result = worksInfoMapper.updateByPrimaryKeySelective(worksInfo);
        if (result > 0) {
            return new ResultContent(Constants.REQUEST_SUCCESS, Constants.SUCCESS, new JSONObject());
        } else {
            return new ResultContent(Constants.REQUEST_FAILED, Constants.FAILED, new JSONObject());
        }
    }

    /**
     * 查询作品详情
     *
     * @param worksId
     * @param token
     * @return
     */
    public ResultContent getWorksInfo(String worksId, String token) {
        logger.info("开始查询作品详情");
        WorksInfo worksInfo = worksInfoMapper.selectWorksInfoByWorksId(worksId);
        String imageInfo = worksInfo.getImage();
        //获取图片URL
        List<String> imageUrlList = new ArrayList<>();
        if (StringUtils.isNotEmpty(imageInfo)) {
            JSONArray jsonArray = JSON.parseArray(imageInfo);
            for (int i = 0; i < jsonArray.size(); i++) {
                String imageUrl = "";
                if (null != jsonArray.getJSONObject(i)) {
                    String fileId = jsonArray.getJSONObject(i).getString("id");
                    GetWidthResizedImageUrlRequest request = new GetWidthResizedImageUrlRequest();

                    request.setFileUuid(fileId);
                    request.setImageWidth(500);
                    ResultContent result = fileServiceImpl.getImageUrl(request);
                    if (result.getCode() == Constants.REQUEST_SUCCESS) {
                        Map<String, String> map = (Map<String, String>) result.getContent();
                        imageUrl = map.get("imageUrl");
                        imageUrlList.add(imageUrl);
                    } else {
                        logger.error("获取图片URL失败");
                    }
                }
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("worksNum", worksInfo.getWorksNum());
        jsonObject.put("worksUuid", worksInfo.getWorksUuid());
        jsonObject.put("worksName", worksInfo.getWorksName());
        jsonObject.put("authorId", worksInfo.getAuthorId());
        AuthorInfo authorInfo = authorInfoMapper.selectAuthorInfoByAuthorId(worksInfo.getAuthorId());
        jsonObject.put("phone", authorInfo.getPhone());
        jsonObject.put("authorName", authorInfo.getAuthorName());
        //查询作品得票数
//        GiftRecord giftRecord = giftRecordMapper.selectByWorksId(worksInfo.getWorksUuid());//根据作品ID查询礼物赠送情况
//        TypeOfGift typeOfGift = typeOfGiftMapper.selectByPrimaryKey(giftRecord.getGiftId());
//        Integer votesNum = worksInfo.getNumberOfVotes() + typeOfGift.ge;
        jsonObject.put("numberOfVotes", worksInfo.getNumberOfVotes());
        jsonObject.put("introductionOfWorks", worksInfo.getIntroductionOfWorks());
        jsonObject.put("imageUrl", imageUrlList);
        jsonObject.put("numOfClicks", worksInfo.getNumOfClicks());
        jsonObject.put("createTime", worksInfo.getCreateTime());


        //查询作品名次
        //TODO 待验证
        Map<String, String > map = new HashMap<>();
        map.put("WorksId", worksId);
        map.put("activityId", worksInfo.getActivityId());
//        int ranking = worksInfoMapper.selectRankingByWorksId(map);
//        jsonObject.put("ranking", ranking);

        return new ResultContent(Constants.REQUEST_SUCCESS, Constants.SUCCESS, jsonObject);
    }

    /**
     * 查询作品排行榜信息
     *
     * @param activityId
     * @param pageNum
     * @param pageSize
     * @param token
     * @return
     */
    public ResultPage getWorksLeaderBoard(String activityId, String pageNum, String pageSize, String token) {
        logger.info("开始查询作品排行榜信息");

        Page<WorksInfo> page = PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize)).doSelectPage(() ->
                worksInfoMapper.selectWorksLeaderBoardByActivityId(activityId));
        List<WorksInfo> worksList = page.getResult();
        List<Map<String, Object>> worksInfoList = new ArrayList<>();
        for (WorksInfo worksInfo : worksList) {
            Map<String, Object> map = new HashMap<>();
            map.put("worksNum", worksInfo.getWorksNum());
            map.put("worksId", worksInfo.getWorksUuid());
            map.put("votes", worksInfo.getNumberOfVotes());
            AuthorInfo authorInfo = authorInfoMapper.selectAuthorInfoByAuthorId(worksInfo.getAuthorId());
            map.put("authorName", authorInfo.getAuthorName());
            worksInfoList.add(map);
        }
        return new ResultPage(Constants.REQUEST_SUCCESS, Constants.SUCCESS, worksInfoList,
                page.getPageSize(), page.getPages(), page.getPageNum(), Integer.valueOf((int) page.getTotal()));
    }

    /**
     * 审核作品
     *
     * @param worksId
     * @param token
     * @return
     */
    public ResultContent updateUnReviewedWorksInfo(String worksId, String token) {
        logger.info("开始审核作品");
        WorksInfo worksInfo = new WorksInfo();
        worksInfo.setWorksUuid(worksId);
        worksInfo.setStatus("1");
        int result = worksInfoMapper.updateByPrimaryKeySelective(worksInfo);

        if (result > 0) {
            return new ResultContent(Constants.REQUEST_SUCCESS, Constants.SUCCESS, new JSONObject());
        } else {
            return new ResultContent(Constants.REQUEST_FAILED, Constants.FAILED, new JSONObject());
        }
    }

    /**
     * 修改作品得票数以及点击量
     *
     * @param worksId
     * @param numOfVotes
     * @param token
     * @return
     */
    public ResultContent updateNumOfVotes(String worksId, String numOfVotes, String numOfClicks, String token) {
        logger.info("开始修改作品得票数");
        WorksInfo worksInfo = new WorksInfo();
        worksInfo.setWorksUuid(worksId);
        worksInfo.setNumberOfVotes(Integer.valueOf(numOfVotes));
        worksInfo.setNumOfClicks(Integer.valueOf(numOfClicks));
        int result = worksInfoMapper.updateByPrimaryKeySelective(worksInfo);
        if (result > 0) {
            return new ResultContent(Constants.REQUEST_SUCCESS, Constants.SUCCESS, new JSONObject());
        } else {
            return new ResultContent(Constants.REQUEST_FAILED, Constants.FAILED, new JSONObject());
        }
    }

    /**
     * 新增作品点击量
     *
     * @param worksId
     * @param token
     * @return
     */
    public ResultContent addClicksOfWorksOnce(String worksId, String token) {

        //查询作品得票数
        WorksInfo works = worksInfoMapper.selectWorksInfoByWorksId(worksId);

        WorksInfo worksInfo = new WorksInfo();
        worksInfo.setWorksUuid(worksId);
        worksInfo.setNumOfClicks(works.getNumOfClicks() + 1);
        int result = worksInfoMapper.updateByPrimaryKeySelective(worksInfo);
        if (result > 0) {
            return new ResultContent(Constants.REQUEST_SUCCESS, Constants.SUCCESS, new JSONObject());
        } else {
            return new ResultContent(Constants.REQUEST_FAILED, Constants.FAILED, new JSONObject());
        }
    }

    /**
     * 查询所有参赛作品
     *
     * @param token
     * @return
     */
    public ResultPage getAllActivityList(String pageNum, String pageSize, String token) {
        logger.info("开始查询所有参赛作品");

        List<ActivityInfo> activityList = activityInfoMapper.selectAllActivityInfo();
        List<Map<String, Object>> mapList = new ArrayList<>();

        Integer pages = 0;
        Integer pageTotal = 0;
        for (ActivityInfo activityInfo : activityList) {
            Page<WorksInfo> page = PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize)).doSelectPage(() ->
                    worksInfoMapper.selectWorksLeaderBoardByActivityId(activityInfo.getActivityId()));
            Map<String, Object> map = new HashMap();
            List<WorksInfo> resultList = page.getResult();
            List<WorksList> list = new ArrayList<>();
            for (WorksInfo worksInfo : resultList) {
                WorksList worksList = new WorksList();
                worksList.setAuthorId(worksInfo.getAuthorId());
                AuthorInfo authorInfo = authorInfoMapper.selectAuthorInfoByAuthorId(worksInfo.getAuthorId());
                worksList.setAuthorName(authorInfo.getAuthorName());
                worksList.setNumOfVotes(worksInfo.getNumberOfVotes());
                worksList.setWorksId(worksInfo.getWorksUuid());
                worksList.setWorksName(worksInfo.getWorksName());
                worksList.setWorksNum(worksInfo.getWorksNum());
                worksList.setStatus(worksInfo.getStatus());

                String imageListStr = JSONArray.toJSONString(getImageUrlList(worksInfo.getImage()));
                worksList.setImage(imageListStr);
                list.add(worksList);
            }
            map.put("activityName", activityInfo.getActivityName());
            map.put("worksList", list);
            pages = page.getPages();
            pageTotal = (int) page.getTotal();
            mapList.add(map);
        }

        return new ResultPage(Constants.REQUEST_SUCCESS, Constants.SUCCESS, mapList,
                Integer.valueOf(pageSize), pages, Integer.valueOf(pageNum), pageTotal);
    }

    private List getImageUrlList(String imageInfo) {
        List<String> imageUrlList = new ArrayList<>();
        if (StringUtils.isNotEmpty(imageInfo)) {
            JSONArray jsonArray = JSON.parseArray(imageInfo);
            for (int i = 0; i < jsonArray.size(); i++) {
                String imageUrl = "";
                if (null != jsonArray.getJSONObject(i)) {
                    String fileId = jsonArray.getJSONObject(i).getString("id");
                    GetWidthResizedImageUrlRequest request = new GetWidthResizedImageUrlRequest();

                    request.setFileUuid(fileId);
                    request.setImageWidth(500);
                    ResultContent result = fileServiceImpl.getImageUrl(request);
                    if (result.getCode() == Constants.REQUEST_SUCCESS) {
                        Map<String, String> map = (Map<String, String>) result.getContent();
                        imageUrl = map.get("imageUrl");
                        imageUrlList.add(imageUrl);
                    } else {
                        logger.error("获取图片URL失败");
                    }
                }
            }
        }
        return imageUrlList;
    }

    public ResultContent uploadFile(MultipartFile file) {
        logger.info("开始执行上传文件服务");

        if (null == file) {
            return new ResultContent(Constants.REQUEST_FAILED, "文件不能为空", "");
        }

        if (file.getSize() > 5 * 1048576) {
            return new ResultContent(Constants.REQUEST_FAILED, "上传文件大小不能超过5M", "");
        }
        String fileName = file.getOriginalFilename();

        if (!ImageVerifyUtils.verifyImageName(fileName)) {
            return new ResultContent(Constants.REQUEST_FAILED, "上传文件非法，请上传正确图片", "");
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

        ResultContent result = fileServiceImpl.uploadFileByBase64String(request);

        if (result.getCode() == Constants.REQUEST_SUCCESS) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("fileUuid", result.getContent());
            return new ResultContent(Constants.REQUEST_SUCCESS, Constants.SUCCESS, jsonObject);
        } else {
            return new ResultContent(Constants.REQUEST_FAILED, Constants.FAILED, "{}");
        }
    }
}
