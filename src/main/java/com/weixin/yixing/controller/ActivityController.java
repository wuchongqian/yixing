package com.weixin.yixing.controller;

import com.commons.utils.ResultContent;
import com.commons.utils.ResultPage;
import com.weixin.yixing.serviceImpl.ActivityServiceImpl;
import com.weixin.yixing.serviceImpl.WorksServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Api(value = "ActivityController", description = "活动接口")
@RestController
public class ActivityController {
    @Autowired
    private ActivityServiceImpl activityServiceImpl;

    @Autowired
    private WorksServiceImpl worksServiceImpl;

    @ApiOperation(value="参加活动，上传作品", notes="参加活动，上传作品")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "openId", value = "微信用户ID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "formId", value = "微信formId", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "activityId", value = "活动ID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "authorName", value = "作者名", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "phone", value = "手机号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query",name="worksName",value ="作品名",required =true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name="introductionOfWorks",value ="作品介绍",required =true,dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "imageIdList", value = "图片ID列表：[{\"id\":\"001\"},{\"id\":\"002\"}]", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "token", value = "通讯密串", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/addRegisterInfo", method = {RequestMethod.POST})
    public ResultContent addRegisterInfo(String openId, String formId, String activityId, String authorName, String phone, String worksName, String introductionOfWorks, String imageIdList, String token) throws IOException {
        return activityServiceImpl.addRegisterInfo(openId, formId, activityId, authorName, phone, worksName, introductionOfWorks, imageIdList, token);
    }

    @ApiOperation(value="查询活动详情", notes="查询活动详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "activityId", value = "活动ID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "token", value = "通讯密串", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getActivityInfo", method = {RequestMethod.GET})
    public ResultContent getActivityInfo(String activityId, String token) {
        return activityServiceImpl.getActivityInfo(activityId, token);
    }

    @ApiOperation(value="添加活动信息", notes="添加活动信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "activityName", value = "活动名称", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "content", value = "活动详情", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "imageId", value = "活动图片ID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "deadline", value = "截止日期", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "token", value = "通讯密串", required = true, dataType = "String")
    })
    @RequestMapping(value = "/addActivityInfo", method = {RequestMethod.POST})
    public ResultContent addActivityInfo(String activityName, String content, String imageId, String deadline, String token) {
        return activityServiceImpl.addActivityInfo(activityName, content, imageId, deadline, token);
    }

    @ApiOperation(value="编辑更新活动信息", notes="编辑更新活动信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "activityId", value = "活动UUID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "activityName", value = "活动名称", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "content", value = "活动详情", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "imageId", value = "活动图片ID", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "token", value = "通讯密串", required = true, dataType = "String")
    })
    @RequestMapping(value = "/updateActivityInfo", method = {RequestMethod.POST})
    public ResultContent updateActivityInfo(String activityId, String activityName, String content, String imageId, String token) {
        return activityServiceImpl.updateActivityInfo(activityId, activityName, content, imageId, token);
    }

    @ApiOperation(value="修改活动截止日期", notes="修改活动截止日期")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "activityId", value = "活动ID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "deadline", value = "截止日期", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "token", value = "通讯密串", required = true, dataType = "String")
    })
    @RequestMapping(value = "/updateActivityDeadline", method = {RequestMethod.POST})
    public ResultContent updateActivityDeadline(String activityId, String deadline, String token) {
        return activityServiceImpl.updateActivityDeadline(activityId, deadline, token);
    }

    @ApiOperation(value="获取活动ID列表", notes="获取活动ID列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "通讯密串", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getActivityIdList", method = {RequestMethod.GET})
    public ResultContent getActivityIdList(String token) {
        return activityServiceImpl.getActivityIdList(token);
    }

}
