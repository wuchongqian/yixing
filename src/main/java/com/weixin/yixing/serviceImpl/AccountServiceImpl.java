//package com.weixin.yixing.serviceImpl;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import org.apache.commons.lang.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.ui.Model;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//public class AccountServiceImpl {
//    private static Logger logger= LoggerFactory.getLogger(AccountServiceImpl.class);
//
//    @Autowired
//    private RedisTemplate redisTemplate;
//
//    public Map<String,Object> doLogin(Model model, String code, String rawData, String signature, String encrypteData, String iv){
//        logger.info( "Start get SessionKey" );
//
//        Map<String,Object> map = new HashMap<String, Object>(  );
//        System.out.println("用户非敏感信息"+rawData);
//
//        JSONObject rawDataJson = JSON.parseObject( rawData );
//
//        System.out.println("签名"+signature);
//        JSONObject SessionKeyOpenId = getSessionKeyOrOpenId( code );
//        System.out.println("post请求获取的SessionAndopenId="+SessionKeyOpenId);
//
//        String openid = SessionKeyOpenId.getString("openid" );
//
//        String sessionKey = SessionKeyOpenId.getString( "session_key" );
//
//        System.out.println("openid="+openid+",session_key="+sessionKey);
//
//        User user = userService.findByOpenid( openid );
//        //uuid生成唯一key
//        String skey = UUID.randomUUID().toString();
//        if(user==null){
//            //入库
//            String nickName = rawDataJson.getString( "nickName" );
//            String avatarUrl = rawDataJson.getString( "avatarUrl" );
//            String gender  = rawDataJson.getString( "gender" );
//            String city = rawDataJson.getString( "city" );
//            String country = rawDataJson.getString( "country" );
//            String province = rawDataJson.getString( "province" );
//
//
//            user = new User();
//            user.setUid( openid );
//            user.setCreateTime( new Date(  ) );
//            user.setSessionkey( sessionKey );
//            user.setUbalance( 0 );
//            user.setSkey( skey );
//            user.setUaddress( country+" "+province+" "+city );
//            user.setUavatar( avatarUrl );
//            user.setUgender( Integer.parseInt( gender ) );
//            user.setUname( nickName );
//            user.setUpdateTime( new Date(  ) );
//
//            userService.insert( user );
//        }else {
//            //已存在
//            log.info( "用户openid已存在,不需要插入" );
//        }
//        //根据openid查询skey是否存在
//        String skey_redis = (String) redisTemplate.opsForValue().get( openid );
//        if(StringUtils.isNotBlank( skey_redis )){
//            //存在 删除 skey 重新生成skey 将skey返回
//            redisTemplate.delete( skey_redis );
//
//        }
//        //  缓存一份新的
//        JSONObject sessionObj = new JSONObject(  );
//        sessionObj.put( "openId",openid );
//        sessionObj.put( "sessionKey",sessionKey );
//        redisTemplate.opsForValue().set( skey,sessionObj.toJSONString() );
//        redisTemplate.opsForValue().set( openid,skey );
//
//        //把新的sessionKey和oppenid返回给小程序
//        map.put( "skey",skey );
//
//        map.put( "result","0" );
//
//
//
//        JSONObject userInfo = getUserInfo( encrypteData, sessionKey, iv );
//        System.out.println("根据解密算法获取的userInfo="+userInfo);
//        userInfo.put( "balance",user.getUbalance() );
//        map.put( "userInfo",userInfo );
//
//        return map;
//    }
//
//    public static JSONObject getSessionKeyOrOpenId(String code){
//        //微信端登录code
//        String wxCode = code;
//        String requestUrl = "https://api.weixin.qq.com/sns/jscode2session";
//        Map<String,String> requestUrlParam = new HashMap<String, String>(  );
//        requestUrlParam.put( "appid","你的小程序appId" );//小程序appId
//        requestUrlParam.put( "secret","你的小程序appSecret" );
//        requestUrlParam.put( "js_code",wxCode );//小程序端返回的code
//        requestUrlParam.put( "grant_type","authorization_code" );//默认参数
//
//        //发送post请求读取调用微信接口获取openid用户唯一标识
//        JSONObject jsonObject = JSON.parseObject( UrlUtil.sendPost( requestUrl,requestUrlParam ));
//        return jsonObject;
//    }
//
//}
