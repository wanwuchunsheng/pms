package com.pms.admin.modules.admin.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aliyun.dingtalkoauth2_1_0.Client;
import com.aliyun.dingtalkoauth2_1_0.models.GetUserTokenRequest;
import com.aliyun.dingtalkoauth2_1_0.models.GetUserTokenResponse;
import com.aliyun.teaopenapi.models.Config;
import com.pms.admin.config.dingtalk.DingTalkConfig;
import com.pms.admin.modules.admin.service.IAdminService;
import com.pms.common.pojo.Result;
import com.pms.common.utils.HttpClientUtils;
import com.pms.security.config.utils.JwtProperties;

import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class AdminServiceImpl implements IAdminService{
	
	/**
     * 注入 JwtProperties 属性配置类
     */
    @Autowired
    private JwtProperties jwtProperties;
    
    @Autowired
    private DingTalkConfig dingTalkConfig;
	
	/**
     * 获取access_token
     *    本地oauth2.0账号密码登录
     * @return
     */
	@Override
	public Result<?> getAuthAccessToken() {
		try {
			String url= this.jwtProperties.getClaims().getIssuer()+"/oauth2/token";
			String result = HttpClientUtils.getAuthAccessToken(url);
			if(null != result) {
				Map<String,Object> json = Convert.toMap(String.class, Object.class, JSONUtil.parse(result));
				if(json.containsKey("access_token")) {
					return Result.success(json);
				}
			}
		} catch (Exception e) {
			log.error("获取local_access_token失败,{}",e.getMessage());
		}
		return Result.failed("获取local_access_token失败");
	}

	/**
     * 注销access_token
     * 	本地oauth2.0注销登录
     * @return
     */
	@Override
	public Result<?> revokeAccessToken(String accessToken) {
		try {
			String url= this.jwtProperties.getClaims().getIssuer()+"/oauth2/revoke";
			String result = HttpClientUtils.revokeAccessToken(url,accessToken);
			if(null != result) {
				Map<String,Object> json = Convert.toMap(String.class, Object.class, JSONUtil.parse(result));
				if(json.containsKey("access_token")) {
					return Result.success(json);
				}
			}
		} catch (Exception e) {
			log.error("注销access_token失败,{}",e.getMessage());
		}
		return Result.failed("注销access_token失败");
	}

	/**
     * 获取access_token， oauth2.0方式登录
     *   钉钉oauth2.0账号密码登录
     * @return
     */
	@Override
	public Result<?> getDingtalkOAuth(String authCode) {
		try {
			Config config = new Config();
	        config.protocol = "https";
	        config.regionId = "central";
	        Client client = new Client(config);
	        GetUserTokenRequest getUserTokenRequest = new GetUserTokenRequest()
	                .setClientSecret(dingTalkConfig.getAppSecret())
	                .setClientId(dingTalkConfig.getAppId())
	                .setCode(authCode)
	                .setGrantType("authorization_code");
        	GetUserTokenResponse gtRespose =client.getUserToken(getUserTokenRequest);
        	log.info("登录成功，信息：{}", JSONUtil.parse(gtRespose.getBody()));
        	return Result.success(gtRespose.getBody().accessToken);
		} catch (Exception e) {
			log.error("获取钉钉access_token失败,authCode={},异常信息：{}",e.getMessage());
		}
		return Result.failed("获取钉钉access_token失败，code过期或参数配置错误");
	}

	
}
