package com.weixin.yixing.dao;

import com.weixin.yixing.entity.UserAuthorRecord;

import java.util.Map;

public interface UserAuthorRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserAuthorRecord record);

    int insertSelective(UserAuthorRecord record);

    UserAuthorRecord selectByPrimaryKey(Integer id);

    UserAuthorRecord selectByWorksId(Map<String, Object>map);

    int updateByPrimaryKeySelective(UserAuthorRecord record);

    int updateByPrimaryKey(UserAuthorRecord record);
}