package com.example.service.impl;

import com.example.entity.WeChatApi;
import com.example.mapper.WeChatApiMapper;
import com.example.service.IWeChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class WeChatService implements IWeChatService {

    @Autowired
    private WeChatApiMapper weChatApiMapper;

    @Override
    public List<WeChatApi> findAll() {
        List<WeChatApi> weChatApis = weChatApiMapper.selectList(null);
        return weChatApis;
    }

    @Override
    public int save(WeChatApi weChatApi) {
//        WeChatApi weChat = new WeChatApi();
//        weChat.setStatus("1");
//        weChat.setApplyer("plt");
        int count = weChatApiMapper.insert(weChatApi);
        return count;
    }
}
