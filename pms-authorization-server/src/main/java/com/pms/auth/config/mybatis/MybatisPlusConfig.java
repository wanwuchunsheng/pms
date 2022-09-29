package com.pms.auth.config.mybatis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;

/**
 * 
 * 插件
 * @author WCH
 * 
 * */
@Configuration
public class MybatisPlusConfig {
	
	/**
     *   mybatis-plus分页插件
     */
    @Bean
    public MybatisPlusInterceptor paginationInterceptor() {
    	 MybatisPlusInterceptor page = new MybatisPlusInterceptor();
    	 page.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
    	 return page;
    }


}
