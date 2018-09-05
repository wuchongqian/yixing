package com.weixin.yixing.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.commons.utils.ResultContent;
import com.commons.utils.ResultPage;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.weixin.yixing.constants.Constants;
import com.weixin.yixing.dao.AuthorInfoMapper;
import com.weixin.yixing.dao.WorksInfoMapper;
import com.weixin.yixing.entity.WorksInfo;
import com.weixin.yixing.entity.WorksList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorksServiceImpl {
    private static Logger logger= LoggerFactory.getLogger(WorksServiceImpl.class);

    @Autowired
    private WorksInfoMapper worksInfoMapper;

    @Autowired
    private AuthorInfoMapper authorInfoMapper;

    /**
     * 查询作品列表根据时间排序
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @param token
     * @return
     */
    public ResultPage getWorksListByCreateTime(String keyword, String pageNum, String pageSize, String  token){
        logger.info("开始查询作品列表根据时间排序");

        Page<WorksList> page= PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize)).doSelectPage(()->
                        worksInfoMapper.selectByKeywordOrderByTime(keyword));
        List<WorksList> list = page.getResult();
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("worksList", list);

        return new ResultPage(Constants.REQUEST_SUCCESS, Constants.SUCCESS,list,
                page.getPageSize(), page.getPages(), page.getPageNum(), Integer.valueOf((int) page.getTotal()));
    }

    /**
     *查询作品列表根据票数排序
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @param token
     * @return
     */
    public ResultPage getWorksListByVotes(String keyword, String pageNum, String pageSize, String  token){
        logger.info("开始查询作品列表根据票数排序");

        Page<WorksList> page= PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize)).doSelectPage(()->
                worksInfoMapper.selectByKeywordOrderByVotes(keyword));
        List<WorksList> list = page.getResult();
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("worksList", list);

        return new ResultPage(Constants.REQUEST_SUCCESS, Constants.SUCCESS,list,
                page.getPageSize(), page.getPages(), page.getPageNum(), Integer.valueOf((int) page.getTotal()));
    }

    /**
     * 查询报名人数
     * @param activityId
     * @param token
     * @return
     */
    public ResultContent getNumOfRegistration(String activityId, String token){
        logger.info("查询报名人数");
        int result = authorInfoMapper.selectCountByActivityId(activityId);
        return new ResultContent(Constants.REQUEST_SUCCESS, Constants.SUCCESS, result);
    }

    /**
     * 作品投票
     * @param worksId
     * @param token
     * @return
     */
    public ResultContent addNumOfVotesOnce(String worksId, String token){
        return null;
    }

    /**
     *查询作品详情
     * @param worksId
     * @param token
     * @return
     */
    public ResultContent getWorksInfo(String worksId, String token){
        logger.info("开始查询作品详情");
        WorksInfo worksInfo = worksInfoMapper.selectWorksInfoByWorksId(worksId);

        return new ResultContent(Constants.REQUEST_SUCCESS, Constants.SUCCESS, worksInfo);
    }

    /**
     *新增作品点击量
     * @param worksId
     * @param token
     * @return
     */
    public ResultContent addClicksOfWorksOnce(String worksId, String token){
        return null;
    }
}
