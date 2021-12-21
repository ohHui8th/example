package com.example.controller;

import com.example.util.HttpsClient;
import com.example.qq.wechat.aes.AesException;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WeChatAPI {

    public WeChatAPI() throws AesException {
    }

    public String getAccessToken(){
        Map resultMap = new HashMap();
        String corpId = "wwaf4fa29cf5869625";
        String corpSecret = "1k5AnbaOIxKHr3QWWn0cA0Yu58kf7yufBfmquuE5oKE";
        String url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid="+corpId+"&corpsecret="+corpSecret;
        resultMap.put("url",url);
        String content = HttpsClient.doGet(resultMap);
        return content;
    }

    /**
     *
     * @param status    审批状态
     * @param applyer   申请人
     * @return
     * @throws Exception
     */
    public String getApi(String status,String applyer) throws Exception {
        String result = null;
        String resultToken = getAccessToken();

        JSONObject resultJSON = JSONObject.fromObject(resultToken);
        String accessToken = resultJSON.getString("access_token");
        String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token="+accessToken;

        //获取验证sign
        JSONObject signObj = new JSONObject();
        signObj.put("touser", "@all");
        signObj.put("msgtype", "text");
        signObj.put("agentid", 3010040);
        JSONObject textJson = new JSONObject();
        textJson.put("content","审批已完成"+status);
        signObj.put("text", textJson);
        signObj.put("safe", 0);
        signObj.put("enable_id_trans", 0);
        signObj.put("enable_duplicate_check", 0);
        signObj.put("duplicate_check_interval", 1800);
        try {
            Map paramMap = new HashMap();
            paramMap.put("url",url);
            result = HttpsClient.doPost(signObj.toString(),"utf-8",paramMap);
            System.out.println("推送办件数据="+result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


}
