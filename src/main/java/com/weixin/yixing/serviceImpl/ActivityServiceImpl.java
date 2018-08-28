package com.weixin.yixing.serviceImpl;

import com.commons.utils.ResultContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ActivityServiceImpl {
    private static Logger logger= LoggerFactory.getLogger(ActivityServiceImpl.class);

    /**
     *
     * @param authorName
     * @param phone
     * @param worksName
     * @param introductionOfWorks
     * @param file
     * @param token
     * @return
     */
    public ResultContent addRegisterInfo(String authorName, String phone, String worksName, String introductionOfWorks, MultipartFile file, String token){
        return  null;
    }

    public ResultContent getActivityInfo(String activityId, String token){
        return null;
    }
}
