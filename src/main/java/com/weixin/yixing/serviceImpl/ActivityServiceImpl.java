package com.weixin.yixing.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.commons.utils.ResultContent;
import com.weixin.yixing.constants.Constants;
import com.weixin.yixing.dao.ActivityInfoMapper;
import com.weixin.yixing.dao.AuthorInfoMapper;
import com.weixin.yixing.dao.AuthorWorksMapper;
import com.weixin.yixing.dao.WorksInfoMapper;
import com.weixin.yixing.entity.ActivityInfo;
import com.weixin.yixing.entity.AuthorInfo;
import com.weixin.yixing.entity.AuthorWorks;
import com.weixin.yixing.entity.WorksInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ActivityServiceImpl {
    private static Logger logger= LoggerFactory.getLogger(ActivityServiceImpl.class);

    @Autowired
    private ActivityInfoMapper activityInfoMapper;

    @Autowired
    private WorksInfoMapper worksInfoMapper;

    @Autowired
    private AuthorInfoMapper authorInfoMapper;

    @Autowired
    private AuthorWorksMapper authorWorksMapper;

    /**
     *参加活动，上传作品
     * @param authorName
     * @param phone
     * @param worksName
     * @param introductionOfWorks
     * @param token
     * @return
     */
    public ResultContent addRegisterInfo(String activityId, String authorName, String phone, String worksName, String introductionOfWorks, String imageIdList,String token) {
        logger.info("开始添加作品信息");

        //添加作品
        WorksInfo works = new WorksInfo();
        works.setActivityId(activityId);
        String worksUuid = UUID.randomUUID().toString();
        works.setWorksUuid(worksUuid);
        works.setIntroductionOfWorks(introductionOfWorks);
        works.setWorksName(worksName);

        List<WorksInfo> list= worksInfoMapper.selectWorksInfoByActivityId(activityId);
        String worksNo = "";
        if (list.size() == 0){
            worksNo = String.valueOf(1);
        }else{
            worksNo = String.valueOf(Integer.valueOf(list.get(0).getWorksNum()) + 1);
        }
        works.setWorksNum(worksNo);
        works.setCreateTime(new Date());
        works.setModifyTime(new Date());

        //图片信息
        works.setImage(imageIdList);
        int worksResult = worksInfoMapper.insertSelective(works);
        if (worksResult<=0){
            return new ResultContent(Constants.REQUEST_FAILED, "新增作品信息错误", "{}");
        }

        //作者表插值
        AuthorInfo authorInfo = new AuthorInfo();
        authorInfo.setActivityId(activityId);
        authorInfo.setAuthorName(authorName);
        authorInfo.setPhone(phone);
        authorInfo.setGender("0");
        authorInfo.setCreateTime(new Date());
        String authorUuid = UUID.randomUUID().toString();
        authorInfo.setAuthorUuid(authorUuid);
        int authorResult = authorInfoMapper.insertSelective(authorInfo);
        if(authorResult <= 0){
            return new ResultContent(Constants.REQUEST_FAILED, "新增作者信息错误", "{}");
        }


        //作者和作品关联关系表
        AuthorWorks authorWorks = new AuthorWorks();
        authorWorks.setAuthorId(authorUuid);
        authorWorks.setWorksId(worksUuid);
        authorWorks.setCreateTime(new Date());
        int authorWorksResult = authorWorksMapper.insertSelective(authorWorks);
        if(authorResult <= 0){
            return new ResultContent(Constants.REQUEST_FAILED, "新增作者作品关联信息错误", "{}");
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("worksId", worksUuid);
        jsonObject.put("authorId", authorUuid);
        return  new ResultContent(Constants.REQUEST_SUCCESS, Constants.SUCCESS, jsonObject);
    }

    /**
     * 查询活动详情
     * @param activityId
     * @param token
     * @return
     */
    public ResultContent getActivityInfo(String activityId, String token){
        logger.info("查询活动详情");
        ActivityInfo result = activityInfoMapper.selectActivityInfoByActivityId(activityId);
        return new ResultContent(Constants.REQUEST_SUCCESS, Constants.SUCCESS, result);

    }
}
