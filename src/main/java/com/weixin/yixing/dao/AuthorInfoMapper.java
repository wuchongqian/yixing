package com.weixin.yixing.dao;

import com.weixin.yixing.entity.AuthorInfo;

import java.util.List;

public interface AuthorInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AuthorInfo record);

    int insertSelective(AuthorInfo record);

    AuthorInfo selectByPrimaryKey(Integer id);

    AuthorInfo selectAuthorInfoByAuthorId(String authorUuid);

    List<AuthorInfo> selectAuthorInfoByPhone(String phone);

    int selectCountByActivityId(String activityId);

    int updateByPrimaryKeySelective(AuthorInfo record);

    int updateByPrimaryKey(AuthorInfo record);
}