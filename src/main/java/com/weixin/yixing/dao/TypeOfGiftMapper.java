package com.weixin.yixing.dao;

import com.weixin.yixing.entity.TypeOfGift;

public interface TypeOfGiftMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TypeOfGift record);

    int insertSelective(TypeOfGift record);

    TypeOfGift selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TypeOfGift record);

    int updateByPrimaryKey(TypeOfGift record);
}