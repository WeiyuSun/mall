package com.coupon;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@MapperScan("com.coupon.dao")
@EnableDiscoveryClient
@SpringBootApplication
public class MallCouponApplication {
	public static void main(String[] args) {
		SpringApplication.run(MallCouponApplication.class, args);
	}

}
