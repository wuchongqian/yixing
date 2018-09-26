package com.weixin.yixing.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.commons.utils.ResultContent;
import com.weixin.yixing.config.RedisUtil;
import com.weixin.yixing.constants.Constants;
import com.weixin.yixing.dao.AccountMapper;
import com.weixin.yixing.dao.ActivityInfoMapper;
import com.weixin.yixing.dao.WeChatUserMapper;
import com.weixin.yixing.entity.Account;
import com.weixin.yixing.entity.ActivityInfo;
import com.weixin.yixing.entity.WeChatUser;
import com.weixin.yixing.exception.CoreException;
import com.weixin.yixing.utils.HttpClientUtil;
import com.weixin.yixing.utils.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AccountServiceImpl {
    private static Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private WeChatUserMapper weChatUserMapper;

    @Autowired
    private ActivityInfoMapper activityInfoMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Value("${appid}")
    private String appid;

    @Value("${secret}")
    private String secret;

    private static final Long EXPIRES = 43200L;//半天

    /**
     * PC端登录
     * @param username
     * @param password
     * @return
     */
    public ResultContent pcLogin(String username, String password){
        logger.info("开始PC端登录");
        String md5Pwd = "";
        try {
            md5Pwd = MD5Util.getGeneral32BitMD5(password);
        }catch (Exception e){
            e.printStackTrace();
        }

        ResultContent res = accountVerification(username, md5Pwd);
        JSONObject jsonObject = new JSONObject();
        if (res.getCode() != 0){
            return new ResultContent(Constants.REQUEST_FAILED, "用户或密码错误", jsonObject);
        }else{
            return getToken(username);
        }
    }

    public ResultContent addAccount(String username, String password){
        Account account = new Account();
        List<Account> res = accountMapper.selectByAccount(username);
        if(null!=res && res.size()>0){
            throw CoreException.of(CoreException.ACCOUNT_EXIST_ALREADY);
        }
        String accountUuid = UUID.randomUUID().toString().replace("-","");
        account.setAccount(username);
        account.setAccountId(accountUuid);
        account.setCreateTime(new Date());
        account.setModifyTime(new Date());
        String md5Pwd = "";
        try {
            md5Pwd = MD5Util.getGeneral32BitMD5(password);
        }catch (Exception e){
            e.printStackTrace();
        }
        account.setPassword(md5Pwd);

        int i = accountMapper.insert(account);
        if (i>0){
            return new ResultContent(Constants.REQUEST_SUCCESS, Constants.SUCCESS, username);
        }else{
            return new ResultContent(Constants.REQUEST_FAILED, Constants.FAILED, "");
        }
    }

    private ResultContent getToken(String username){
        String token = UUID.randomUUID().toString();
        redisUtil.set(token, username,EXPIRES);
        return new ResultContent(Constants.REQUEST_SUCCESS, Constants.SUCCESS, token);
    }

    private ResultContent accountVerification(String account, String password){

        List<Account> res= accountMapper.selectByAccount(account);
        if(null!=res && res.size()>0){
            if(password.equals(res.get(0).getPassword())){
                return new ResultContent(Constants.REQUEST_SUCCESS, Constants.SUCCESS,"{}");
            } else {
                throw CoreException.of(CoreException.ACCOUNT_PASSOWRD_ERROR);
            }
        } else {
            throw CoreException.of(CoreException.ACCOUNT_NOT_EXIST);
        }
    }

    public ResultContent weChatLogin(String code) {
        logger.info("开始小程序登录");
        JSONObject response = getSessionKeyOrOpenId(code);
        if(response.getString("errcode") != null){
            return new ResultContent(Constants.REQUEST_FAILED, "code错误或过期，code：" +
                    response.getString("errcode") + ", errmsg:" + response.getString("errmsg"), new JSONObject());
        }
        String wxOpenId = response.getString("openid");
        String wxSessionKey = response.getString("session_key");
        String userToken = UUID.randomUUID().toString();
        WeChatUser user = weChatUserMapper.findByOpenid(wxOpenId);
        if (null == user) {
            WeChatUser weChatUser = new WeChatUser();
            weChatUser.setOpenId(wxOpenId);
            weChatUser.setSessionKey(wxSessionKey);
            weChatUser.setSkey(userToken);
            weChatUser.setCreateTime(new Date());
            weChatUserMapper.insertSelective(weChatUser);
        } else {
            logger.info("用户openid已存在,不需要插入");
        }
        Long expires = response.getLong("expires");

        JSONObject sessionObj = new JSONObject();
        sessionObj.put("openId", wxOpenId);
        sessionObj.put("sessionKey", wxSessionKey);
        sessionObj.put("token", userToken);
//        ActivityInfo activityInfo = activityInfoMapper.selectNewActivityInfo();
//        sessionObj.put("activityId", activityInfo.getActivityId());
        redisUtil.set(userToken, sessionObj.toJSONString(), expires);
        return new ResultContent(Constants.REQUEST_SUCCESS, Constants.SUCCESS, sessionObj);
    }

    /**
     * 获取最新活动Id
     * @return
     */
    public ResultContent getActivityId(){
        JSONObject jsonObject = new JSONObject();
        ActivityInfo activityInfo = activityInfoMapper.selectNewActivityInfo();
        if(null == activityInfo){
            return new ResultContent(Constants.REQUEST_FAILED, "暂无活动信息", jsonObject);
        }
        jsonObject.put("activityId", activityInfo.getActivityId());
        jsonObject.put("activityName", activityInfo.getActivityName());
        return new ResultContent(Constants.REQUEST_SUCCESS, Constants.SUCCESS, jsonObject);
    }

    /**
     * 更新微信账号信息
     * @param openid
     * @param rawData
     * @return
     */
    public ResultContent updateWeChatUserInfo(String openid, String rawData) {
        logger.info("Start get SessionKey");
        ;
        JSONObject rawDataJson = JSON.parseObject(rawData);
        //入库
        String nickName = rawDataJson.getString("nickName");
        String avatarUrl = rawDataJson.getString("avatarUrl");
        String gender = rawDataJson.getString("gender");
        String city = rawDataJson.getString("city");
        String country = rawDataJson.getString("country");
        String province = rawDataJson.getString("province");
        String weChatAccount = rawDataJson.getString("username");

        WeChatUser user = new WeChatUser();
        user.setOpenId(openid);
        user.setCountry(country);
        user.setProvince(province);
        user.setCity(city);
        user.setAvatarurl(avatarUrl);
        user.setGender(gender);
        user.setNickName(nickName);
        user.setWechatAccount(weChatAccount);
        user.setModifyTime(new Date());
        int result= weChatUserMapper.updateByPrimaryKeySelective(user);

//        //根据openid查询skey是否存在
//        String skey_redis = (String)redisUtil.get(openid);
//
//        if(StringUtils.isNotBlank( skey_redis )){
//            //存在 删除 skey 重新生成skey 将skey返回
//            redisUtil.remove( skey_redis );
//
//        }
        //  缓存一份新的
//        JSONObject sessionObj = new JSONObject(  );
//        sessionObj.put( "openId",openid );
//        sessionObj.put( "sessionKey",sessionKey );
//        redisUtil.set(skey, sessionObj.toJSONString());
//        redisUtil.set(openid,skey);
//
//        //把新的sessionKey和oppenid返回给小程序
//        map.put( "skey",skey );
//        map.put( "result","0" );
//        JSONObject userInfo = getUserInfo( encrypteData, sessionKey, iv );
//        System.out.println("根据解密算法获取的userInfo="+userInfo);
//        userInfo.put( "balance",user.getUbalance() );
//        map.put( "userInfo",userInfo );

        if (result > 0){
            return new ResultContent(Constants.REQUEST_SUCCESS, Constants.SUCCESS, new JSONObject());
        }else{
            return new ResultContent(Constants.REQUEST_FAILED, Constants.FAILED, new JSONObject());
        }
    }

    private JSONObject getSessionKeyOrOpenId(String code) {
        //微信端登录code
        String wxCode = code;
        String requestUrl = "https://api.weixin.qq.com/sns/jscode2session";
        Map<String, String> requestUrlParam = new HashMap<String, String>();
        requestUrlParam.put("appid", appid);//小程序appId
        requestUrlParam.put("secret", secret);
        requestUrlParam.put("js_code", wxCode);//小程序端返回的code
        requestUrlParam.put("grant_type", "authorization_code");//默认参数

        //发送post请求读取调用微信接口获取openid用户唯一标识
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = JSON.parseObject(HttpClientUtil.post(requestUrl, requestUrlParam));
        } catch (Exception e) {
            e.printStackTrace();
        }
        jsonObject.put("expires", EXPIRES);
        return jsonObject;
    }
}
