package com.pms.security.service;

import com.pms.common.pojo.User;

/**
 * 动态权限相关业务类
 * @author WCH
 */
public interface DynamicSecurityService {
    /**
     * 加载资源
     */
	User loadDataSource(String accessToken);
    
}
