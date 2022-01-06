package com.example.controller;

import com.example.entity.WeChatApi;
import com.example.mapper.WeChatApiMapper;
import com.example.qq.wechat.aes.AesException;
import com.example.service.IWeChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TestMain {

    @Autowired
    private IWeChatService weChatService;

    @Test
    public void findAdd(){
        List<WeChatApi> all = weChatService.findAll();
        for (WeChatApi weChatApi : all){
            System.out.println(weChatApi.getApplyer());
        }
    }

    @Test
    public void save(){
        WeChatApi weChatApi = new WeChatApi();
        weChatApi.setStatus("1");
        weChatApi.setApplyer("plt");
        int save = weChatService.save(weChatApi);
        System.out.println(save);

    }
//    public static void main(String[] args) throws AesException {
//        WeChatAPI weChatAPI = new WeChatAPI();
//        String groupAccessToken = weChatAPI.getGroupAccessToken();
//        HaloController haloController = new HaloController();
//        String group = haloController.createGroup();
//        String content = haloController.sendGroup();
//        System.out.println(content);
//    }
}
