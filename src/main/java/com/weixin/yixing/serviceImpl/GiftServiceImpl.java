package com.weixin.yixing.serviceImpl;

import com.commons.utils.ResultContent;
import com.commons.utils.ResultPage;
import com.weixin.yixing.dao.GiftRecordMapper;
import com.weixin.yixing.dao.TypeOfGiftMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GiftServiceImpl {
    private static Logger logger= LoggerFactory.getLogger(GiftServiceImpl.class);

    @Autowired
    private GiftRecordMapper giftRecordMapper;

    @Autowired
    private TypeOfGiftMapper typeOfGiftMapper;

    /**
     * 赠送礼物
     * @param giftId
     * @param presenterId
     * @param presenterName
     * @param worksId
     * @param token
     * @return
     */
    public ResultContent addGift(String giftId, String presenterId, String presenterName, String worksId, String token){

        return null;
    }

    /**
     * 查询礼物赠送情况
     * @param worksId
     * @param pageNum
     * @param pageSize
     * @param token
     * @return
     */
    public ResultPage  getGiftTrack(String worksId, String pageNum, String pageSize, String  token){

        return null;
    }


}
