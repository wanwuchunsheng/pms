package com.pms.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import com.pms.security.config.common.IgnoreUrlsConfig;
import com.pms.security.config.common.RestAuthenticationEntryPoint;
import com.pms.security.config.common.RestfulAccessDeniedHandler;


/**
 * SpringSecurity 5.4.x以上新用法配置，仅用于配置HttpSecurity
 * Created by macro on 2019/11/5.
 */
@Configuration
public class SecurityConfig {

    @Autowired
    private IgnoreUrlsConfig ignoreUrlsConfig;
    
    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;
    
    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = httpSecurity.authorizeRequests();
        //不需要保护的资源路径允许访问
        for (String url : ignoreUrlsConfig.getUrls()) {
            registry.antMatchers(url).permitAll();
        }
        //允许跨域请求的OPTIONS请求
        registry.antMatchers(HttpMethod.OPTIONS).permitAll();
        // 任何请求需要身份认证
        registry.and()
                .authorizeRequests()
                .anyRequest()
                //.authenticated()
                .access("@dynamicSecurityAuthManager.canAccess(request,authentication)")
                .and()
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // 自定义权限拒绝处理类
                .and()
                .exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
			    // 除上面外的所有请求全部需要鉴权认证
		        // 资源服务内部JWT认证
		        .oauth2ResourceServer(resourceServer -> resourceServer
		                .accessDeniedHandler(restfulAccessDeniedHandler)
		                .authenticationEntryPoint(restAuthenticationEntryPoint)
		                .jwt()
		        );
        return httpSecurity.build();
    }
    
}
