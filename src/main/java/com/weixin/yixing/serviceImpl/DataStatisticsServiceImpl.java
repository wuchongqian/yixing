package com.weixin.yixing.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.commons.utils.ResultContent;
import com.weixin.yixing.constants.Constants;
import com.weixin.yixing.dao.AuthorInfoMapper;
import com.weixin.yixing.dao.WorksInfoMapper;
import com.weixin.yixing.entity.WorksInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("numOfWorks", numOfWorks);
        jsonObject.put("numOfAuthor", numOfAuthor);
        jsonObject.put("numOfVotes", numOfVotes);
        return new ResultContent(Constants.REQUEST_SUCCESS, Constants.SUCCESS, jsonObject);
    }

    /**
     *
     * @param activityId
     * @param year
     * @param month
     * @param token
     * @return
     */
    /*TODO 逻辑错误*/
    public ResultContent getViewsDistribute(String activityId, Integer year, Integer month, String token) {
        logger.info("开始进行点击量查询");
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);//默认1月为0月
        int day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<JSONObject> list = new ArrayList<>();
        for (int i = day; i >= 0; i--) {
            JSONObject jsonObject = new JSONObject();
            calendar.add(Calendar.DATE, -i);
            String dataStr = sdf.format(calendar.getTime());
            Map<String, Object> map = new HashMap<>();
            map.put("activityId",activityId);
            map.put("currentDate",dataStr);

            List<WorksInfo> worksInfoList = worksInfoMapper.selectClicksDataByDate(map);//TODO 计算clickNum
            jsonObject.put("clickNum", 0);
            jsonObject.put("date", dataStr);
            list.add(jsonObject);
        }
        JSONObject object = new JSONObject();
        if(list.size() > 0){
            object.put("viewList", list);
            return new ResultContent(Constants.REQUEST_SUCCESS, Constants.SUCCESS, object);
        }else {
            object.put("viewList", Collections.EMPTY_LIST);
            return new ResultContent(Constants.REQUEST_SUCCESS, Constants.IS_EMPTY, object);
        }
    }


}
