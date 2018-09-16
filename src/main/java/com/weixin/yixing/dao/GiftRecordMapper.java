package com.weixin.yixing.dao;

import com.weixin.yixing.entity.GiftRecord;

public interface GiftRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GiftRecord record);

    int insertSelective(GiftRecord record);

    GiftRecord selectByPrimaryKey(Integer id);
    GiftRecord selectByWorksId(String worksId);

    int updateByPrimaryKeySelective(GiftRecord record);

    int updateByPrimaryKey(GiftRecord record);
}