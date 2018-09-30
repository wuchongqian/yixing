package com.weixin.yixing.dao;

import com.weixin.yixing.entity.WeChatUser;

public interface WeChatUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WeChatUser record);

    int insertSelective(WeChatUser record);

    WeChatUser selectByPrimaryKey(Integer id);

    WeChatUser findByOpenid(String openId);

    int updateByPrimaryKeySelective(WeChatUser record);

    int updateByPrimaryKey(WeChatUser record);
}