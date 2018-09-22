package com.weixin.yixing.controller;

import com.alibaba.fastjson.JSONObject;
import com.commons.utils.ResultContent;
import com.weixin.yixing.serviceImpl.AccountServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "WeChatController", description = "微信接口")
@RestController
public class AccountController {

    @Autowired
    private AccountServiceImpl accountServiceImpl;

    @ApiOperation(value="微信登录", notes="微信登录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "code", value = "活动ID", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/wetChatLogin", method = {RequestMethod.GET})
    public JSONObject getSessionKeyOrOpenId(String code) {
        return accountServiceImpl.getSessionKeyOrOpenId(code);
    }
}
