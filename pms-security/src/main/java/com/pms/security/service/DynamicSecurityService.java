package com.pms.security.service;

import com.pms.security.pojo.AdminUserDetails;

/**
 * 动态权限相关业务类
 * @author WCH
 */
public interface DynamicSecurityService {
    /**
     * 加载资源
     */
	AdminUserDetails loadDataSource(String accessToken);
    
}
