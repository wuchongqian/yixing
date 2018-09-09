package com.weixin.yixing.serviceImpl;

import com.commons.utils.ResultContent;
import com.weixin.yixing.constants.Constants;
import com.weixin.yixing.dao.ActivityInfoMapper;
import com.weixin.yixing.entity.ActivityInfo;
import com.weixin.yixing.entity.WorksInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityServiceImpl {
    private static Logger logger= LoggerFactory.getLogger(ActivityServiceImpl.class);

    @Autowired
    private ActivityInfoMapper activityInfoMapper;

    /**
     *参加活动，上传作品
     * @param authorName
     * @param phone
     * @param worksName
     * @param introductionOfWorks
     * @param token
     * @return
     */
    public ResultContent addRegisterInfo(String activityId,String authorName, String phone, String worksName, String introductionOfWorks, String token) {
        logger.info("开始添加作品信息");

        //
//        WorksInfo works = new WorksInfo();
//        works.setActivityId(activityId);
//
//        works.setIntroductionOfWorks(introductionOfWorks);
//        works.setWorksName(worksName);
        return  null;
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
