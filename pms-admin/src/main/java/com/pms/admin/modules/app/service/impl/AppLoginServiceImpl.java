package com.pms.admin.modules.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiSnsGetuserinfoBycodeRequest;
import com.dingtalk.api.request.OapiUserGetbyunionidRequest;
import com.dingtalk.api.request.OapiUserGetuserinfoRequest;
import com.dingtalk.api.request.OapiV2UserGetRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiSnsGetuserinfoBycodeResponse;
import com.dingtalk.api.response.OapiUserGetbyunionidResponse;
import com.dingtalk.api.response.OapiUserGetuserinfoResponse;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.pms.admin.config.dingtalk.DingTalkConfig;
import com.pms.admin.modules.app.service.IAppLoginService;
import com.pms.common.pojo.Result;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AppLoginServiceImpl implements IAppLoginService{
	
	@Autowired
	private DingTalkConfig dingtalkConfig;

	/**
	 * 企业内部微应用，
	 *   小程序获取accessToken
	 * @author WCH
	 * 
	 * */
	@Override
	public Result<?> getDingtalkAccessToken() {
		try {
			DingTalkClient clientDingTalkClient = new DefaultDingTalkClient(dingtalkConfig.getGetTokenUrl());
	        OapiGettokenRequest request = new OapiGettokenRequest();
	        // 填写步骤一创建应用的Appkey
	        request.setAppkey(dingtalkConfig.getAppKey());
	        // 填写步骤一创建应用的Appsecret
	        request.setAppsecret(dingtalkConfig.getAppKeySecret());
	        request.setHttpMethod("GET");
	        OapiGettokenResponse response =clientDingTalkClient.execute(request);
	        if(response.getErrcode() == 0){
	            return Result.success(response.getAccessToken());
	        }
	        return Result.failed("APP登录获取钉钉accessToken失败，请检查参数配置");
		} catch (Exception e) {
			log.error("APP登录获取钉钉accessToken失败:{}-{}",e.getMessage(),e);
		}
		return Result.failed("APP登录获取钉钉accessToken异常");
	}

	/**
	 * 企业内部微应用，
	 *   小程序通过accessToken，code获取用户ID
	 * @author WCH
	 * 
	 * */
	@Override
	public Result<?> getDingtalkUserId(String accessToken, String code) {
		try {
			// 通过临时授权码获取授权用户的个人信息
	        DefaultDingTalkClient client = new DefaultDingTalkClient(dingtalkConfig.getGetAppUserInfoUrl());
	        OapiUserGetuserinfoRequest request = new OapiUserGetuserinfoRequest();
	        request.setCode(code);
			request.setHttpMethod("GET");
			OapiUserGetuserinfoResponse response = client.execute(request, accessToken);
			if(response.getErrcode() != 0) {
				return Result.failed("APP登录获取userid失败，请检查配置参数");
			}
			return Result.success(response.getUserid());
		} catch (Exception e) {
			log.error("APP登录获取userid异常,access_token:{} code:{}-异常信息：{}",accessToken,code,e);
		}
		return Result.failed("APP登录获取userid异常");
	}

	/**
	 * 企业内部微应用，
	 *   小程序通过accessToken，userId获取用户详细信息
	 * @author WCH
	 * 
	 * */
	@Override
	public Result<?> getDingtalkUserInfo(String accessToken, String userId) {
		try {
			DingTalkClient client = new DefaultDingTalkClient(dingtalkConfig.getGetUserByUserIdUrl());
	        OapiV2UserGetRequest req = new OapiV2UserGetRequest();
	        req.setUserid(userId);
	        req.setLanguage("zh_CN");
	        OapiV2UserGetResponse rsp = client.execute(req, accessToken);
	        if( rsp.getErrcode()!=0) {
	        	return Result.failed(rsp.getErrmsg());
	        }
	        return Result.success(rsp.getResult());
		} catch (Exception e) {
			log.error("APP获取钉钉用户详细信息失败,access_token:{}-userid:{}-异常信息：{}",accessToken,userId,e);
		}
		return Result.failed("APP获取钉钉登录用户信息异常");
	}

}
