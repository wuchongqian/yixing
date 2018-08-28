package com.weixin.yixing.dao;

import com.weixin.yixing.entity.AccountOfWeChat;

public interface AccountOfWeChatMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AccountOfWeChat record);

    int insertSelective(AccountOfWeChat record);

    AccountOfWeChat selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AccountOfWeChat record);

    int updateByPrimaryKey(AccountOfWeChat record);
}