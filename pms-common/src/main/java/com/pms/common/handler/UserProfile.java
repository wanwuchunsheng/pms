package com.pms.common.handler;

import javax.servlet.http.HttpServletRequest;

import com.pms.common.pojo.User;

public class UserProfile {
	
	
	/**
	 * 获取用户基础信息
	 *    通用方法封装
	 * @author WCH
	 * @date 2020-8-31 17:31:58
	 * 
	 * */
	public static User getUser(HttpServletRequest request) {
		return (User) request.getAttribute("userInfo");
	}

}
