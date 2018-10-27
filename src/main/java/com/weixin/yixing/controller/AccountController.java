package com.weixin.yixing.controller;

import com.commons.utils.ResultContent;
import com.weixin.yixing.annotation.LoginRequired;
import com.weixin.yixing.serviceImpl.AccountServiceImpl;
import com.weixin.yixing.serviceImpl.WeChatPayServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Api(value = "WeChatController", description = "微信接口")
@RestController
public class AccountController {

    @Autowired
    private AccountServiceImpl accountServiceImpl;

    @Autowired
    private WeChatPayServiceImpl weChatPayServiceImpl;

    @ApiOperation(value="微信登录", notes="微信登录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "code", value = "微信code", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/wetChatLogin", method = {RequestMethod.GET})
    public ResultContent wetChatLogin(String code) {
        return accountServiceImpl.weChatLogin(code);
    }

    @ApiOperation(value="更新微信账号信息", notes="更新微信账号信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "openId", value = "微信OPENID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "rawData", value = "微信账号信息字符串", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/updateWeChatUserInfo", method = {RequestMethod.POST})
    public ResultContent updateWeChatUserInfo(String openId, String rawData) {
        return accountServiceImpl.updateWeChatUserInfo(openId, rawData);
    }

    @ApiOperation(value="获取最新活动信息", notes="获取最新活动信息")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/getActivityId", method = {RequestMethod.GET})
    public ResultContent getActivityId() {
        return accountServiceImpl.getActivityId();
    }

    @ApiOperation(value="PC后台登录", notes="PC后台登录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "password", value = "密码", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/pcLogin", method = {RequestMethod.GET})
    public ResultContent pcLogin(String username, String password) {
        return accountServiceImpl.pcLogin(username, password);
    }

    @ApiOperation(value="PC后台新增账号", notes="PC后台新增账号")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "password", value = "密码", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/addAccount", method = {RequestMethod.POST})
    public ResultContent addAccount(String username, String password) {
        return accountServiceImpl.addAccount(username, password);
    }

    @ApiOperation(value="微信支付", notes="微信支付")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "openid", value = "微信OpenId", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "giftId", value = "礼物Id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "worksId", value = "作品Id", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType="query", name = "request", value = "请求", required = true, dataType = "HttpServletRequest"),
            @ApiImplicitParam(paramType="query", name = "token", value = "通讯密串", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/weChatPay", method = {RequestMethod.POST})
    public ResultContent weChatPay(String openid, HttpServletRequest request,String giftId, String worksId, String token) throws Exception{
        return weChatPayServiceImpl.weChatPay(openid, request, giftId, worksId, token);
    }

}
