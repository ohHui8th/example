package com.example.controller;

import com.example.entity.WeChatApi;
import com.example.mapper.WeChatApiMapper;
import com.example.qq.wechat.aes.*;
import com.example.service.SayHalo;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

@RestController
@RequestMapping("/sayHalo")
public class HaloController {

    @Autowired
    private SayHalo sayHalo;

    @Autowired
    private WeChatApiMapper weChatApiMapper;

    private static final Logger logger = LoggerFactory.getLogger(HaloController.class);

    @GetMapping("/getHalo")
    public String getHalo(){
        return sayHalo.sayHalo();
    }

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

    @PostMapping("/getApi")
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
            String resultApi = weChatAPI.getApi(status,Applyer);
            logger.info("推送消息结果集=  "+resultApi);
            WeChatApi weChatApi = new WeChatApi();
            if(StringUtils.isNotEmpty(status)){
                weChatApi.setApplyer(Applyer);
                weChatApi.setStatus(status);
                weChatApiMapper.insert(weChatApi);
            }
        }
        return str;
    }

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
