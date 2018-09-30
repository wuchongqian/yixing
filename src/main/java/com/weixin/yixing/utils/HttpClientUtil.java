package com.weixin.yixing.utils;

import com.alibaba.fastjson.JSONObject;
import com.sun.corba.se.impl.orbutil.threadpool.TimeoutException;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.lang.reflect.Method;
import java.net.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.Map.Entry;


public class HttpClientUtil {

    static Logger log = Logger.getLogger(HttpClientUtil.class) ;

    private static RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();//设置请求和传输超时时间

    private static CloseableHttpClient httpclient = HttpClients.createDefault();

    public static String send(String httpUrl, String message) throws IOException {
        String result = null ;
        HttpPost httpPost = new HttpPost(httpUrl);
        //设置数据读取超时5s   传输超时5s    链接请求超时5s
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(5000)
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .build();
        httpPost.setConfig(requestConfig) ;
        message = URLEncoder.encode(message, "UTF-8") ;
        StringEntity entity = new StringEntity(message);
        httpPost.setEntity(entity);
        CloseableHttpResponse response = httpclient.execute(httpPost);
        BufferedReader in = null ;
        try {
            InputStream content = response.getEntity().getContent() ;
            in = new BufferedReader(new InputStreamReader(content));
            StringBuilder sb = new StringBuilder();
            String line = "" ;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            result = sb.toString() ;
            System.out.println("接收原始报文：" + URLDecoder.decode(result, "UTF-8")) ;
        } finally {
            EntityUtils.consume(response.getEntity());
            response.close();
        }
        return result ;
    }

    /**
     * 发起post请求，请求参数以Map集合形式传入，封装到List <NameValuePair> 发起post请求
     * @param httpUrl
     * @param params
     * @return
     * @throws Exception
     */
    public static String post(String httpUrl, Map<String, String> params) throws Exception {
        String result = null ;
        CloseableHttpClient httpclient = createSSLClientDefault();
        //httpclient.
        //httpclient.
        BufferedReader in = null ;
        HttpPost httpPost = new HttpPost(httpUrl);
        httpPost.setConfig(requestConfig);
        List <NameValuePair> nvps = new ArrayList <NameValuePair>();
        StringBuffer paramsBuf = new StringBuffer() ;
        for(Entry<String, String> e : params.entrySet()) {
            nvps.add(new BasicNameValuePair(e.getKey(), e.getValue()));
            paramsBuf.append("&").append(e.getKey()).append("=").append(e.getValue()) ;
        }
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
        try {
//          报文参数27：&id=jn-3-767744&groupPlatProTerminalId=119667&extend=uwJZ8j3CkpGPL4rM5J6KJhjR99O7yAe3BAGLS8ooI8ijNqKHfzTaK6W9wQvjZEVOmWJ3HxFb2O9D
//          wDbe3++UiQ==&xxtCode=370000&terminalType=1&role=3&type=3
            System.out.println("post请求报文地址：" + httpUrl+"?"+paramsBuf.toString()) ;
            CloseableHttpResponse response = httpclient.execute(httpPost);
            InputStream content = response.getEntity().getContent() ;
            in = new BufferedReader(new InputStreamReader(content, "UTF-8"));
//          in = new BufferedReader(new InputStreamReader(content, "GBK"));
//          in = new BufferedReader(new InputStreamReader(content));
            StringBuilder sb = new StringBuilder();
            String line = "" ;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            result = sb.toString() ;
            System.out.println("响应报文：" + result) ;
            //  响应报文：{"ret":0,"msg":"成功","callbackurl":"https://edu.10086.cn/test-sso/login?service=http%3A%2F%2F112.35.7.169%3A9010%2Feducloud%2Flogin%2Flogin%3Ftype%3D3%26mode%3D1%26groupId%3D4000573%26provincePlatformId%3D54","accesstoken":"2467946a-bee9-4d8c-8cce-d30181073b75"}Í
            return result ;
        } catch (Exception e) {
            e.printStackTrace() ;
        } finally {
            httpclient.close();
        }
        return null ;
    }

    public static JSONObject postData(String httpUrl, Object obj) throws Exception {
        JSONObject json = null;
        try{
            String result = post(httpUrl,obj);
            json = JSONObject.parseObject(result);
        }catch(TimeoutException e){
            System.out.println("请求超时了："+httpUrl);
            throw e;
        }finally {
            return json ;
        }
    }

    public static String post(String httpUrl, Object obj) throws Exception {
        Map<String, String>  params = getParamData(obj);
        String result = null ;

        try {
            result = post(httpUrl,params);
            return result ;
        } catch (Exception e) {
            e.printStackTrace() ;
        } finally {
            httpclient.close();
        }
        return null ;
    }


    private static Map<String, String>  getParamData(Object obj) {

        Class cla = obj.getClass();
        Map<String, String> map = new HashMap<String, String>();
        Method[] methods = cla.getDeclaredMethods();
        try {
            for (Method m : methods) {
                String mname = m.getName();
                if (mname.startsWith("get")) {
                    String name = mname.substring(3, mname.length());// 截取字段
                    name = name.substring(0, 1).toLowerCase()
                            + name.substring(1, name.length());// 把首字母变小写
                    String value = m.invoke(obj)==null?"":m.invoke(obj).toString();
                    map.put(name,value);// 取值
                }

            }
            Class superclass = cla.getSuperclass();
            while (!superclass.equals(Object.class)) {
                Method[] superclassmethods = superclass.getDeclaredMethods();
                for (Method m : superclassmethods) {
                    String mname = m.getName();
                    if (mname.startsWith("get")) {
                        String name = mname.substring(3, mname.length());// 截取字段
                        name = name.substring(0, 1).toLowerCase()
                                + name.substring(1, name.length());// 把首字母变小写
                        String value = m.invoke(obj)==null?"":m.invoke(obj).toString();

                        map.put(name,value);// 取值
                    }

                }
                superclass = superclass.getSuperclass();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    public static CloseableHttpClient createSSLClientDefault(){

        try {
            //SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            // 在JSSE中，证书信任管理器类就是实现了接口X509TrustManager的类。我们可以自己实现该接口，让它信任我们指定的证书。
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            //信任所有
            X509TrustManager x509mgr = new X509TrustManager() {

                //　　该方法检查客户端的证书，若不信任该证书则抛出异常
                public void checkClientTrusted(X509Certificate[] xcs, String string) {
                }
                // 　　该方法检查服务端的证书，若不信任该证书则抛出异常
                public void checkServerTrusted(X509Certificate[] xcs, String string) {
                }
                // 　返回受信任的X509证书数组。
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[] { x509mgr }, null);
            ////创建HttpsURLConnection对象，并设置其SSLSocketFactory对象
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            //  HttpsURLConnection对象就可以正常连接HTTPS了，无论其证书是否经权威机构的验证，只要实现了接口X509TrustManager的类MyX509TrustManager信任该证书。
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();


        } catch (KeyManagementException e) {

            e.printStackTrace();

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();

        } catch (Exception e) {

            e.printStackTrace();

        }

        // 创建默认的httpClient实例.
        return  HttpClients.createDefault();

    }

    public static String doGet(String url, Map<String, String> param) {
        // 创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String resultString = "";
        CloseableHttpResponse response = null;
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.get(key));
                }
            }
            URI uri = builder.build();

            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);

            // 执行请求
            response = httpClient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }

    public static String jsonPost(String url, JSONObject object) {
        StringBuffer sb = new StringBuffer("");
        try {
            URL url1 = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) url1
                    .openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("POST");
            connection.connect();
            //POST请求
            DataOutputStream out = new DataOutputStream(
                    connection.getOutputStream());
            out.writeBytes(object.toString());

            System.out.println("data="+object.toString());
            out.flush();
            out.close();
            //读取响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String lines;

            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                sb.append(lines);
            }

            reader.close();
            connection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String httpsRequest(String requestUrl, String requestMethod, String outputStr){
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            // 当outputStr不为null时向输出流写数据
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            return buffer.toString();
        } catch (ConnectException ce) {
            System.out.println("连接超时：{}");
        } catch (Exception e) {
            System.out.println("https请求异常：{}");
        }
        return null;
    }


}