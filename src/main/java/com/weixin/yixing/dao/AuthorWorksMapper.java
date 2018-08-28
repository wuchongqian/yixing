package com.weixin.yixing.dao;

import com.weixin.yixing.entity.AuthorWorks;

public interface AuthorWorksMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AuthorWorks record);

    int insertSelective(AuthorWorks record);

    AuthorWorks selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AuthorWorks record);

    int updateByPrimaryKey(AuthorWorks record);
}