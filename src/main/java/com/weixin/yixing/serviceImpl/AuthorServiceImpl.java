package com.weixin.yixing.serviceImpl;

import com.commons.utils.ResultContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AuthorServiceImpl {
    private static Logger logger= LoggerFactory.getLogger(AuthorServiceImpl.class);

    public ResultContent getAuthorInfo(String authorId, String token){
        return null;
    }

    public ResultContent addLikeOfAuthor(String authorId, String token){
        return null;
    }

}
