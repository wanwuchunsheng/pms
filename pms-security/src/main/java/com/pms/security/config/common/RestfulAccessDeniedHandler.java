package com.pms.security.config.common;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.pms.common.pojo.Result;

import cn.hutool.core.convert.Convert;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;

/**
 * 当访问接口没有权限时，自定义的返回结果
 * @author WCH
 * @Ddatetime 2022年7月7日17:46:48
 */
public class RestfulAccessDeniedHandler implements AccessDeniedHandler{
	
    @Override
    public void handle(HttpServletRequest request,HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
    	response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control","no-cache");
    	response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        if(Convert.toStr(HttpStatus.HTTP_UNAUTHORIZED).equals(e.getMessage())) {
        	response.getWriter().println(JSONUtil.parse(Result.failed(HttpStatus.HTTP_UNAUTHORIZED, "身份验证不通过"))); 
        }else {
        	response.getWriter().println(JSONUtil.parse(Result.failed(HttpStatus.HTTP_FORBIDDEN, "无访问权限")));
        }
        response.getWriter().flush();
    }
    

}
