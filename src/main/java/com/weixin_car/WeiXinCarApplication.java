package com.weixin_car;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.weixin_car.mapper")
public class WeiXinCarApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeiXinCarApplication.class, args);
    }

}
