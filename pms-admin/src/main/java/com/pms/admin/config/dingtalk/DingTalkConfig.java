package com.pms.admin.config.dingtalk;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
/**
 * 钉钉相关配置
 * @author WCH
 * */
@Component
@Getter
public class DingTalkConfig {
	
	@Value("${dingtalk.appId}")
    private String appId;

    @Value("${dingtalk.appSecret}")
    private String appSecret;
	
    @Value("${dingtalk.appKey}")
    private String appKey;

    @Value("${dingtalk.appKeySecret}")
    private String appKeySecret;

    @Value("${dingtalk.getTokenUrl}")
    private String getTokenUrl;

    @Value("${dingtalk.getUserInfoUrl}")
    private String getUserInfoUrl;
    
    @Value("${dingtalk.getAppUserInfoUrl}")
    private String getAppUserInfoUrl;

    @Value("${dingtalk.getUnionIdUrl}")
    private String getUnionIdUrl;

    @Value("${dingtalk.getUserByUserIdUrl}")
    private String getUserByUserIdUrl;

}
