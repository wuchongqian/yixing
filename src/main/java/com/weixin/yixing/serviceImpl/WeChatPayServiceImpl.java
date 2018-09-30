package com.weixin.yixing.serviceImpl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.weixin.yixing.entity.PaymentPo;
import com.weixin.yixing.utils.MessageUtils;
import com.weixin.yixing.utils.PayUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class WeChatPayServiceImpl {
    private static Logger logger = LoggerFactory.getLogger(WeChatPayServiceImpl.class);

    @Value("${appid}")
    private String appid;

    @Value("${secret}")
    private String secret;

    /**
     * @author
     * @version 创建时间：2017年1月21日 下午4:59:03
     * 小程序端请求的后台action，返回签名后的数据传到前台
     */

    private String total_fee;//总金额
    private String body;//商品描述
    private String detail;//商品详情
    private String attach;//附加数据
    private String time_start;//交易起始时间
    private String time_expire;//交易结束时间
    private JSONArray jsonArray = new JSONArray();

    public Map pay(String openid) throws Exception {
        body = new String(body.getBytes("UTF-8"), "ISO-8859-1");
        String mch_id = "替换为自己的商户号";//商户号
        String nonce_str = UUID.randomUUID().toString().replace("-","");//随机字符串
        String today = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String code = PayUtils.createCode(8);
        String out_trade_no = mch_id + today + code;//商户订单号
        String spbill_create_ip = "替换为自己的终端IP";//终端IP
        String notify_url = "http://www.weixin.qq.com/wxpay/pay.php";//通知地址
        String trade_type = "JSAPI";//交易类型

        /**/
        PaymentPo paymentPo = new PaymentPo();
        paymentPo.setAppid(appid);
        paymentPo.setMch_id(mch_id);
        paymentPo.setNonce_str(nonce_str);
        String newbody = new String(body.getBytes("ISO-8859-1"), "UTF-8");//以utf-8编码放入paymentPo，微信支付要求字符编码统一采用UTF-8字符编码
        paymentPo.setBody(newbody);
        paymentPo.setOut_trade_no(out_trade_no);
        paymentPo.setTotal_fee(total_fee);
        paymentPo.setSpbill_create_ip(spbill_create_ip);
        paymentPo.setNotify_url(notify_url);
        paymentPo.setTrade_type(trade_type);
        paymentPo.setOpenId(openid);
        // 把请求参数打包成数组
        Map<String, String> sParaTemp = new HashMap<>();
        sParaTemp.put("appid", paymentPo.getAppid());
        sParaTemp.put("mch_id", paymentPo.getMch_id());
        sParaTemp.put("nonce_str", paymentPo.getNonce_str());
        sParaTemp.put("body", paymentPo.getBody());
        sParaTemp.put("out_trade_no", paymentPo.getOut_trade_no());
        sParaTemp.put("total_fee", paymentPo.getTotal_fee());
        sParaTemp.put("spbill_create_ip", paymentPo.getSpbill_create_ip());
        sParaTemp.put("notify_url", paymentPo.getNotify_url());
        sParaTemp.put("trade_type", paymentPo.getTrade_type());
        sParaTemp.put("openid", paymentPo.getOpenId());
        // 除去数组中的空值和签名参数
        Map sPara = PayUtils.paraFilter(sParaTemp);
        String prestr = PayUtils.createLinkString(sPara); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        String key = "&key=替换为商户支付密钥"; // 商户支付密钥
        //MD5运算生成签名
        String mysign = PayUtils.sign(prestr, key, "utf-8").toUpperCase();
        paymentPo.setSign(mysign);
        //打包要发送的xml
        String respXml = MessageUtils.messageToXML(paymentPo);
        // 打印respXml发现，得到的xml中有“__”不对，应该替换成“_”
        respXml = respXml.replace("__", "_");
        String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";//统一下单API接口链接
        String param = respXml;
        //String result = SendRequestForUrl.sendRequest(url, param);//发起请求
        String result = PayUtils.httpRequest(url, "POST", param);
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
        System.out.println("return_msg" + return_msg);
        JSONObject JsonObject = new JSONObject();
        if (return_code == "SUCCESS" || return_code.equals(return_code)) {
            // 业务结果
            String prepay_id = map.get("prepay_id");//返回的预付单信息
            String nonceStr = UUID.randomUUID().toString().replace("-","");
            JsonObject.put("nonceStr", nonceStr);
            JsonObject.put("package", "prepay_id=" + prepay_id);
            Long timeStamp = System.currentTimeMillis() / 1000;
            JsonObject.put("timeStamp", timeStamp + "");
            String stringSignTemp = "appId=" + appid + "&nonceStr=" + nonceStr + "&package=prepay_id=" + prepay_id + "&signType=MD5&timeStamp=" + timeStamp;
            //再次签名
            String paySign = PayUtils.sign(stringSignTemp, "&key=替换为自己的密钥", "utf-8").toUpperCase();
            JsonObject.put("paySign", paySign);
            jsonArray.add(JsonObject);
        }
        return map;
    }
}
