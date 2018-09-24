package com.weixin.yixing.dao;

import com.weixin.yixing.entity.WorksInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface WorksInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WorksInfo record);

    int insertSelective(WorksInfo record);

    WorksInfo selectByPrimaryKey(Integer id);

    List<WorksInfo> selectByKeywordOrderByTime(Map<String, Object> map);

    List<WorksInfo> selectByKeywordOrderByTimeForPC(Map<String, Object> map);

    List<WorksInfo> selectByKeywordOrderByVotes(Map<String, Object> map);

    List<WorksInfo> selectByKeywordOrderByVotesForPC(Map<String, Object> map);

    List<WorksInfo> selectByUnReviewedWorksForPC();

    int selectNumOfUnReviewedWorks();

    int selectAllWorksStatisticData(String activityId);

    WorksInfo selectWorksInfoByWorksId(String worksUuid);

//    WorksInfo selectUnReviewedWorksInfoByWorksId(String worksUuid);

    List<WorksInfo> selectWorksInfoByActivityId(String activityId);

    List<WorksInfo> selectWorksLeaderBoardByActivityId(String activityId);

    List<WorksInfo> selectWorksInfoByAuthorId(Map<String, Object>map);

    int getSumOfVotes(String activityId);

    int getSumOfVotesByAuthorId(String authorId);

    List<WorksInfo> selectClicksDataByDate(Map<String, Object>map);

    int updateByPrimaryKeySelective(WorksInfo record);

    int updateByPrimaryKey(WorksInfo record);
}