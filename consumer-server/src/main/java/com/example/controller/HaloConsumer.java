package com.example.controller;

import com.example.service.SayHaloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/halo")
public class HaloConsumer {

    @Autowired
    private SayHaloService sayHaloService;

    @GetMapping("/getConsumer")
    public String getConsumer(){
        return sayHaloService.getProduct();
    }
}
