package com.pms.security.service;

import com.pms.common.redis.IGlobalCache;
import com.pms.security.pojo.AdminUserDetails;

public interface ILoadUserDataSource {

	AdminUserDetails getUserDataSource(IGlobalCache globalCache,String key);

}
