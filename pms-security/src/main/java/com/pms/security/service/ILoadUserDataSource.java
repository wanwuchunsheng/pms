package com.pms.security.service;

import com.pms.common.pojo.User;
import com.pms.common.redis.IGlobalCache;

public interface ILoadUserDataSource {

	User getUserDataSource(IGlobalCache globalCache,String key);

}
