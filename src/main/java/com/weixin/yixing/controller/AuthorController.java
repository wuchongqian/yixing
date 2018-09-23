package com.weixin.yixing.controller;

import com.commons.utils.ResultContent;
import com.weixin.yixing.serviceImpl.AuthorServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "AuthorController", description = "作者接口")
@RestController
public class AuthorController {

    @Autowired
    private AuthorServiceImpl authorServiceImpl;

    @ApiOperation(value="查询作者详情", notes="查询作者详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "openId", value = "微信用户ID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "activityId", value = "活动ID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "authorId", value = "作者ID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "token", value = "通讯密串", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/getAuthorInfo", method = {RequestMethod.GET})
    public ResultContent getAuthorInfo(String openId, String activityId, String authorId, String token) {
        return authorServiceImpl.getAuthorInfo(openId, activityId, authorId, token);
    }

    @ApiOperation(value="PC端查询作者详情", notes="PC端查询作者详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "authorId", value = "作者ID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "token", value = "通讯密串", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/getAuthorInfoForPC", method = {RequestMethod.GET})
    public ResultContent getAuthorInfoForPC(String authorId, String token) {
        return authorServiceImpl.getAuthorInfoForPC(authorId, token);
    }

    @ApiOperation(value="喜欢作者", notes="喜欢作者")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "openId", value = "微信用户ID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "authorId", value = "作者ID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "token", value = "通讯密串", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/addLikeOfAuthor", method = {RequestMethod.GET})
    public ResultContent addLikeOfAuthor(String openId, String authorId, String token) {
        return authorServiceImpl.addLikeOfAuthor(openId, authorId, token);
    }

    @ApiOperation(value="取消喜欢作者", notes="取消喜欢作者")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "openId", value = "微信用户ID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "authorId", value = "作者ID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "token", value = "通讯密串", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/cancelLikeOfAuthor", method = {RequestMethod.GET})
    public ResultContent cancelLikeOfAuthor(String openId, String authorId, String token) {
        return authorServiceImpl.cancelLikeOfAuthor(openId, authorId, token);
    }

}
