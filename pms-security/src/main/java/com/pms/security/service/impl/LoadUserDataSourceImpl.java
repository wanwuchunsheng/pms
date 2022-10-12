package com.pms.security.service.impl;

import org.springframework.stereotype.Service;

import com.pms.common.constant.Constants;
import com.pms.common.pojo.User;
import com.pms.common.redis.IGlobalCache;
import com.pms.security.service.ILoadUserDataSource;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;

@Service
public class LoadUserDataSourceImpl implements ILoadUserDataSource{
	
	@Override
	public User getUserDataSource(IGlobalCache globalCache, String key) {
		User user = JSONUtil.toBean(JSONUtil.parseObj( globalCache.get(Constants.JUSTAUTH + key)) , User.class);
		if(ObjectUtil.isNotEmpty(user)) {
			//重新刷新token有效时间
			globalCache.expire(Constants.JUSTAUTH + key, Constants.REDIS_TOKEN_TIMEOUT);
		}
    	return user;
	}

}
