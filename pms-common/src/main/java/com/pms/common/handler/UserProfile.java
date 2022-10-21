package com.pms.common.handler;

import javax.servlet.http.HttpServletRequest;

import com.pms.common.pojo.User;

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
	public static User getUser(HttpServletRequest request) {
		try {
			User user = JSONUtil.toBean(JSONUtil.parseObj( request.getAttribute("userInfo")) , User.class);
			return user;
		} catch (Exception e) {
			log.error("获取全局用户登录信息异常：{}",e.getMessage());
		}
		return null; 
	}

}
