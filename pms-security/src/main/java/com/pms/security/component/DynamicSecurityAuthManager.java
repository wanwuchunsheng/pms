package com.pms.security.component;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import com.pms.common.constant.Constants;
import com.pms.common.pojo.SysResouce;
import com.pms.common.pojo.User;
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
	
    public boolean canAccess(HttpServletRequest request, Authentication authentication) {
    	try {
    		//1、获取access_token
            String authorization = request.getHeader(Constants.AUTHORIZATION);
            if(ObjectUtil.isEmpty(authorization)) {
            	return true;
            }
            String accessToken = authorization.substring(Constants.BEARER.length());
            //2、获取登录用户可访问资源权限
            User user = dynamicSecurityService.loadDataSource(accessToken);
            List<String> iterator = user.getPermissionList();
            //3、获取当前访问的路径
            String path = request.getServletPath();
            //4、判断当前路径和资源权限路径
            if(iterator.contains(path)) {
            	user.setResouceList(null);
            	user.setPermissionList(null);
            	request.setAttribute("userInfo", JSONUtil.parse(user));
            	return true;
            }
		} catch (Exception e) {
			e.printStackTrace();
			log.error("ERROR:鉴权异常! url:{}",request.getRequestURL());
		}
		return false;
		
    }
}