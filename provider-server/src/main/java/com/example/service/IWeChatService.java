package com.example.service;

import com.example.entity.WeChatApi;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IWeChatService {

    public List<WeChatApi> findAll();

    int save(WeChatApi weChatApi);
}
