package com.pms.security.component;

import org.springframework.security.access.ConfigAttribute;

import com.pms.security.pojo.UmsResource;

import java.util.List;
import java.util.Map;

/**
 * 动态权限相关业务类
 * @author WCH
 */
public interface DynamicSecurityService {
    /**
     * 加载资源ANT通配符和资源对应MAP
     */
    Map<String, ConfigAttribute> loadDataSource();
    
    List<UmsResource> loadDataMenu();
}
