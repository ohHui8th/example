package com.example.service.impl;

import com.example.service.SayHalo;
import org.springframework.stereotype.Service;

@Service
public class SayHaloImpl implements SayHalo {
    @Override
    public String sayHalo() {
        return "why you bully me?";
    }
}
