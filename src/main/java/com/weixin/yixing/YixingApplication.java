package com.weixin.yixing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class YixingApplication {

	public static void main(String[] args) {
		SpringApplication.run(YixingApplication.class, args);
	}
}
