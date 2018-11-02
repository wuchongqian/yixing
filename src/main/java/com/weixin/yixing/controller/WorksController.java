package com.weixin.yixing.controller;

import com.commons.utils.ResultContent;
import com.commons.utils.ResultPage;
import com.weixin.yixing.annotation.LoginRequired;
import com.weixin.yixing.serviceImpl.WorksServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "WorksController", description = "作品接口")
@RestController
@CrossOrigin
public class WorksController {
    @Autowired
    private WorksServiceImpl worksServiceImpl;

    @ApiOperation(value="查询PC端作品列表根据时间排序", notes="查询PC端作品列表根据时间排序")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "activityId", value = "活动ID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "keyword", value = "关键字查询（作品号，作品名次）", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pageNum", value = "页码，默认1", required = true, dataType = "String", defaultValue = "1"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页大小，默认10", required = true, dataType = "String", defaultValue = "10"),
            @ApiImplicitParam(paramType = "query", name = "token", value = "通讯密串", required = true, dataType = "String")
    })
    @LoginRequired
    @RequestMapping(value = "/getWorksListByCreateTimeForPC", method = {RequestMethod.GET})
    public ResultPage getWorksListByCreateTimeForPC(String activityId, String keyword, String pageNum, String pageSize, String  token) {
        return worksServiceImpl.getWorksListByCreateTimeForPC(activityId, keyword, pageNum, pageSize, token);
    }

    @ApiOperation(value="查询PC端作品列表根据人气排序", notes="查询PC端作品列表根据人气排序")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "activityId", value = "活动ID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "keyword", value = "关键字查询（作品号，作品名次）", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pageNum", value = "页码，默认1", required = true, dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页大小，默认10", required = true, dataType = "int", defaultValue = "10"),
            @ApiImplicitParam(paramType = "query", name = "token", value = "通讯密串", required = true, dataType = "String")
    })
    @LoginRequired
    @RequestMapping(value = "/getWorksListByVotesForPC", method = {RequestMethod.GET})
    public ResultPage getWorksListByVotesForPC(String activityId, String keyword, String pageNum, String pageSize, String  token) {
        return worksServiceImpl.getWorksListByVotesForPC(activityId, keyword, pageNum, pageSize, token);
    }

    @ApiOperation(value="查询作品列表根据时间排序", notes="查询作品列表根据时间排序")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "activityId", value = "活动ID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "keyword", value = "关键字查询（作品号，作品名次）", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pageNum", value = "页码，默认1", required = true, dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页大小，默认10", required = true, dataType = "int", defaultValue = "10"),
            @ApiImplicitParam(paramType = "query", name = "token", value = "通讯密串", required = true, dataType = "String")
    })
    @LoginRequired
    @RequestMapping(value = "/getWorksListByCreateTime", method = {RequestMethod.GET})
    public ResultPage getWorksListByCreateTime(String activityId, String keyword, String pageNum, String pageSize, String  token) {
        return worksServiceImpl.getWorksListByCreateTime(activityId, keyword, pageNum, pageSize, token);
    }

    @ApiOperation(value="查询作品列表根据票数排序", notes="查询作品列表根据票数排序")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "activityId", value = "活动ID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "keyword", value = "关键字查询（作品号，作品名次）", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pageNum", value = "页码，默认1", required = true, dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页大小，默认10", required = true, dataType = "int", defaultValue = "10"),
            @ApiImplicitParam(paramType = "query", name = "token", value = "通讯密串", required = true, dataType = "String")
    })
    @LoginRequired
    @RequestMapping(value = "/getWorksListByVotes", method = {RequestMethod.GET})
    public ResultPage getWorksListByVotes(String activityId, String keyword, String pageNum, String pageSize, String  token) {
        return worksServiceImpl.getWorksListByVotes(activityId, keyword, pageNum, pageSize, token);
    }

    @ApiOperation(value="查询未审核作品列表", notes="查询未审核作品列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "pageNum", value = "页码，默认1", required = true, dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页大小，默认10", required = true, dataType = "int", defaultValue = "10"),
            @ApiImplicitParam(paramType = "query", name = "token", value = "通讯密串", required = true, dataType = "String")
    })
    @LoginRequired
    @RequestMapping(value = "/getUnrReviewedWorksList", method = {RequestMethod.GET})
    public ResultPage getUnrReviewedWorksList(String pageNum, String pageSize, String  token) {
        return worksServiceImpl.getUnrReviewedWorksList(pageNum, pageSize, token);
    }

    @ApiOperation(value="查询未审核作品数量", notes="查询未审核作品数量")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "通讯密串", required = true, dataType = "String")
    })
    @LoginRequired
    @RequestMapping(value = "/getNumOfUnReviewedWorks", method = {RequestMethod.GET})
    public ResultContent getNumOfUnReviewedWorks(String token) {
        return worksServiceImpl.getNumOfUnReviewedWorks(token);
    }

    @ApiOperation(value="查询报名人数", notes="查询报名人数")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "activityId", value = "活动ID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "token", value = "通讯密串", required = true, dataType = "String")
    })
    @LoginRequired
    @RequestMapping(value = "/getNumOfRegistration", method = {RequestMethod.GET})
    public ResultContent getNumOfRegistration(String activityId, String token) {
        return worksServiceImpl.getNumOfRegistration(activityId, token);
    }

    @ApiOperation(value="作品投票", notes="作品投票")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "openId", value = "微信用户ID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "worksId", value = "作品UUID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "token", value = "通讯密串", required = true, dataType = "String")
    })
    @LoginRequired
    @RequestMapping(value = "/addNumOfVotesOnce", method = {RequestMethod.POST})
    public ResultContent addNumOfVotesOnce(String openId, String worksId, String token) {
        return worksServiceImpl.addNumOfVotesOnce(openId,worksId, token);
    }

    @ApiOperation(value="点击广告", notes="点击广告")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "openId", value = "微信用户ID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "worksId", value = "作品UUID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "adUrl", value = "广告url", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "token", value = "通讯密串", required = true, dataType = "String")
    })
    @LoginRequired
    @RequestMapping(value = "/viewAds", method = {RequestMethod.POST})
    public ResultContent viewAds(String openId,String worksId, String adUrl, String token) {
        return worksServiceImpl.viewAds(openId,worksId,adUrl, token);
    }

    @ApiOperation(value="查询作品详情", notes="查询作品详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "worksId", value = "作品UUID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "token", value = "通讯密串", required = true, dataType = "String")
    })
    @LoginRequired
    @RequestMapping(value = "/getWorksInfo", method = {RequestMethod.GET})
    public ResultContent getWorksInfo(String worksId, String token) {
        return worksServiceImpl.getWorksInfo(worksId, token);
    }

    @ApiOperation(value="查询作品排行榜信息", notes="查询作品排行榜信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "activityId", value = "活动ID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pageNum", value = "页码，默认1", required = true, dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页大小，默认10", required = true, dataType = "int", defaultValue = "10"),
            @ApiImplicitParam(paramType = "query", name = "token", value = "通讯密串", required = true, dataType = "String")
    })
    @LoginRequired
    @RequestMapping(value = "/getWorksLeaderBoard", method = {RequestMethod.GET})
    public ResultPage getWorksLeaderBoard(String activityId, String pageNum, String pageSize, String  token) {
        return worksServiceImpl.getWorksLeaderBoard(activityId, pageNum, pageSize, token);
    }

    @ApiOperation(value="审核作品", notes="审核作品")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "worksId", value = "作品UUID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "status", value = "作品状态（‘1’表示审核通过，‘2’表示不通过）", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "token", value = "通讯密串", required = true, dataType = "String")
    })
    @LoginRequired
    @RequestMapping(value = "/updateUnReviewedWorksInfo", method = {RequestMethod.POST})
    public ResultContent updateUnReviewedWorksInfo(String worksId, String status, String token) {
        return worksServiceImpl.updateUnReviewedWorksInfo(worksId, status, token);
    }

    @ApiOperation(value="修改作品得票数以及点击量", notes="修改作品得票数以及点击量")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "worksId", value = "作品UUID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "numOfVotes", value = "作品票数", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "numOfClicks", value = "作品点击量", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "token", value = "通讯密串", required = true, dataType = "String")
    })
    @LoginRequired
    @RequestMapping(value = "/updateNumOfVotes", method = {RequestMethod.POST})
    public ResultContent updateNumOfVotes(String worksId, String numOfVotes,String numOfClicks,  String token) {
        return worksServiceImpl.updateNumOfVotes(worksId, numOfVotes, numOfClicks, token);
    }

    @ApiOperation(value="新增作品点击量", notes="新增作品点击量")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "worksId", value = "作品UUID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "token", value = "通讯密串", required = true, dataType = "String")
    })
    @LoginRequired
    @RequestMapping(value = "/addClicksOfWorksOnce", method = {RequestMethod.POST})
    public ResultContent addClicksOfWorksOnce(String worksId, String token) {
        return worksServiceImpl.addClicksOfWorksOnce(worksId, token);
    }

    @ApiOperation(value="查询所有参赛作品", notes="查询所有参赛作品")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "pageNum", value = "页码，默认1", required = true, dataType = "String", defaultValue = "1"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页大小，默认10", required = true, dataType = "String", defaultValue = "10"),
            @ApiImplicitParam(paramType = "query", name = "token", value = "通讯密串", required = true, dataType = "String")
    })
    @LoginRequired
    @RequestMapping(value = "/getAllActivityList", method = {RequestMethod.GET})
    public ResultPage getAllActivityList(String pageNum, String pageSize, String  token) {
        return worksServiceImpl.getAllActivityList(pageNum, pageSize, token);
    }

}
