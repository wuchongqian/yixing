package com.weixin.yixing.dao;

import com.weixin.yixing.entity.ActivityInfo;

import java.util.List;

public interface ActivityInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ActivityInfo record);

    int insertSelective(ActivityInfo record);

    ActivityInfo selectByPrimaryKey(Integer id);

    ActivityInfo selectActivityInfoByActivityId(String activityId);

    List<ActivityInfo> selectAllActivityInfo();

    int updateByPrimaryKeySelective(ActivityInfo record);

    int updateByPrimaryKey(ActivityInfo record);
}