package com.weixin.yixing.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.commons.utils.ResultContent;
import com.weixin.yixing.config.RedisUtil;
import com.weixin.yixing.constants.Constants;
import com.weixin.yixing.dao.AdMapper;
import com.weixin.yixing.dao.AuthorInfoMapper;
import com.weixin.yixing.dao.WorksInfoMapper;
import com.weixin.yixing.utils.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DataStatisticsServiceImpl {
    private static Logger logger= LoggerFactory.getLogger(DataStatisticsServiceImpl.class);

    @Autowired
    private WorksInfoMapper worksInfoMapper;

    @Autowired
    private AuthorInfoMapper authorInfoMapper;

    @Autowired
    private AdMapper adMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Value("${appid}")
    private String appid;

    @Value("${secret}")
    private String secret;

    /**
     * 数据统计查询
     * @param activityId
     * @param token
     * @return
     */
    public ResultContent getStatisticsDataInfo(String activityId, String token){
        logger.info("开始进行数据统计查询");

        int numOfWorks = worksInfoMapper.selectAllWorksStatisticData(activityId);//统计作品数

        int numOfAuthor = authorInfoMapper.selectCountByActivityId(activityId);//统计报名人数

        int numOfVotes = worksInfoMapper.getSumOfVotes(activityId);//查询总票数

        int numOfAdClicks = adMapper.selectNumOfClicks();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("numOfWorks", numOfWorks);
        jsonObject.put("numOfAuthor", numOfAuthor);
        jsonObject.put("numOfVotes", numOfVotes);
        jsonObject.put("numOfAdClicks", numOfAdClicks);
        return new ResultContent(Constants.REQUEST_SUCCESS, Constants.SUCCESS, jsonObject);
    }

    /**
     *查询浏览量
     * @param year
     * @param month
     * @param token
     * @return
     */
    public ResultContent getViewsDistribute(Integer year, Integer month, String token) {
        logger.info("开始进行点击量查询");
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String endDate = "";
        String beginDate = "";
        int c = calendar.get(Calendar.MONTH) + 1;
        if (c == month){
//            calendar.add(Calendar.DATE, -1);
//            endDate = sdf.format(calendar.getTime());
//            calendar.add(Calendar.MONTH, 0);
//            calendar.set(Calendar.DAY_OF_MONTH,1);
//            beginDate = sdf.format(calendar.getTime());
            return new ResultContent(Constants.REQUEST_SUCCESS, "微信当前仅支持整月查看数据", "");
        }else{
            calendar.clear();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month - 1);//默认1月为0月
            int day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            calendar.set(Calendar.DATE,day);
            endDate = sdf.format(calendar.getTime());
            calendar.add(Calendar.DATE,-day+1);
            beginDate = sdf.format(calendar.getTime());
        }


        //获取微信accessToken
        String weChatToken = "";
        if (redisUtil.exists("weChatToken")){
            weChatToken = (String) redisUtil.get("weChatToken");
        }else{
            JSONObject tokenObject = getWeChatToken();
            weChatToken = tokenObject.getString("access_token");
        }

        StringBuilder sb = new StringBuilder();
        String url = "https://api.weixin.qq.com/datacube/getweanalysisappidmonthlyvisittrend?access_token=";
        sb.append(url).append(weChatToken);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("begin_date", beginDate);
        jsonObject.put("end_date", endDate);
        JSONObject object = new JSONObject();
        try{
            object = JSON.parseObject(HttpClientUtil.jsonPost(sb.toString(), jsonObject));
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResultContent(Constants.REQUEST_SUCCESS, Constants.SUCCESS, object);
    }

    private JSONObject getWeChatToken(){
        String url = "https://api.weixin.qq.com/cgi-bin/token";
        Map<String, String> params = new HashMap<>();
        params.put("grant_type", "client_credential");
        params.put("appid", appid);
        params.put("secret", secret);
        JSONObject object = new JSONObject();
        try{
            object = JSON.parseObject(HttpClientUtil.doGet(url, params));
        }catch (Exception e){
            e.printStackTrace();
        }
        redisUtil.set("weChatToken", object.getString("access_token"), object.getLong("expires_in"));
        return object;
    }

}
