package com.weixin.yixing.controller;

import com.commons.utils.ResultContent;
import com.weixin.yixing.annotation.LoginRequired;
import com.weixin.yixing.serviceImpl.ActivityServiceImpl;
import com.weixin.yixing.serviceImpl.DataStatisticsServiceImpl;
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

@Api(value = "MiniProgramController", description = "上传及统计接口")
@RestController
public class MiniProgramController {

    @Autowired
    private ActivityServiceImpl activityServiceImpl;

    @Autowired
    private WorksServiceImpl worksServiceImpl;

    @Autowired
    private DataStatisticsServiceImpl dataStatisticsServiceImpl;


    @ApiOperation(value="文件上传", notes="文件上传")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "token", value = "通讯密串", required = true, dataType = "String")
    })
    @LoginRequired
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public ResultContent uploadFile(MultipartFile  uploadFile , String token) {
        return worksServiceImpl.uploadFile(uploadFile);
    }

    @ApiOperation(value="数据统计查询", notes="数据统计查询")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "activityId", value = "活动ID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "token", value = "通讯密串", required = true, dataType = "String")
    })
    @LoginRequired
    @RequestMapping(value = "/getStatisticsDataInfo", method = {RequestMethod.GET})
    public ResultContent getStatisticsDataInfo(String activityId, String token) {
        return dataStatisticsServiceImpl.getStatisticsDataInfo(activityId, token);
    }

    @ApiOperation(value="点击量查询", notes="点击量查询")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "year", value = "活动ID", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType="query", name = "month", value = "活动ID", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "token", value = "通讯密串", required = true, dataType = "String")
    })
    @LoginRequired
    @RequestMapping(value = "/getViewsDistribute", method = {RequestMethod.GET})
    public ResultContent getViewsDistribute(Integer year, Integer month, String token) {
        return dataStatisticsServiceImpl.getViewsDistribute(year, month, token);
    }

}
