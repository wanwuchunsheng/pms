package com.pms.common.utils;

import java.util.HashMap;
import java.util.Map;

import cn.hutool.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpClientUtils {
	
	public static String getAuthAccessToken(String url){
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("grant_type", "client_credentials");
			return HttpRequest.post(url).basicAuth("my_client","123456").form(map).timeout(15000).execute().body();
		} catch (Exception e) {
			log.error("获取localAccessToken异常{}",e);
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
			return HttpRequest.post(url).basicAuth("my_client","123456").form(map).timeout(15000).execute().body();
		} catch (Exception e) {
			log.error("注销AccessToken异常{}",e);
		}
		return null;
	}

}
