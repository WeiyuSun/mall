package com.mallthirdpart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author weiyu
 */
@EnableDiscoveryClient
@SpringBootApplication
public class MallThirdpartApplication {


	public static void main(String[] args) {
		SpringApplication.run(MallThirdpartApplication.class, args);
	}
}
