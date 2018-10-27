package com.weixin.yixing.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.commons.utils.ResultContent;
import com.commons.utils.ResultPage;
import com.commons.utils.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.weixin.yixing.constants.Constants;
import com.weixin.yixing.dao.GiftRecordMapper;
import com.weixin.yixing.dao.TypeOfGiftMapper;
import com.weixin.yixing.dao.WeChatUserMapper;
import com.weixin.yixing.dao.WorksInfoMapper;
import com.weixin.yixing.entity.GiftRecord;
import com.weixin.yixing.entity.TypeOfGift;
import com.weixin.yixing.entity.WeChatUser;
import com.weixin.yixing.entity.WorksInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GiftServiceImpl {
    private static Logger logger= LoggerFactory.getLogger(GiftServiceImpl.class);

    @Autowired
    private GiftRecordMapper giftRecordMapper;

    @Autowired
    private TypeOfGiftMapper typeOfGiftMapper;

    @Autowired
    private WorksInfoMapper worksInfoMapper;

    @Autowired
    private WeChatUserMapper weChatUserMapper;

    /**
     * 赠送礼物
     * @param giftId
     * @param presenterId
     * @param out_trade_no
     * @param worksId
     * @param token
     * @return
     */
    public ResultContent addGift(String giftId, String presenterId, String worksId, String out_trade_no,String token){
        logger.info("开始赠送礼物");
        GiftRecord giftRecord = new GiftRecord();
        giftRecord.setGiftId(Integer.valueOf(giftId));
        giftRecord.setPresenterId(presenterId);
        WeChatUser weChatUser = weChatUserMapper.findByOpenid(presenterId);
        giftRecord.setPresenterName(weChatUser.getNickName());
        giftRecord.setWorkId(worksId);
        giftRecord.setCreateTime(new Date());
        giftRecord.setModifyTime(new Date());
        giftRecord.setOutTradeNo(out_trade_no);

        //更新作品票数
        TypeOfGift typeOfGift = typeOfGiftMapper.selectByPrimaryKey(Integer.valueOf(giftId));
        Integer voteOfGift = typeOfGift.getVoteOfGift();
        WorksInfo worksInfo = worksInfoMapper.selectWorksInfoByWorksId(worksId);
        Integer sum = voteOfGift + worksInfo.getNumberOfVotes();
        WorksInfo newWorksInfo = new WorksInfo();
        newWorksInfo.setWorksUuid(worksId);
        newWorksInfo.setNumberOfVotes(sum);
        int workResult = worksInfoMapper.updateByPrimaryKeySelective(newWorksInfo);
        int result = giftRecordMapper.insert(giftRecord);
        if (result>0 && workResult>0){
            return new ResultContent(Constants.REQUEST_SUCCESS, Constants.SUCCESS,new JSONObject());
        }else{
            return new ResultContent(Constants.REQUEST_FAILED,"赠送礼物失败",new JSONObject());
        }
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
        logger.info("开始查询礼物赠送情况");
        if(StringUtils.isEmpty(worksId)){
            return new ResultPage(ResultContent.CODE_FAILED, "worksId参数不能为空", "{}",
                    null, null, null, null);
        }
        Page<GiftRecord> page=PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize)).doSelectPage(()->giftRecordMapper.selectByWorksId(worksId));
        List<GiftRecord> giftRecords = page.getResult();
        List<Map<String, Object>> giftTrackList = new ArrayList<>();

        if(giftRecords.size()> 0){
            for (GiftRecord giftRecord: giftRecords){
                Map<String, Object> map = new HashMap();
                map.put("presenterName",giftRecord.getPresenterName());
                //获取赠送人头像url
                WeChatUser weChatUser = weChatUserMapper.findByOpenid(giftRecord.getPresenterId());
                String url = weChatUser.getAvatarurl();
                map.put("avatarUrl", url);
                //获取礼物信息
                TypeOfGift typeOfGift =typeOfGiftMapper.selectByPrimaryKey(giftRecord.getGiftId());
                map.put("giftName",typeOfGift.getTypeName());
                map.put("voteOfGift",typeOfGift.getVoteOfGift());
                giftTrackList.add(map);
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("giftTrackList",giftTrackList);
        List<GiftRecord>  giftRecordList = giftRecordMapper.selectByWorksId(worksId);
        Integer sum = 0;
        for(GiftRecord giftRecord: giftRecordList){
            TypeOfGift typeOfGift =typeOfGiftMapper.selectByPrimaryKey(giftRecord.getGiftId());
            sum = sum + typeOfGift.getVoteOfGift();
        }
        jsonObject.put("vote",sum);

        return new ResultPage(ResultContent.CODE_SUCCESS, Constants.SUCCESS, jsonObject,
                page.getPageSize(), page.getPages(), page.getPageNum(), (int) page.getTotal());
    }


}
