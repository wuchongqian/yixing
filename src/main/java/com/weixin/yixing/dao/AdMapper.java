package com.weixin.yixing.dao;

import com.weixin.yixing.entity.Ad;

import java.util.Map;

public interface AdMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Ad record);

    int insertSelective(Ad record);

    Ad selectByPrimaryKey(Integer id);

    Ad selectByOpenIdAndWorksId(Map<String, Object> map);

    int selectNumOfClicks();

    int updateByPrimaryKeySelective(Ad record);

    int updateByPrimaryKey(Ad record);
}