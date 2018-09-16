package com.weixin.yixing.dao;

import com.weixin.yixing.entity.GiftRecord;

import java.util.List;

public interface GiftRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GiftRecord record);

    int insertSelective(GiftRecord record);

    GiftRecord selectByPrimaryKey(Integer id);

    List<GiftRecord> selectByWorksId(String worksId);

    int updateByPrimaryKeySelective(GiftRecord record);

    int updateByPrimaryKey(GiftRecord record);
}