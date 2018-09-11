package com.weixin.yixing.dao;

import com.weixin.yixing.entity.WorksInfo;
import com.weixin.yixing.entity.WorksList;

import java.util.List;

public interface WorksInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WorksInfo record);

    int insertSelective(WorksInfo record);

    WorksInfo selectByPrimaryKey(Integer id);

    List<WorksList> selectByKeywordOrderByTime(String keyword);

    List<WorksList> selectByKeywordOrderByVotes(String keyword);

    WorksInfo selectWorksInfoByWorksId(String worksUuid);

    List<WorksInfo> selectWorksInfoByActivityId(String activityId);

    int updateByPrimaryKeySelective(WorksInfo record);

    int updateByPrimaryKey(WorksInfo record);
}