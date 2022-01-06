package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@MapperScan({"com.baomidou.mybatisplus.samples.quickstart.mapper","com.example.mapper"})
public class ProviderServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProviderServerApplication.class, args);
        System.out.println("    START   UP  COMPLETE                          " +
                "       .__      ___ ___       .__  ______  __  .__         \n" +
                "  ____ |  |__  /   |   \\ __ __|__|/  __  \\/  |_|  |__    \n" +
                " /  _ \\|  |  \\/    ~    \\  |  \\  |>      <   __\\  |  \\ \n" +
                "(  <_> )   Y  \\    Y    /  |  /  /   --   \\  | |   Y  \\\n" +
                " \\____/|___|  /\\___|_  /|____/|__\\______  /__| |___|  /\n" +
                "            \\/       \\/                 \\/          \\/");

    }

}
