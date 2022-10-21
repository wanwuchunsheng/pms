package com.pms.security.handler;

import javax.servlet.http.HttpServletRequest;

import com.pms.security.pojo.AdminUserDetails;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserProfile {
	
	
	/**
	 * 获取用户基础信息
	 *    通用方法封装
	 * @author WCH
	 * @date 2020-8-31 17:31:58
	 * 
	 * */
	public static AdminUserDetails getUser(HttpServletRequest request) {
		try {
			return JSONUtil.toBean(JSONUtil.parseObj( request.getAttribute("userInfo")) , AdminUserDetails.class);
		} catch (Exception e) {
			log.error("获取全局用户登录信息异常：{}",e.getMessage());
		}
		return null; 
	}

}
