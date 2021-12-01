package com.example.controller;

import com.example.service.SayHalo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sayHalo")
public class HaloController {

    @Autowired
    private SayHalo sayHalo;

    @GetMapping("/getHalo")
    public String getHalo(){
        return sayHalo.sayHalo();
    }
}
