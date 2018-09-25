package com.weixin.yixing.controller;

import com.commons.utils.ResultContent;
import com.commons.utils.ResultPage;
import com.weixin.yixing.serviceImpl.GiftServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "GiftController", description = "礼物接口")
@RestController
public class GiftController {
    @Autowired
    private GiftServiceImpl giftServiceImpl;

    @ApiOperation(value="赠送礼物", notes="赠送礼物")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "giftId", value = "礼物ID(1.画纸，2.画笔，3.画板，4.画架，5.颜料，6.画框)", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "presenterId", value = "赠送人微信OpenID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "presenterName", value = "赠送者微信姓名", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "worksId", value = "作品ID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "token", value = "通讯密串", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/addGift", method = {RequestMethod.POST})
    public ResultContent addGift(String giftId, String presenterId, String presenterName, String worksId, String token) {
        return giftServiceImpl.addGift(giftId, presenterId, presenterName, worksId, token);
    }

    @ApiOperation(value="查询作品列表根据票数排序", notes="查询作品列表根据票数排序")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "worksId", value = "作品ID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pageNum", value = "页码，默认1", required = true, dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页大小，默认10", required = true, dataType = "int", defaultValue = "10"),
            @ApiImplicitParam(paramType = "query", name = "token", value = "通讯密串", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getGiftTrack", method = {RequestMethod.GET})
    public ResultPage getGiftTrack(String worksId, String pageNum, String pageSize, String  token) {
        return giftServiceImpl.getGiftTrack(worksId, pageNum, pageSize, token);
    }
}
