package com.example.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpsClient {

    public static String trim(String str) {
        if (str == null) {
            return null;
        }
        return str.trim();
    }

    public static String getStringByObj(Object obj) {
        if (obj == null || "".equals((obj + "").trim()) || "undefined".equals((obj + "").trim()) || "null".equals((obj + "").trim())) {
            return "";
        }
        return trim(obj + "");
    }

    public static boolean isNotEmptyObject(Object object) {
        if (object == null || "".equals((object + "").trim()) || "null".equals((object + "").trim()) || "undefined".equals((object + "").trim())) {
            return false;
        } else {
            return true;
        }
    }

    public static String doPost(String jsonstr, String charset, Map map){
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try{
            httpClient = new SSLClient();
            httpPost = new HttpPost(getStringByObj(map.get("url")));

            if(isNotEmptyObject(map.get("C-DynAmic-Password-Foruser"))){
                httpPost.addHeader("C-DynAmic-Password-Foruser", getStringByObj(map.get("C-DynAmic-Password-Foruser")));
            }
            if(isNotEmptyObject(map.get("C-Business-Id"))){
                httpPost.addHeader("C-Business-Id", getStringByObj(map.get("C-Business-Id")));
            }
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.addHeader("C-App-Id", getStringByObj(map.get("C-App-Id")));
            httpPost.addHeader("Referer", getStringByObj(map.get("Referer")));
            httpPost.addHeader("C-Tenancy-Id", getStringByObj(map.get("C-Tenancy-Id")));

            StringEntity se = new StringEntity(jsonstr, Charset.forName("UTF-8"));
            se.setContentType("text/json");
            se.setContentEncoding(new BasicHeader("Content-Type", "application/json"));
            httpPost.setEntity(se);
            HttpResponse response = httpClient.execute(httpPost);
            if(response != null){
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null){
                    result = EntityUtils.toString(resEntity,charset);
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return result;
    }

    public static String doGet(Map<String, Object> paramMap) {
        HttpClient httpClient = null;
        HttpResponse response = null;
        String result = "";
        try {
            // 创建httpGet远程连接实例
            httpClient = new SSLClient();
            HttpGet httpGet = new HttpGet(getStringByObj(paramMap.get("url")));
            // 设置请求头信息，鉴权
            httpGet.setHeader("C-App-Id", getStringByObj(paramMap.get("C-App-Id")));
            httpGet.setHeader("C-Business-Id", getStringByObj(paramMap.get("C-Business-Id")));
            httpGet.setHeader("C-Dynamic-Password", getStringByObj(paramMap.get("C-Dynamic-Password")));
            httpGet.setHeader("C-Tenancy-Id", getStringByObj(paramMap.get("C-Tenancy-Id")));
            httpGet.setHeader("Referer", getStringByObj(paramMap.get("Referer")));
            // 设置配置请求参数
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// 连接主机服务超时时间
                    .setConnectionRequestTimeout(35000)// 请求超时时间
                    .setSocketTimeout(60000)// 数据读取超时时间
                    .build();
            // 为httpGet实例设置配置
            httpGet.setConfig(requestConfig);
            try {
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 执行get请求得到返回对象
            response = httpClient.execute(httpGet);
            // 通过返回对象获取返回数据
            HttpEntity entity = response.getEntity();
            // 通过EntityUtils中的toString方法将结果转换为字符串
            result = EntityUtils.toString(entity,"utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
