package com.pms.security.component;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.AntPathMatcher;

import com.pms.common.constant.Constants;
import com.pms.security.pojo.AdminUserDetails;
import com.pms.security.service.DynamicSecurityService;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 说明：权限决策控制器
 * @author WCH
 * @datetime 2022年10月11日13:19:06
 * 
 * */
@Slf4j
public class DynamicSecurityAuthManager {
	
	@Autowired
    private DynamicSecurityService dynamicSecurityService;
	
	private static AntPathMatcher matcher = new AntPathMatcher();
	
    public boolean canAccess(HttpServletRequest request, Authentication authentication) {
    	try {
    		//1、获取access_token
            String authorization = request.getHeader(Constants.AUTHORIZATION);
            if(ObjectUtil.isNotEmpty(authorization)) {
            	String accessToken = authorization.substring(Constants.BEARER.length());
            	//2、获取登录用户可访问资源权限
                AdminUserDetails user = dynamicSecurityService.loadDataSource(accessToken);
                Collection<? extends GrantedAuthority> iterator = user.getAuthorities();
                //3、获取当前访问的路径
                String path = request.getServletPath();
                //4、判断当前路径和资源权限路径
                for (GrantedAuthority authority : iterator) {
                	if(matcher.match(authority.getAuthority(), path)) {
                		request.setAttribute("userInfo", JSONUtil.parse(user));
                        return true;
                	}
                }
            }
		} catch (Exception e) {
			log.error("ERROR:无权访问 url={}，消息：{}",request.getRequestURL(),e.getMessage());
		}
		return false;
		
    }
}