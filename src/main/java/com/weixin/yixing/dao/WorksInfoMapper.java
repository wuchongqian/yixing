package com.weixin.yixing.dao;

import com.weixin.yixing.entity.WorksInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WorksInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WorksInfo record);

    int insertSelective(WorksInfo record);

    WorksInfo selectByPrimaryKey(Integer id);

    List<WorksInfo> selectByKeywordOrderByTime(@Param("keyword") String keyword, @Param("activityId") String activityId);

    List<WorksInfo> selectByKeywordOrderByTimeForPC(@Param("keyword") String keyword, @Param("activityId") String activityId);

    List<WorksInfo> selectByKeywordOrderByVotes(@Param("keyword") String keyword, @Param("activityId") String activityId);

    List<WorksInfo> selectByKeywordOrderByVotesForPC(@Param("keyword") String keyword, @Param("activityId") String activityId);

    List<WorksInfo> selectByUnReviewedWorksForPC();

    int selectNumOfUnReviewedWorks();

    WorksInfo selectWorksInfoByWorksId(String worksUuid);

//    WorksInfo selectUnReviewedWorksInfoByWorksId(String worksUuid);

    List<WorksInfo> selectWorksInfoByActivityId(String activityId);

    List<WorksInfo> selectWorksLeaderBoardByActivityId(String activityId);

    int selectRankingByWorksId(String worksId);//TODO 待验证

    int updateByPrimaryKeySelective(WorksInfo record);

    int updateByPrimaryKey(WorksInfo record);
}