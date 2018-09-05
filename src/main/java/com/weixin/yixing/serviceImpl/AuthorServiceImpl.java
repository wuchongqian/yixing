package com.weixin.yixing.serviceImpl;

import com.commons.utils.ResultContent;
import com.weixin.yixing.constants.Constants;
import com.weixin.yixing.dao.AuthorInfoMapper;
import com.weixin.yixing.entity.AuthorInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorServiceImpl {
    private static Logger logger= LoggerFactory.getLogger(AuthorServiceImpl.class);

    @Autowired
    private AuthorInfoMapper authorInfoMapper;

    /**
     * 查询作者详情
     * @param authorId
     * @param token
     * @return
     */
    public ResultContent getAuthorInfo(String authorId, String token){
        logger.info("开始查询作者详情");
        AuthorInfo authorInfo = authorInfoMapper.selectAuthorInfoByAuthorId(authorId);
        return new ResultContent(Constants.REQUEST_SUCCESS, Constants.SUCCESS, authorInfo);
    }

    /**
     * 增加喜欢作者
     * @param authorId
     * @param token
     * @return
     */
    public ResultContent addLikeOfAuthor(String authorId, String token){
        return null;
    }

}
