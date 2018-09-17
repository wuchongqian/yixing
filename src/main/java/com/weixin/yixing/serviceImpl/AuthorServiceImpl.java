package com.weixin.yixing.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.commons.utils.ResultContent;
import com.weixin.yixing.constants.Constants;
import com.weixin.yixing.dao.ActivityInfoMapper;
import com.weixin.yixing.dao.AuthorInfoMapper;
import com.weixin.yixing.dao.WorksInfoMapper;
import com.weixin.yixing.entity.ActivityInfo;
import com.weixin.yixing.entity.AuthorInfo;
import com.weixin.yixing.entity.AuthorList;
import com.weixin.yixing.entity.WorksInfo;
import com.weixin.yixing.entity.vo.GetWidthResizedImageUrlRequest;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * 查询作者详情
     * @param authorId
     * @param token
     * @return
     */
    public ResultContent getAuthorInfo(String activityId, String authorId, String token){
        logger.info("开始查询作者详情");
        AuthorInfo authorInfo = authorInfoMapper.selectAuthorInfoByAuthorId(authorId);
        AuthorList author = new AuthorList();
        author.setAuthorUuid(authorInfo.getAuthorUuid());
        author.setAuthorName(authorInfo.getAuthorName());
        author.setIntroductionOfAuthor(authorInfo.getIntroductionOfAuthor());
        author.setLike(authorInfo.getLike());
        author.setPhone(authorInfo.getPhone());
        Integer sum = worksInfoMapper.getSumOfVotesByAuthorId(authorId);
        author.setSumOfVotes(sum.toString());
        List<JSONObject>worksList = new ArrayList<>();
        //获取作者作品列表
        List<WorksInfo> worksInfoList= worksInfoMapper.selectWorksInfoByAuthorId(authorId, activityId);
        for (WorksInfo worksInfo: worksInfoList){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("numOfVotes", worksInfo.getNumberOfVotes());
            jsonObject.put("worksNo", worksInfo.getWorksNum());
            jsonObject.put("introductionOfWorks", worksInfo.getIntroductionOfWorks());
            jsonObject.put("numOfClicks", worksInfo.getNumOfClicks());
            jsonObject.put("worksName", worksInfo.getWorksName());
            String imageListStr = JSONArray.toJSONString(getImageUrlList(worksInfo.getImage()));
            jsonObject.put("imageListStr", imageListStr);
            worksList.add(jsonObject);
        }
        author.setWorksList(worksList);
        return new ResultContent(Constants.REQUEST_SUCCESS, Constants.SUCCESS, authorInfo);
    }

    /**
     * PC端查询作者详情
     * @param authorId
     * @param token
     * @return
     */
    public ResultContent getAuthorInfoForPC(String authorId, String token){
        logger.info("开始查询作者详情");
        AuthorInfo authorInfo = authorInfoMapper.selectAuthorInfoByAuthorId(authorId);
        AuthorList author = new AuthorList();
        author.setAuthorUuid(authorInfo.getAuthorUuid());
        author.setAuthorName(authorInfo.getAuthorName());
        author.setIntroductionOfAuthor(authorInfo.getIntroductionOfAuthor());
        author.setLike(authorInfo.getLike());
        author.setPhone(authorInfo.getPhone());
        Integer sum = worksInfoMapper.getSumOfVotesByAuthorId(authorId);
        author.setSumOfVotes(sum.toString());
        List<Map<String, Object>>mapList = new ArrayList<>();
        List<AuthorInfo> authorList = authorInfoMapper.selectAuthorInfoByPhone(authorId);
        for(AuthorInfo author1:authorList){
            //获取作者作品列表
            String activityId = author1.getActivityId();
            List<WorksInfo> worksInfoList= worksInfoMapper.selectWorksInfoByAuthorId(authorId, activityId);
            Map<String, Object>map = new HashMap();
            List<JSONObject>worksList = new ArrayList<>();
            map.put("activityId", activityId);
            ActivityInfo activityInfo= activityInfoMapper.selectActivityInfoByActivityId(activityId);
            map.put("activityName", activityInfo.getActivityName());
            for (WorksInfo worksInfo: worksInfoList){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("numOfVotes", worksInfo.getNumberOfVotes());
                jsonObject.put("worksNo", worksInfo.getWorksNum());
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
        return new ResultContent(Constants.REQUEST_SUCCESS, Constants.SUCCESS, authorInfo);
    }

    /**
     * 增加喜欢作者
     * @param authorId
     * @param token
     * @return
     */
    public ResultContent addLikeOfAuthor(String authorId, String token){
        logger.info("开始添加喜欢作者");
        //查询作者喜欢数
        AuthorInfo author = authorInfoMapper.selectAuthorInfoByAuthorId(authorId);

        AuthorInfo authorInfo = new AuthorInfo();
        authorInfo.setAuthorUuid(authorId);
        authorInfo.setLike(author.getLike() + 1);
        int result = authorInfoMapper.updateByPrimaryKeySelective(authorInfo);
        if (result > 0){
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
                        imageUrl = map.get(imageUrl);
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
