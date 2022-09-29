package com.pms.admin.config.security;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;

import com.pms.admin.config.redis.IGlobalCache;
import com.pms.security.component.DynamicSecurityService;
import com.pms.security.pojo.UmsResource;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class AdminSecurityConfig {
	
	@Autowired
	private IGlobalCache globalCache;
	
	@Bean
    public DynamicSecurityService dynamicSecurityService() {
        return new DynamicSecurityService() {
            @Override
            public Map<String, ConfigAttribute> loadDataSource() {
                Map<String, ConfigAttribute> map = new ConcurrentHashMap<>();
                List<UmsResource> resourceList = new ArrayList<>();
                UmsResource ur1 = new UmsResource();
                ur1.setId(1l);
                ur1.setCreateTime(new Date());
                ur1.setName("商品品牌管理");
                ur1.setUrl("/brand/**");
                ur1.setCategoryId(1l);
                UmsResource ur2 = new UmsResource();
                ur2.setId(2l);
                ur2.setCreateTime(new Date());
                ur2.setName("商品属性分类管理");
                ur2.setUrl("/productAttribute/**");
                ur2.setCategoryId(1l);
                UmsResource ur3 = new UmsResource();
                ur3.setId(3l);
                ur3.setCreateTime(new Date());
                ur3.setName("后台管理");
                ur3.setUrl("/admin/queryUserInfo");
                ur3.setCategoryId(1l);
                resourceList.add(ur1);
                resourceList.add(ur2);
                resourceList.add(ur3);
                for (UmsResource resource : resourceList) {
                    map.put(resource.getUrl(), new SecurityConfig(resource.getId() + ":" + resource.getName()));
                }
                return map;
            }
        };
    }

}
