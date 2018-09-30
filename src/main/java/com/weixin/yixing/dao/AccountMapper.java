package com.weixin.yixing.dao;

import com.weixin.yixing.entity.Account;

import java.util.List;

public interface AccountMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Account record);

    int insertSelective(Account record);

    Account selectByPrimaryKey(Integer id);

    List<Account> selectByAccount(String account);

    int updateByPrimaryKeySelective(Account record);

    int updateByPrimaryKey(Account record);
}