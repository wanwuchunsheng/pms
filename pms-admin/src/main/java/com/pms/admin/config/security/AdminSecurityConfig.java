package com.pms.admin.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pms.common.pojo.User;
import com.pms.common.redis.IGlobalCache;
import com.pms.security.service.DynamicSecurityService;
import com.pms.security.service.ILoadUserDataSource;

@Configuration
public class AdminSecurityConfig{
	
	@Autowired
	private IGlobalCache globalCache;
	
	@Autowired
	private ILoadUserDataSource loadUserDataSource;
	
	@Bean
    public DynamicSecurityService dynamicSecurityService() {
        return new DynamicSecurityService() {
			@Override
            public User loadDataSource(String key) {
				return loadUserDataSource.getUserDataSource(globalCache,key);
            }
        };
    }

}
