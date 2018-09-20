package com.weixin.yixing.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.commons.utils.ResultContent;
import com.commons.utils.ResultPage;
import com.commons.utils.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        if(StringUtils.isEmpty(activityId) ){
            return new ResultContent(Constants.REQUEST_FAILED, "activityId参数不能为空，请重新填写", "{}");
        }
        if(StringUtils.isEmpty(authorName)){
            return new ResultContent(Constants.REQUEST_FAILED, "authorName参数不能为空，请重新填写", "{}");
        }
        if(StringUtils.isEmpty(phone)){
            return new ResultContent(Constants.REQUEST_FAILED, "phone参数不能为空，请重新填写", "{}");
        }
        if(StringUtils.isEmpty(worksName)){
            return new ResultContent(Constants.REQUEST_FAILED, "worksName参数不能为空，请重新填写", "{}");
        }
        if(StringUtils.isEmpty(introductionOfWorks)){
            return new ResultContent(Constants.REQUEST_FAILED, "introductionOfWorks参数不能为空，请重新填写", "{}");
        }
        if(StringUtils.isEmpty(imageIdList)){
            return new ResultContent(Constants.REQUEST_FAILED, "imageIdList参数不能为空，请重新填写", "{}");
        }
//        List<AuthorInfo> authors = authorInfoMapper.selectAuthorInfoByPhone(phone);
//        if(authors.size()>0){
//
//        }
        //添加作品
        WorksInfo works = new WorksInfo();
        works.setActivityId(activityId);
        String worksUuid = UUID.randomUUID().toString();
        String authorUuid = UUID.randomUUID().toString();
        works.setWorksUuid(worksUuid);
        works.setIntroductionOfWorks(introductionOfWorks);
        if (StringUtils.isEmpty(worksName)) {
            works.setWorksName("");
        }else{
            works.setWorksName(worksName);
        }

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
        works.setAuthorId(authorUuid);

        //图片信息
        works.setImage(imageIdList);
        works.setStatus("0");
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

    /**
     * 添加活动信息
     * @param activityName
     * @param content
     * @param deadline
     * @param token
     * @return
     */
    public ResultContent addActivityInfo(String activityName, String content, String imageId, String deadline, String token){
        logger.info("开始添加活动信息");

        ActivityInfo activityInfo = new ActivityInfo();
        String activityUuid = UUID.randomUUID().toString();
        activityInfo.setActivityId(activityUuid);
        activityInfo.setActivityName(activityName);
        activityInfo.setIntroductionOfActivity(content);
        activityInfo.setImage(imageId);
        activityInfo.setCreateTime(new Date());
        activityInfo.setModifyTime(new Date());
        if (StringUtils.isEmpty(deadline)){
            activityInfo.setDeadline(new Date());
        }else{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try{
                activityInfo.setDeadline(sdf.parse(deadline));
            }catch (ParseException e){
                e.printStackTrace();
            }
        }

        int result = activityInfoMapper.insertSelective(activityInfo);
        JSONObject jsonObject = new JSONObject();
        if (result>0){
            jsonObject.put("activityUuid", activityUuid);
            return new ResultContent(Constants.REQUEST_SUCCESS, Constants.SUCCESS, jsonObject);
        }else{
            return new ResultContent(Constants.REQUEST_FAILED, Constants.FAILED, jsonObject);
        }
    }

    /**
     * 修改活动截止日期
     * @param activityId
     * @param deadline
     * @param token
     * @return
     */
    public ResultContent updateActivityDeadline(String activityId, String deadline, String token){
        logger.info("开始修改活动截止日期");

        ActivityInfo activityInfo = new ActivityInfo();
        activityInfo.setActivityId(activityId);
        activityInfo.setModifyTime(new Date());
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            Date date = sdf.parse(deadline);
            activityInfo.setDeadline(date);
        }catch (ParseException pe) {
            pe.printStackTrace();
        }
        int result = activityInfoMapper.updateByPrimaryKeySelective(activityInfo);
        if (result>0){
            return new ResultContent(Constants.REQUEST_SUCCESS, Constants.SUCCESS, new JSONObject());
        }else{
            return new ResultContent(Constants.REQUEST_FAILED, Constants.FAILED, new JSONObject());
        }
    }
}
