package com.weixin.yixing.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.commons.utils.ResultContent;
import com.weixin.yixing.constants.Constants;
import com.weixin.yixing.dao.*;
import com.weixin.yixing.entity.*;
import com.weixin.yixing.entity.vo.GetWidthResizedImageUrlRequest;
import com.weixin.yixing.exception.CoreException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthorServiceImpl {
    private static Logger logger= LoggerFactory.getLogger(AuthorServiceImpl.class);

    @Autowired
    private AuthorInfoMapper authorInfoMapper;

    @Autowired
    private WorksInfoMapper worksInfoMapper;

    @Autowired
    private FileServiceImpl fileServiceImpl;

    @Autowired
    private ActivityInfoMapper activityInfoMapper;

    @Autowired
    private UserAuthorRecordMapper userAuthorRecordMapper;

    @Autowired
    private WeChatUserMapper weChatUserMapper;

    /**
     * 查询作者详情
     * @param authorId
     * @param token
     * @return
     */
    public ResultContent getAuthorInfo(String openId, String activityId, String authorId, String token){
        logger.info("开始查询作者详情");
        AuthorInfo authorInfo = authorInfoMapper.selectAuthorInfoByAuthorId(authorId);
        if(null == authorInfo){
            throw CoreException.of(CoreException.RESOURCE_NO_FOUND);
        }
        AuthorList author = new AuthorList();
        author.setAuthorUuid(authorInfo.getAuthorUuid());
        author.setAuthorName(authorInfo.getAuthorName());
        author.setIntroductionOfAuthor(authorInfo.getIntroductionOfAuthor());
        author.setLike(authorInfo.getLikes());
        author.setPhone(authorInfo.getPhone());
        WeChatUser weChatUser = weChatUserMapper.findByOpenid(authorInfo.getWechatOpenId());
        if(null != weChatUser){
            author.setAvatarUrl(weChatUser.getAvatarurl());
        }else{
            author.setAvatarUrl("");
        }

        Integer sum = worksInfoMapper.getSumOfVotesByAuthorId(authorId);
        author.setSumOfVotes(sum.toString());
        Map<String, Object> map = new HashMap<>();
        map.put("openId", openId);
        map.put("authorId", authorId);
        UserAuthorRecord userAuthorRecord = userAuthorRecordMapper.selectByWorksId(map);
        if (null != userAuthorRecord){
            author.setLikeStatus(userAuthorRecord.getLikeStatus());
        }
        List<JSONObject>worksList = new ArrayList<>();
        //获取作者作品列表
        Map<String, Object> map1 = new HashMap<>();
        map1.put("activityId",activityId);
        map1.put("authorId",authorId);
        List<WorksInfo> worksInfoList= worksInfoMapper.selectWorksInfoByAuthorId(map1);
        for (WorksInfo worksInfo: worksInfoList){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("numOfVotes", worksInfo.getNumberOfVotes());
            jsonObject.put("worksNo", worksInfo.getWorksNum());
            jsonObject.put("worksId", worksInfo.getWorksUuid());
            jsonObject.put("introductionOfWorks", worksInfo.getIntroductionOfWorks());
            jsonObject.put("numOfClicks", worksInfo.getNumOfClicks());
            jsonObject.put("worksName", worksInfo.getWorksName());
            String imageListStr = JSONArray.toJSONString(getImageUrlList(worksInfo.getImage()));
            jsonObject.put("imageListStr", imageListStr);
            worksList.add(jsonObject);
        }
        author.setWorksList(worksList);
        return new ResultContent(Constants.REQUEST_SUCCESS, Constants.SUCCESS, author);
    }

    /**
     * PC端查询作者详情
     * @param authorId
     * @param token
     * @return
     */
    public ResultContent getAuthorInfoForPC(String authorId, String token){
        logger.info("开始PC端查询作者详情");
        AuthorInfo authorInfo = authorInfoMapper.selectAuthorInfoByAuthorId(authorId);
        if (null == authorInfo){
            return new ResultContent(Constants.REQUEST_FAILED, "作者ID不存在", "{}");
        }
        AuthorList author = new AuthorList();
        author.setAuthorUuid(authorInfo.getAuthorUuid());
        author.setAuthorName(authorInfo.getAuthorName());
        author.setIntroductionOfAuthor(authorInfo.getIntroductionOfAuthor());
        author.setLike(authorInfo.getLikes());
        author.setPhone(authorInfo.getPhone());
        WeChatUser weChatUser = weChatUserMapper.findByOpenid(authorInfo.getWechatOpenId());
        if(null != weChatUser){
            author.setAvatarUrl(weChatUser.getAvatarurl());
        }else{
            author.setAvatarUrl("");
        }
        Integer sum = worksInfoMapper.getSumOfVotesByAuthorId(authorId);
        author.setSumOfVotes(sum.toString());
        List<Map<String, Object>>mapList = new ArrayList<>();
        List<AuthorInfo> authorList = authorInfoMapper.selectAuthorInfoByPhone(authorInfo.getPhone());
        for(AuthorInfo author1:authorList){
            //获取作者作品列表
            String activityId = author1.getActivityId();
            Map<String, Object> map1 = new HashMap<>();
            map1.put("activityId",activityId);
            map1.put("authorId",authorId);
            List<WorksInfo> worksInfoList= worksInfoMapper.selectWorksInfoByAuthorId(map1);
            Map<String, Object>map = new HashMap();
            List<JSONObject>worksList = new ArrayList<>();
            map.put("activityId", activityId);
            ActivityInfo activityInfo= activityInfoMapper.selectActivityInfoByActivityId(activityId);
            if (null != activityInfo){
                map.put("activityName", activityInfo.getActivityName());
            }else{
                map.put("activityName", "");
            }
            for (WorksInfo worksInfo: worksInfoList){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("numOfVotes", worksInfo.getNumberOfVotes());
                jsonObject.put("worksNo", worksInfo.getWorksNum());
                jsonObject.put("worksId", worksInfo.getWorksUuid());
                jsonObject.put("status", worksInfo.getStatus());
                jsonObject.put("introductionOfWorks", worksInfo.getIntroductionOfWorks());
                jsonObject.put("numOfClicks", worksInfo.getNumOfClicks());
                jsonObject.put("worksName", worksInfo.getWorksName());
                String imageListStr = JSONArray.toJSONString(getImageUrlList(worksInfo.getImage()));
                jsonObject.put("imageListStr", imageListStr);
                worksList.add(jsonObject);
            }
            map.put("worksList", worksList);
            mapList.add(map);
        }

        author.setWorksList(mapList);
        return new ResultContent(Constants.REQUEST_SUCCESS, Constants.SUCCESS, author);
    }

    /**
     * 增加喜欢作者
     * @param authorId
     * @param token
     * @return
     */
    public ResultContent addLikeOfAuthor(String openId, String authorId, String token){
        logger.info("开始添加喜欢作者");
        Map<String, Object> map = new HashMap();
        map.put("openId", openId);
        map.put("authorId", authorId);
        UserAuthorRecord userAuthorRecord = new UserAuthorRecord();
        int recordResult = 0;
        UserAuthorRecord userAuthor = userAuthorRecordMapper.selectByWorksId(map);
        if (null == userAuthor){
            userAuthorRecord.setAuthorUuid(authorId);
            userAuthorRecord.setWechatOpenid(openId);
            userAuthorRecord.setLikeStatus("1");
            userAuthorRecord.setCreateTime(new Date());
            userAuthorRecord.setModifyTime(new Date());
            recordResult = userAuthorRecordMapper.insert(userAuthorRecord);
        }else {
            userAuthorRecord.setAuthorUuid(authorId);
            userAuthorRecord.setWechatOpenid(openId);
            userAuthorRecord.setLikeStatus("1");
            userAuthorRecord.setModifyTime(new Date());
            recordResult = userAuthorRecordMapper.updateByPrimaryKeySelective(userAuthorRecord);
        }

        AuthorInfo author = authorInfoMapper.selectAuthorInfoByAuthorId(authorId);
        if (null == author){
            throw CoreException.of(CoreException.RESOURCE_NO_FOUND);
        }
        AuthorInfo authorInfo = new AuthorInfo();
        authorInfo.setAuthorUuid(authorId);
        authorInfo.setLikes(author.getLikes() + 1);
        int result = authorInfoMapper.updateByPrimaryKeySelective(authorInfo);
        if (result > 0 && recordResult >0){
            return new ResultContent(Constants.REQUEST_SUCCESS, Constants.SUCCESS, new JSONObject());
        }else{
            return new ResultContent(Constants.REQUEST_FAILED, Constants.FAILED, new JSONObject());
        }
    }

    /**
     * 取消喜欢作者
     * @param authorId
     * @param openId
     * @param token
     * @return
     */
    public ResultContent cancelLikeOfAuthor(String openId, String authorId, String token){
        logger.info("开始取消喜欢作者");
        UserAuthorRecord userAuthorRecord = new UserAuthorRecord();
        userAuthorRecord.setAuthorUuid(authorId);
        userAuthorRecord.setWechatOpenid(openId);
        userAuthorRecord.setLikeStatus("0");
        userAuthorRecord.setModifyTime(new Date());
        int recordResult = userAuthorRecordMapper.updateByPrimaryKeySelective(userAuthorRecord);
        //查询作者喜欢数
        AuthorInfo author = authorInfoMapper.selectAuthorInfoByAuthorId(authorId);
        if (null == author){
            throw CoreException.of(CoreException.RESOURCE_NO_FOUND);
        }
        if (author.getLikes() == 0){
            return new ResultContent(Constants.REQUEST_SUCCESS, "喜欢数为0，无法取消", new JSONObject());
        }
        AuthorInfo authorInfo = new AuthorInfo();
        authorInfo.setAuthorUuid(authorId);
        authorInfo.setLikes(author.getLikes() - 1);
        int result = authorInfoMapper.updateByPrimaryKeySelective(authorInfo);
        if (result > 0 && recordResult >0){
            return new ResultContent(Constants.REQUEST_SUCCESS, Constants.SUCCESS, new JSONObject());
        }else{
            return new ResultContent(Constants.REQUEST_FAILED, Constants.FAILED, new JSONObject());
        }
    }

    private List getImageUrlList(String imageInfo){
        List<String> imageUrlList=new ArrayList<>();
        if(StringUtils.isNotEmpty(imageInfo)) {
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

}
