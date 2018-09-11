package com.weixin.yixing.dao;

import com.weixin.yixing.entity.File;

import java.util.List;

public interface FileMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(File record);

    int insertSelective(File record);

    File selectByPrimaryKey(Integer id);

    List<File> selectByFileSelective(File file);

    int updateByPrimaryKeySelective(File record);

    int updateByPrimaryKey(File record);
}