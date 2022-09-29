package com.pms.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan(basePackages = {"com.pms.admin.*.dao"})
@ComponentScan(basePackages = {"com.pms"})
public class AdminApplication {
	
	public static void main(String[] args) {
		
		SpringApplication.run(AdminApplication.class, args);
	}

}
