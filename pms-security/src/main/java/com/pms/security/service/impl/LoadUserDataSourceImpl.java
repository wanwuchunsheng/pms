package com.pms.security.service.impl;

import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;

import com.pms.common.constant.Constants;
import com.pms.common.redis.IGlobalCache;
import com.pms.security.pojo.AdminUserDetails;
import com.pms.security.service.ILoadUserDataSource;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

@Service
public class LoadUserDataSourceImpl implements ILoadUserDataSource{
	
	@Override
	public AdminUserDetails getUserDataSource(IGlobalCache globalCache, String key) {
		JSONObject jsonStr = JSONUtil.parseObj( globalCache.get(Constants.JUSTAUTH + key));
		if(ObjectUtil.isEmpty(jsonStr)) {
			throw new AuthorizationServiceException(Convert.toStr(HttpStatus.HTTP_UNAUTHORIZED));
		}
		globalCache.expire(Constants.JUSTAUTH + key, Constants.REDIS_TOKEN_TIMEOUT);
		AdminUserDetails adminUser = JSONUtil.toBean(jsonStr , AdminUserDetails.class);
    	return adminUser;
		
	}

}
