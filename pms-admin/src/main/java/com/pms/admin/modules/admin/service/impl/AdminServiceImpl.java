package com.pms.admin.modules.admin.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.pms.admin.modules.admin.service.IAdminService;
import com.pms.common.pojo.Result;
import com.pms.common.utils.HttpClientUtils;

import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AdminServiceImpl implements IAdminService{
	
	/**
     * 注入 JwtProperties 属性配置类
     
    @Autowired
    private JwtProperties jwtProperties;
*/
	/**
     * 获取access_token
     * @return
     */
	@Override
	public Result<?> getAuthAccessToken() {
		try {
			String url= "http://os.com:9000/pms-auth-server/oauth2/token";
			String result = HttpClientUtils.getAuthAccessToken(url);
			if(null != result) {
				Map<String,Object> json = Convert.toMap(String.class, Object.class, JSONUtil.parse(result));
				if(json.containsKey("access_token")) {
					return Result.success(json);
				}
			}
		} catch (Exception e) {
			log.error("结果转换异常",e.getMessage());
		}
		return Result.failed("获取local_access_token失败");
	}

	/**
     * 注销access_token
     * @return
     */
	@Override
	public Result<?> revokeAccessToken(String accessToken) {
		try {
			String url= "http://os.com:9000/pms-auth-server/oauth2/revoke";
			String result = HttpClientUtils.revokeAccessToken(url,accessToken);
			if(null != result) {
				Map<String,Object> json = Convert.toMap(String.class, Object.class, JSONUtil.parse(result));
				if(json.containsKey("access_token")) {
					return Result.success(json);
				}
			}
		} catch (Exception e) {
			log.error("结果转换异常",e.getMessage());
		}
		return Result.failed("注销access_token失败");
	}

}
