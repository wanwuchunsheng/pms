package com.pms.common.utils;

import java.util.HashMap;
import java.util.Map;

import com.pms.common.constant.Constants;

import cn.hutool.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpClientUtils {
	
	public static String getAuthAccessToken(String url){
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("grant_type", "client_credentials");
			return HttpRequest.post(url).basicAuth(Constants.AUTH_CLIENT,Constants.AUTH_SECRET).form(map).timeout(15000).execute().body();
		} catch (Exception e) {
			log.error("获取localAccessToken异常{}",e.getMessage());
		}
		return null;
	}

	/**
     * 注销access_token
     * @return
     */
	public static String revokeAccessToken(String url,String accessToken) {
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("token", accessToken);
			map.put("token_type_hint", "access_token");
			return HttpRequest.post(url).basicAuth(Constants.AUTH_CLIENT,Constants.AUTH_SECRET).form(map).timeout(15000).execute().body();
		} catch (Exception e) {
			log.error("注销AccessToken异常{}",e.getMessage());
		}
		return null;
	}
	
	/**
     * 通用方法
     * @return
     */
	public static String commonReqAuth(String url,Map<String,Object> map) {
		try {
			return HttpRequest.post(url).basicAuth(Constants.AUTH_CLIENT,Constants.AUTH_SECRET).form(map).timeout(15000).execute().body();
		} catch (Exception e) {
			log.error("通用auth方法调用异常{}",e.getMessage());
		}
		return null;
	}

}
