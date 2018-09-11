package com.weixin.yixing.controller;

import com.commons.utils.ResultContent;
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

@Api(value = "YiXingController", description = "小程序")
@RestController
public class MiniProgramController {

    @Autowired
    private ActivityServiceImpl activityServiceImpl;

    @Autowired
    private WorksServiceImpl worksServiceImpl;


    @ApiOperation(value="参加活动，上传作品", notes="参加活动，上传作品")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "activityId", value = "活动ID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "authorName", value = "作者名", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "phone", value = "手机号", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query",name="worksName",value ="作品名",required =false,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name="introductionOfWorks",value ="作品介绍",required =false,dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "imageIdList", value = "图片ID列表：[{\"id\":\"001\"},{\"id\":\"002\"}]", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "token", value = "通讯密串", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/addRegisterInfo", method = {RequestMethod.POST})
    public ResultContent addRegisterInfo(String activityId, String authorName, String phone, String worksName, String introductionOfWorks,String imageIdList, String token) throws IOException{
        return activityServiceImpl.addRegisterInfo(activityId, authorName, phone, worksName, introductionOfWorks, imageIdList, token);
    }

    @ApiOperation(value="文件上传", notes="文件上传")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "token", value = "通讯密串", required = true, dataType = "String")
    })
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public ResultContent uploadFile(MultipartFile  uploadFile , String token) {
        return worksServiceImpl.uploadFile(uploadFile);
    }
}
