package com.weixin.yixing.utils;


import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {

    public static String METHOD_GET = "GET";
    public static String METHOD_POST = "POST";
    public static String METHOD_DELETE = "DELETE";
    public static String METHOD_PUT = "PUT";

    //日志文件
    private static Logger logger = Logger.getLogger(HttpUtils.class);

    public static HttpURLConnection sendRequest(String url,String param,String method) throws Exception {

        byte[] entitydata = param.getBytes("UTF-8");

        HttpURLConnection c = (HttpURLConnection) new URL(url)
                .openConnection();

        logger.debug("网络请求："+ url+"，数据："+param+"，方法："+method);

        c.setRequestProperty("accept", "*/*");
        c.setRequestProperty("connection", "Keep-Alive");
        c.setRequestProperty("User-agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:39.0) Gecko/20100101 Firefox/39.0");
        c.setRequestProperty("Accept-Charset", "UTF-8");

        if(!method.equals(METHOD_GET)){
            c.setRequestMethod(method);

            c.setDoOutput(true);

            c.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

            c.setRequestProperty("Content-Length",
                    String.valueOf(entitydata.length));

            OutputStream outStream = c.getOutputStream();
            outStream.write(entitydata);
            outStream.flush();
            outStream.close();

            int responeCode = -1;

            responeCode = c.getResponseCode();

            if(responeCode == 500){
                throw new Exception("AG服务异常");
            }
        }else{
            c.setRequestMethod("GET");
        }

        c.connect();

        return c;
    }

    public String getResponse(HttpURLConnection c) throws IOException{

        String sCurrentLine = "";

        String sTotalString = "";

        java.io.InputStream is = null;

        BufferedReader reader = null;
        try{
            is = c.getInputStream();
            reader = new BufferedReader(
                    new InputStreamReader(is,"UTF-8"));
            while ((sCurrentLine = reader.readLine()) != null)
                if (sCurrentLine.length() > 0)
                    sTotalString = sTotalString + sCurrentLine.trim();
        }finally {
            if(reader!=null){
                reader.close();
            }
        }
        return sTotalString;
    }

}
