package com.weixin.yixing.dao;

import com.weixin.yixing.entity.AuthorWorks;

public interface AuthorWorksMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AuthorWorks record);

    int insertSelective(AuthorWorks record);

    AuthorWorks selectByPrimaryKey(Integer id);

    AuthorWorks selectByAuthorId(String authorId);

    AuthorWorks selectByWorksId(String worksId);

    int updateByPrimaryKeySelective(AuthorWorks record);

    int updateByPrimaryKey(AuthorWorks record);
}