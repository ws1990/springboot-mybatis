package com.ws.study.springboot.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Description:
 *
 * @author 王松
 * @version 1.0
 * @date 19-1-22 上午10:13
 */
@SpringBootApplication
@MapperScan("com.ws.study.springboot.mybatis.*.mapper")
public class Bootstrap {

    public static void main(String[] args) {
        SpringApplication.run(Bootstrap.class, args);
    }

}
