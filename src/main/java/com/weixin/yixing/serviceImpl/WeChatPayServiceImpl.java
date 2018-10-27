package com.weixin.yixing.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.commons.utils.ResultContent;
import com.weixin.yixing.config.RedisUtil;
import com.weixin.yixing.constants.Constants;
import com.weixin.yixing.dao.*;
import com.weixin.yixing.entity.*;
import com.weixin.yixing.utils.HttpClientUtil;
import com.weixin.yixing.utils.MD5Util;
import com.weixin.yixing.utils.MessageUtils;
import com.weixin.yixing.utils.PayUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class WeChatPayServiceImpl {
    private static Logger logger = LoggerFactory.getLogger(WeChatPayServiceImpl.class);

    @Value("${appid}")
    private String appid;

    @Value("${apikey}")
    private String apikey;

    @Autowired
    private TypeOfGiftMapper typeOfGiftMapper;

    @Autowired
    private WorksInfoMapper worksInfoMapper;

    @Autowired
    private GiftRecordMapper giftRecordMapper;

    @Autowired
    private WeChatUserMapper weChatUserMapper;

    @Autowired
    private RedisUtil redisUtil;
    /**
     * @author
     * @version 创建时间：2017年1月21日 下午4:59:03
     * 小程序端请求的后台action，返回签名后的数据传到前台
     */

    private String detail;//商品详情
    private String attach;//附加数据
    private String time_start;//交易起始时间
    private String time_expire;//交易结束时间

    public ResultContent weChatPay(String openid, HttpServletRequest request, String giftId, String worksId, String token) throws Exception {
        logger.info("开始礼物赠送支付: "+"openid-"+ openid+", giftId-"+ giftId + ", worksId-"+ worksId + ", token-" + token);

        TypeOfGift typeOfGift = typeOfGiftMapper.selectByPrimaryKey(Integer.valueOf(giftId));
        Integer valueOfGift = typeOfGift.getValueOfGift();
        String nameOfGift = typeOfGift.getTypeName();

        String total_fee = String.valueOf(valueOfGift);
        String mch_id = "1515160931";//商户号
        String body = "清禾艺星-" + nameOfGift;
//        body = new String(body.getBytes("UTF-8"));
        String nonce_str = UUID.randomUUID().toString().replace("-", "");//随机字符串
        String today = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String code = PayUtils.createCode(8);
        String out_trade_no = today + code;//商户订单号

        redisUtil.set("worksId", worksId, 300L);
        redisUtil.set("giftId", giftId, 300L);

        String spbill_create_ip = PayUtils.getIpAddr(request);//终端IP  PayUtils.getIpAddr(request)
        String notify_url = "https://www.qingheyixing.com/wxNotify";//通知地址
        String trade_type = "JSAPI";//交易类型

        /**/
        PaymentPo paymentPo = new PaymentPo();
        paymentPo.setAppid(appid);
        paymentPo.setMch_id(mch_id);
        paymentPo.setNonce_str(nonce_str);
//        String newbody = new String(body.getBytes("ISO-8859-1"), "UTF-8");//以utf-8编码放入paymentPo，微信支付要求字符编码统一采用UTF-8字符编码
        paymentPo.setBody(body);
        paymentPo.setOut_trade_no(out_trade_no);
        paymentPo.setTotal_fee(total_fee);
        paymentPo.setSpbill_create_ip(spbill_create_ip);
        paymentPo.setNotify_url(notify_url);
        paymentPo.setTrade_type(trade_type);
        paymentPo.setOpenId(openid);
        // 把请求参数打包成数组
        Map<String, String> sParaTemp = new HashMap<>();
        sParaTemp.put("appid", paymentPo.getAppid());
        sParaTemp.put("body", body);
        sParaTemp.put("mch_id", paymentPo.getMch_id());
        sParaTemp.put("nonce_str", paymentPo.getNonce_str());
        sParaTemp.put("notify_url", paymentPo.getNotify_url());
        sParaTemp.put("openid", paymentPo.getOpenId());
        sParaTemp.put("out_trade_no", paymentPo.getOut_trade_no());
        sParaTemp.put("spbill_create_ip", paymentPo.getSpbill_create_ip());
        sParaTemp.put("total_fee", paymentPo.getTotal_fee());
        sParaTemp.put("trade_type", paymentPo.getTrade_type());

        // 除去数组中的空值和签名参数
        Map sPara = PayUtils.paraFilter(sParaTemp);
        String stringA = PayUtils.createLinkString(sPara); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        String key = "&key=" + apikey; // 商户支付密钥

        //MD5运算生成签名
        String mySign = MD5Util.encryptByMD5(stringA + key).toUpperCase();
        paymentPo.setSign(mySign);

        //打包要发送的xml
        String respXml = MessageUtils.messageToXML(paymentPo);
        // 打印respXml发现，得到的xml中有“__”不对，应该替换成“_”
        respXml = respXml.replace("__", "_");
        String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";//统一下单API接口链接
        String param = "<xml>" + "<appid>" + appid + "</appid>"
                + "<body><![CDATA[" + body + "]]></body>"
                + "<mch_id>" + mch_id + "</mch_id>"
                + "<nonce_str>" + nonce_str + "</nonce_str>"
                + "<notify_url>" + notify_url + "</notify_url>"
                + "<openid>" + openid + "</openid>"
                + "<out_trade_no>" + out_trade_no + "</out_trade_no>"
                + "<spbill_create_ip>" + spbill_create_ip + "</spbill_create_ip>"
                + "<total_fee>" + total_fee + "</total_fee>"
                + "<trade_type>" + trade_type + "</trade_type>"
                + "<sign>" + mySign + "</sign>"
                + "</xml>";
        //String result = SendRequestForUrl.sendRequest(url, param);//发起请求
        String result = HttpClientUtil.httpsRequest(url, "POST", param);
        // 将解析结果存储在HashMap中
        Map<String, String> map = new HashMap();
        InputStream in = new ByteArrayInputStream(result.getBytes());
        // 读取输入流
        SAXReader reader = new SAXReader();
        Document document = reader.read(in);
        // 得到xml根元素
        Element root = document.getRootElement();
        // 得到根元素的所有子节点
        @SuppressWarnings("unchecked")
        List<Element> elementList = root.elements();
        for (Element element : elementList) {
            map.put(element.getName(), element.getText());
        }
        // 返回信息
        String return_code = map.get("return_code");//返回状态码
        String return_msg = map.get("return_msg");//返回信息
//        System.out.println("return_msg:" + return_msg);
        JSONObject jsonObject = new JSONObject();
        if (return_code == "SUCCESS" || return_code.equals(return_code)) {
            // 业务结果
            String prepay_id = map.get("prepay_id");//返回的预付单信息
            String nonceStr = UUID.randomUUID().toString().replace("-", "");
            jsonObject.put("nonceStr", nonceStr);
            jsonObject.put("package", "prepay_id=" + prepay_id);
            Long timeStamp = System.currentTimeMillis() / 1000;
            jsonObject.put("timeStamp", timeStamp + "");
            String stringSignTemp = "appId=" + appid + "&nonceStr=" + nonceStr + "&package=prepay_id=" + prepay_id + "&signType=MD5&timeStamp=" + timeStamp;
            //再次签名
            String paySign = PayUtils.sign(stringSignTemp, key, "utf-8").toUpperCase();
            jsonObject.put("paySign", paySign);
            jsonObject.put("openid", openid);
//            jsonArray.add(jsonObject);
        }
        return new ResultContent(Constants.REQUEST_SUCCESS, map.get("return_msg"), jsonObject);
    }

    /**
     * @Description:微信支付
     * @return
     * @throws Exception
     */
    public void wxNotify(HttpServletRequest request, HttpServletResponse response) throws Exception{

        logger.info("开始微信支付回调");
        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream)request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while((line = br.readLine()) != null){
            sb.append(line);
        }
        br.close();
        //sb为微信返回的xml
        String notityXml = sb.toString();
        String resXml = "";
        System.out.println("接收到的报文：" + notityXml);

        Map map = PayUtils.doXMLParse(notityXml);

        String returnCode = (String) map.get("return_code");
        if("SUCCESS".equals(returnCode)){
            //验证签名是否正确
            Map<String, String> validParams = PayUtils.paraFilter(map);  //回调验签时需要去除sign和空值参数
            String validStr = PayUtils.createLinkString(validParams);//把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
            String sign = PayUtils.sign(validStr, "&key=" + apikey, "utf-8").toUpperCase();//拼装生成服务器端验证的签名
            //根据微信官网的介绍，此处不仅对回调的参数进行验签，还需要对返回的金额与系统订单的金额进行比对等
            if(sign.equals(map.get("sign"))){
                /**此处添加自己的业务逻辑代码start**/
                String fee = (String)map.get("total_fee");
                String outTradeNo = (String)map.get("out_trade_no");
                String openid = (String)map.get("openid");
                System.out.println(openid);
                System.out.println((String)redisUtil.get("giftId"));
                String giftId = (String) (redisUtil.get("giftId"));
                TypeOfGift typeOfGift = typeOfGiftMapper.selectByPrimaryKey(Integer.valueOf(giftId));
                Integer valueOfGift = typeOfGift.getValueOfGift();
                String money = String.valueOf(valueOfGift);
                if (fee.equals(money)){
                    //通知微信服务器已经支付成功
                    resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                            + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                    System.out.println((String)redisUtil.get("worksId"));

                    Boolean result = addGift(String.valueOf(giftId), openid,(String)redisUtil.get("worksId"), outTradeNo);
                }else{
                    resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                            + "<return_msg><![CDATA[金额错误]]></return_msg>" + "</xml> ";
                }
            }
        }else{
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        System.out.println(resXml);
        System.out.println("微信支付回调数据结束");

        BufferedOutputStream out = new BufferedOutputStream(
                response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }

    private Boolean addGift(String giftId, String presenterId, String worksId, String out_trade_no){
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
            return true;
        }else{
            return false;
        }
    }

}
