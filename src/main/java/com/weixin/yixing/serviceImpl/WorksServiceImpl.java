package com.weixin.yixing.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.commons.utils.ResultContent;
import com.commons.utils.ResultPage;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sun.org.apache.regexp.internal.RE;
import com.weixin.yixing.config.RedisUtil;
import com.weixin.yixing.constants.Constants;
import com.weixin.yixing.dao.*;
import com.weixin.yixing.entity.*;
import com.weixin.yixing.entity.vo.GetWidthResizedImageUrlRequest;
import com.weixin.yixing.entity.vo.UploadFileByStringBase64Request;
import com.weixin.yixing.exception.CoreException;
import com.weixin.yixing.utils.HttpClientUtil;
import com.weixin.yixing.utils.ImageVerifyUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.text.SimpleDateFormat;
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

    @Autowired
    private WeChatUserMapper weChatUserMapper;

    @Autowired
    private UserVotesRecordMapper userVotesRecordMapper;

    @Autowired
    private AuthorWorksMapper authorWorksMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Value("${appid}")
    private String appid;

    @Value("${secret}")
    private String secret;

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
        if (null == page){
            return new ResultPage(Constants.REQUEST_SUCCESS, "暂时没有参赛作品", "{}",1,1,1,1);
        }
        List<WorksInfo> resultList = page.getResult();
        List<WorksList> list = new ArrayList<>();
        for (WorksInfo worksInfo : resultList) {
            WorksList worksList = new WorksList();
            worksList.setAuthorId(worksInfo.getAuthorId());
            AuthorInfo authorInfo = authorInfoMapper.selectAuthorInfoByAuthorId(worksInfo.getAuthorId());
            if (null != authorInfo){
                worksList.setAuthorName(authorInfo.getAuthorName());
            }else{
                worksList.setAuthorName("");
            }
            worksList.setNumOfVotes(worksInfo.getNumberOfVotes());
            worksList.setNumOfClicks(worksInfo.getNumOfClicks());
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
        if (null == page){
            return new ResultPage(Constants.REQUEST_SUCCESS, "暂时没有参赛作品", "{}",1,1,1,1);
        }
        List<WorksInfo> resultList = page.getResult();
        List<WorksList> list = new ArrayList<>();
        for (WorksInfo worksInfo : resultList) {
            WorksList worksList = new WorksList();
            worksList.setAuthorId(worksInfo.getAuthorId());
            AuthorInfo authorInfo = authorInfoMapper.selectAuthorInfoByAuthorId(worksInfo.getAuthorId());
            if (null != authorInfo){
                worksList.setAuthorName(authorInfo.getAuthorName());
            }else{
                worksList.setAuthorName("");
            }
            worksList.setNumOfVotes(worksInfo.getNumberOfVotes());
            worksList.setWorksId(worksInfo.getWorksUuid());
            worksList.setNumOfClicks(worksInfo.getNumOfClicks());
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
        if (null == page){
            return new ResultPage(Constants.REQUEST_SUCCESS, "暂时没有参赛作品", "{}",1,1,1,1);
        }
        List<WorksInfo> resultList = page.getResult();
        List<WorksList> list = new ArrayList<>();
        for (WorksInfo worksInfo : resultList) {
            WorksList worksList = new WorksList();
            worksList.setAuthorId(worksInfo.getAuthorId());
            AuthorInfo authorInfo = authorInfoMapper.selectAuthorInfoByAuthorId(worksInfo.getAuthorId());
            if (null != authorInfo){
                worksList.setAuthorName(authorInfo.getAuthorName());
            }else{
                worksList.setAuthorName("");
            }
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
        if (null == page){
            return new ResultPage(Constants.REQUEST_SUCCESS, "暂时没有参赛作品", "{}",1,1,1,1);
        }
        List<WorksInfo> resultList = page.getResult();
        List<WorksList> list = new ArrayList<>();
        for (WorksInfo worksInfo : resultList) {
            WorksList worksList = new WorksList();
            worksList.setAuthorId(worksInfo.getAuthorId());
            AuthorInfo authorInfo = authorInfoMapper.selectAuthorInfoByAuthorId(worksInfo.getAuthorId());
            if (null != authorInfo){
                worksList.setAuthorName(authorInfo.getAuthorName());
            }else{
                worksList.setAuthorName("");
            }
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
        if (null == page){
            return new ResultPage(Constants.REQUEST_SUCCESS, "作品全部审核完成", "{}",1,1,1,1);
        }
        List<WorksInfo> resultList = page.getResult();
        List<WorksList> list = new ArrayList<>();
        for (WorksInfo worksInfo : resultList) {
            WorksList worksList = new WorksList();
            worksList.setAuthorId(worksInfo.getAuthorId());
            AuthorInfo authorInfo = authorInfoMapper.selectAuthorInfoByAuthorId(worksInfo.getAuthorId());
            if (null != authorInfo){
                worksList.setAuthorName(authorInfo.getAuthorName());
            }else{
                worksList.setAuthorName("");
            }
            worksList.setNumOfVotes(worksInfo.getNumberOfVotes());
            worksList.setNumOfClicks(worksInfo.getNumOfClicks());
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
        List<AuthorInfo> authorInfoList = authorInfoMapper.selectByActivityId(activityId);
        List<Map<String, Object>> avatarUrlList = new ArrayList<>();
        for(AuthorInfo authorInfo: authorInfoList){
            Map<String, Object>map = new HashMap<>();
            WeChatUser weChatUser = weChatUserMapper.findByOpenid(authorInfo.getWechatOpenId());
            map.put("avatarUrl", weChatUser.getAvatarurl());
            map.put("nickName", weChatUser.getNickName());
            avatarUrlList.add(map);
        }
        jsonObject.put("avatarUrlList", avatarUrlList);
        return new ResultContent(Constants.REQUEST_SUCCESS, Constants.SUCCESS, jsonObject);
    }

    /**
     * 作品投票
     *
     * @param worksId
     * @param token
     * @return
     */
    public ResultContent addNumOfVotesOnce(String openId,String worksId, String token) {
        logger.info("开始投票, " +"openid:" + openId + ", worksId:" + worksId);

        //查询是否超过投票限制
        WeChatUser weChatUser = weChatUserMapper.findByOpenid(openId);
        int limit = weChatUser.getVoteNum();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(new Date());
        Map<String, Object>map = new HashMap<>();
        map.put("openId",openId);
        map.put("dateStr", dateStr);
        List<UserVotesRecord>list = userVotesRecordMapper.selectByDate(map);
        int sumOfVotes = 0;
        for(UserVotesRecord userVotesRecord:list){
            sumOfVotes = sumOfVotes + userVotesRecord.getVotes();
        }
        if (sumOfVotes >= limit){
            return new ResultContent(Constants.REQUEST_FAILED, "已达到每日投票上限，转发给朋友帮忙投票或送礼物获得票数", "{}");
        } else {
            UserVotesRecord userVotesRecord = new UserVotesRecord();
            userVotesRecord.setWechatOpenid(openId);
            userVotesRecord.setWorksUuid(worksId);
            userVotesRecord.setVotingDate(dateStr);
            userVotesRecord.setVotes(1);
            userVotesRecord.setCreateTime(new Date());
            userVotesRecord.setModifyTime(new Date());
            userVotesRecordMapper.insert(userVotesRecord);
        }

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
        if (null == worksInfo){
            return new ResultContent(Constants.REQUEST_FAILED, "该作品不存在", "{}");
        }
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
        if (null != authorInfo){
            jsonObject.put("phone", authorInfo.getPhone());
            jsonObject.put("authorName", authorInfo.getAuthorName());
            String openId = authorInfo.getWechatOpenId();
            WeChatUser weChatUser = weChatUserMapper.findByOpenid(openId);
            if(null != weChatUser){
                jsonObject.put("avatarUrl",weChatUser.getAvatarurl());
            }else {
                jsonObject.put("avatarUrl","");
            }
        }else{
            jsonObject.put("phone", "");
            jsonObject.put("authorName", "");
            jsonObject.put("avatarUrl","");
        }

        jsonObject.put("numberOfVotes", worksInfo.getNumberOfVotes());
        jsonObject.put("introductionOfWorks", worksInfo.getIntroductionOfWorks());
        jsonObject.put("imageUrl", imageUrlList);
        jsonObject.put("numOfClicks", worksInfo.getNumOfClicks());
        jsonObject.put("createTime", worksInfo.getCreateTime());
        jsonObject.put("status", worksInfo.getStatus());

        //查询作品名次
        Map<String, String > map = new HashMap<>();
        map.put("WorksId", worksId);
        map.put("activityId", worksInfo.getActivityId());
        List<WorksInfo> list = worksInfoMapper.selectWorksLeaderBoardByActivityId(worksInfo.getActivityId());
        int ranking= 0;
        for (int i = 0; i< list.size(); i++){
            if (worksId.equals(list.get(i).getWorksUuid())){
                ranking = i+1;
            }
        }
        jsonObject.put("ranking", ranking);

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
        if (null == page){
            return new ResultPage(Constants.REQUEST_SUCCESS, "暂时没有参赛作品", "{}",1,1,1,1);
        }
        List<WorksInfo> worksList = page.getResult();
        List<Map<String, Object>> worksInfoList = new ArrayList<>();
        for (WorksInfo worksInfo : worksList) {
            Map<String, Object> map = new HashMap<>();
            map.put("worksNum", worksInfo.getWorksNum());
            map.put("worksId", worksInfo.getWorksUuid());
            map.put("votes", worksInfo.getNumberOfVotes());
            AuthorInfo authorInfo = authorInfoMapper.selectAuthorInfoByAuthorId(worksInfo.getAuthorId());
            if (null != authorInfo){
                map.put("authorName", authorInfo.getAuthorName());
            }else{
                map.put("authorName", "");
            }
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
    public ResultContent updateUnReviewedWorksInfo(String worksId, String status, String token) {
        logger.info("开始审核作品");
        WorksInfo worksInfo = new WorksInfo();
        worksInfo.setWorksUuid(worksId);
        worksInfo.setStatus(status);


        //开始发送消息
        AuthorWorks authorWorks = authorWorksMapper.selectByWorksId(worksId);
        String authorId = authorWorks.getAuthorId();
        AuthorInfo authorInfo = authorInfoMapper.selectAuthorInfoByAuthorId(authorId);
        String formId = authorInfo.getFormId();
//        String formId = "d5ebc65a65cb9e6a6eeab0d276099ee9";
        String openId = authorInfo.getWechatOpenId();
        String authorName = authorInfo.getAuthorName();
        String msgResult = "";
        if("1".equals(status)){
            msgResult = "恭喜您作品已审核通过，敬请关注作品动态和投票结果";
        }else{
            msgResult = "作品未审核通过,如有疑问请联系:18676833933.";
        }
        ActivityInfo activityInfo = activityInfoMapper.selectActivityInfoByActivityId(authorInfo.getActivityId());
        if(null == activityInfo){
            throw  CoreException.of(CoreException.ACTIVITY_NOT_EXIST);
        }
        String activityName = activityInfo.getActivityName();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        JSONObject jsonObject = getJsonDate(authorName, sdf.format(new Date()), msgResult, activityName);
        System.out.println(jsonObject.toJSONString());
        JSONObject resultObj = sendTemplateMessage(openId, "pages/home/main", "", formId, jsonObject);
        //更新审核状态
        int result = worksInfoMapper.updateByPrimaryKeySelective(worksInfo);
        if (result > 0 && resultObj.getInteger("errcode") == 0) {
            return new ResultContent(Constants.REQUEST_SUCCESS, Constants.SUCCESS, new JSONObject());
        } else {
            return new ResultContent(Constants.REQUEST_FAILED, resultObj.getString("errmsg"), new JSONObject());
        }
    }

    /**
     * 发送模板消息
     * @param openId
     * @param page
     * @param emphasisKeyword
     * @param formId
     * @param data
     * @return
     */
    public JSONObject sendTemplateMessage(String openId, String page, String emphasisKeyword, String formId, JSONObject data){

        String url = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=";
        StringBuilder sb = new StringBuilder();
        String weChatToken = "";
        if (redisUtil.exists("weChatToken")){
            weChatToken = (String) redisUtil.get("weChatToken");
        }else{
            JSONObject tokenObject = getWeChatToken();
            weChatToken = tokenObject.getString("access_token");
        }
        sb.append(url).append(weChatToken);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("touser", openId);
        jsonObject.put("template_id", "wTWqmeNUHhqS1-o-pGY0gN3eHBF-RzNjKwFqD6-ICmE");
        jsonObject.put("page", page);
        jsonObject.put("form_id", formId);
        jsonObject.put("data", data);
        jsonObject.put("emphasis_keyword", emphasisKeyword);
        JSONObject object = new JSONObject();
        try{
            object = JSON.parseObject(HttpClientUtil.httpsRequest(sb.toString(), "POST",jsonObject.toJSONString()));
        }catch (Exception e){
            e.printStackTrace();
        }
        return object;
    }

    private JSONObject getWeChatToken(){
        String url = "https://api.weixin.qq.com/cgi-bin/token";
        Map<String, String> params = new HashMap<>();
        params.put("grant_type", "client_credential");
        params.put("appid", appid);
        params.put("secret", secret);
        JSONObject object = new JSONObject();
        try{
            object = JSON.parseObject(HttpClientUtil.doGet(url, params));
        }catch (Exception e){
            e.printStackTrace();
        }
        redisUtil.set("weChatToken", object.getString("access_token"), object.getLong("expires_in"));
        return object;
    }

    private  JSONObject getJsonDate(String name, String date, String result, String activityName){
        JSONObject json = new JSONObject();
        JSONObject value1 = new JSONObject();
        value1.put("value", name);
        json.put("keyword1",value1);
        JSONObject value2 = new JSONObject();
        value2.put("value", date);
        json.put("keyword2",value2);
        JSONObject value3 = new JSONObject();
        value3.put("value", result);
        json.put("keyword3",value3);
        JSONObject value4 = new JSONObject();
        value4.put("value", activityName);
        json.put("keyword4",value4);
        return json;
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
            if (null == page){
                return new ResultPage(Constants.REQUEST_SUCCESS, "暂时没有参赛作品", "{}",1,1,1,1);
            }
            Map<String, Object> map = new HashMap();
            List<WorksInfo> resultList = page.getResult();
            List<WorksList> list = new ArrayList<>();
            for (WorksInfo worksInfo : resultList) {
                WorksList worksList = new WorksList();
                worksList.setAuthorId(worksInfo.getAuthorId());
                AuthorInfo authorInfo = authorInfoMapper.selectAuthorInfoByAuthorId(worksInfo.getAuthorId());
                if (null != authorInfo){
                    worksList.setAuthorName(authorInfo.getAuthorName());
                }else{
                    worksList.setAuthorName("");
                }
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

    public ResultContent uploadFile(@RequestParam(value = "file", required = false)MultipartFile file) {
        logger.info("开始执行上传文件服务");

        if (null == file) {
            return new ResultContent(Constants.REQUEST_FAILED, "文件不能为空", "");
        }

        if (file.getSize() > 10 * 1048576) {
            return new ResultContent(Constants.REQUEST_FAILED, "上传文件大小不能超过10M", "");
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
