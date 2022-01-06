package com.example.controller;

import com.example.entity.WeChatApi;
import com.example.mapper.WeChatApiMapper;
import com.example.qq.wechat.aes.*;
import com.example.service.SayHalo;

import com.example.util.HttpsClient;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sayHalo")
public class HaloController {

    @Autowired
    private SayHalo sayHalo;

    @Autowired
    private WeChatApiMapper weChatApiMapper;

    private static final Logger logger = LoggerFactory.getLogger(HaloController.class);

    @RequestMapping("/merryChristmas")
    public String getHalo(){
        return sayHalo.sayHalo();
    }

    /**
     * 验证访问企业微信API接口的端口号是否可用
     * @param msg_signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @return
     * @throws AesException
     */
    @GetMapping("/getApi")
    public String getApi(@RequestParam String msg_signature,@RequestParam String timestamp,@RequestParam String nonce,@RequestParam String echostr) throws AesException {

        WXBizJsonMsgCrypt bizJsonMsgCrypt = new WXBizJsonMsgCrypt("Gy2s1RgBcruc",
                "zEMOwv2PvGtydmNP2SLHYRda3rz9nV9gDmHGJcNVvSg","wwaf4fa29cf5869625");
        /**
         * 验证URL函数
         * 1.签名校验
         * 2.解密数据包，得到明文消息内容
         */
        String verifyURL = bizJsonMsgCrypt.VerifyURL(msg_signature,timestamp,nonce,echostr);

        return verifyURL;
    }

    /**
     * 审批申请状态变化回调通知
     * @param httpServletRequest
     * @return
     * @throws Exception
     */
    @PostMapping("/getApi")
    @ResponseBody
    public String postApi(HttpServletRequest httpServletRequest) throws Exception {
        String str = null;
        WXBizMsgCrypt bizJsonMsgCrypt = new WXBizMsgCrypt("Gy2s1RgBcruc",
                "zEMOwv2PvGtydmNP2SLHYRda3rz9nV9gDmHGJcNVvSg","wwaf4fa29cf5869625");
        //  提取xml消息体
        String content = ReadAsChars(httpServletRequest);
        logger.info("请求消息体=  "+content);
        //  提取出xml数据包中的加密消息
        Object[] extract = XMLParse.extract(content);
        //  消息体签名校验
        String sha1 =SHA1.getSHA1("Gy2s1RgBcruc", httpServletRequest.getParameter("timestamp")
                , httpServletRequest.getParameter("nonce"), (String) extract[1]);
        //  比较消息签名是否相等，相等则表示验证通过
        if (sha1.equals(httpServletRequest.getParameter("msg_signature"))){
            /**
             * 解密函数
             * 1.签名效验
             * 2.解密数据包，得到明文消息结构体
             */
            str = bizJsonMsgCrypt.DecryptMsg(httpServletRequest.getParameter("msg_signature"),
                    httpServletRequest.getParameter("timestamp"), httpServletRequest.getParameter("nonce"), content);
            logger.info("解密后函数=  "+str);
            //  将解密后的xml内容转为json
            XMLSerializer xmlSerializer = new XMLSerializer();
            JSONObject jsonObject = JSONObject.fromObject(xmlSerializer.read(str));
            //  获取状态
            String status = jsonObject.getJSONObject("ApprovalInfo").getString("SpStatus");
            //  获取申请人
            String Applyer = jsonObject.getJSONObject("ApprovalInfo").getJSONObject("Applyer").getString("UserId");
            WeChatAPI weChatAPI = new WeChatAPI();
            //  往企业微信推送消息
//            String resultApi = weChatAPI.getApi(status,Applyer);
//            logger.info("推送消息结果集=  "+resultApi);
            sendGroup();

            WeChatApi weChatApi = new WeChatApi();
            weChatApi.setApplyer(Applyer);
            if(StringUtils.isNotEmpty(status) && "1".equals(status)){
                weChatApi.setStatus(status);
            }
            weChatApiMapper.insert(weChatApi);
        }
        return str;
    }

    /**
     * 创建群聊会话
     * @return
     * @throws AesException
     */
    public String createGroup() throws AesException {
        Map resultMap = new HashMap();
        WeChatAPI weChatAPI = new WeChatAPI();
        String groupAccessToken = weChatAPI.getGroupAccessToken();
        JSONObject resultJSON = JSONObject.fromObject(groupAccessToken);
        String accessToken = resultJSON.getString("access_token");
        JSONObject signObj = new JSONObject();
        signObj.put("name","group");
        signObj.put("owner","GuanHui");
        List list = new ArrayList();
        list.add("GuanHui");
        list.add("h");
        list.add("XiaoPeng");
        signObj.put("userlist",list);
        signObj.put("chatid","");
        String url = "https://qyapi.weixin.qq.com/cgi-bin/appchat/create?access_token="+accessToken;
        resultMap.put("url",url);
        String content = HttpsClient.doPost(signObj.toString(), "utf-8", resultMap);
        return content;
    }

    /**
     * 推送群聊消息
     * @return
     */
    public String sendGroup() throws AesException {
        Map paramMap = new HashMap();
        WeChatAPI weChatAPI = new WeChatAPI();
        String groupAccessToken = weChatAPI.getGroupAccessToken();
        JSONObject resultJSON = JSONObject.fromObject(groupAccessToken);
        String accessToken = resultJSON.getString("access_token");

        JSONObject signObj = new JSONObject();
        signObj.put("chatid","wraljLCAAATFJXkyJblW0V3Q_ja9HYtg");
        signObj.put("msgtype","text");
        JSONObject textJson = new JSONObject();
        textJson.put("content","你的快递已到请携带工卡前往邮件中心领取");
        signObj.put("text",textJson);
        signObj.put("safe",0);

        String url = "https://qyapi.weixin.qq.com/cgi-bin/appchat/send?access_token="+accessToken;
        paramMap.put("url",url);
        String content = HttpsClient.doPost(signObj.toString(), "utf-8", paramMap);
        return content;
    }

    public String getApprovalDetail(String spNo) throws AesException {
        Map paramMap = new HashMap();
        WeChatAPI weChatAPI = new WeChatAPI();
        String token = weChatAPI.getAccessToken();
        JSONObject resultJSON = JSONObject.fromObject(token);
        String accessToken = resultJSON.getString("access_token");
        JSONObject signObj = new JSONObject();
        signObj.put("sp_no",spNo);
        String url = "https://qyapi.weixin.qq.com/cgi-bin/oa/getapprovaldetail?access_token="+accessToken;
        paramMap.put("url",url);
        String content = HttpsClient.doPost(signObj.toString(), "utf-8", paramMap);

        return content;
    }

    /**
     * 解析xml请求体
     * @param request
     * @return
     */
    public static String ReadAsChars(HttpServletRequest request) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder("");
        try
        {
            br = request.getReader();
            String str;
            while ((str = br.readLine()) != null)
            {
                sb.append(str);
            }
            br.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (null != br)
            {
                try
                {
                    br.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }




}
