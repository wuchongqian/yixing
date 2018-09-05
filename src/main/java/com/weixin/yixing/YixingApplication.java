package com.weixin.yixing;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.weixin.yixing.dao")
public class YixingApplication {

	public static void main(String[] args) {
		SpringApplication.run(YixingApplication.class, args);
	}
}
