package com.weixin.yixing.dao;

import com.weixin.yixing.entity.UserVotesRecord;

import java.util.List;
import java.util.Map;

public interface UserVotesRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserVotesRecord record);

    int insertSelective(UserVotesRecord record);

    UserVotesRecord selectByPrimaryKey(Integer id);

    UserVotesRecord selectByWorksId(Map<String, Object> map);

    List<UserVotesRecord> selectByDate(Map<String, Object> map);

    int updateByPrimaryKeySelective(UserVotesRecord record);

    int updateByPrimaryKey(UserVotesRecord record);
}