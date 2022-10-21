package com.pms.security.config.common;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.pms.common.pojo.Result;

import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;

/**
 * 当未登录或者token失效访问接口时，自定义的返回结果
 * @author WCH
 * @Ddatetime 2022年7月7日17:46:48
 */
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
	
	
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control","no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println(JSONUtil.parse(Result.failed(HttpStatus.HTTP_UNAUTHORIZED, "身份验证不通过"))); 
        response.getWriter().flush();
    }
  
}
