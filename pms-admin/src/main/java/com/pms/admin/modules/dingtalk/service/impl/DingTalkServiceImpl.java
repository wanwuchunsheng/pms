package com.pms.admin.modules.dingtalk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiSnsGetuserinfoBycodeRequest;
import com.dingtalk.api.request.OapiUserGetbyunionidRequest;
import com.dingtalk.api.request.OapiV2UserGetRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiSnsGetuserinfoBycodeResponse;
import com.dingtalk.api.response.OapiUserGetbyunionidResponse;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.pms.admin.config.dingtalk.DingTalkConfig;
import com.pms.admin.modules.dingtalk.service.IDingTalkService;
import com.pms.common.pojo.Result;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DingTalkServiceImpl implements IDingTalkService{
	
	@Autowired
	private DingTalkConfig dingtalkConfig;

   /**
     * @method  获取accesstoken
     * @author WCH
     * @datetime 2022-9-26 15:31:15
     * @return 钉钉accessToken
    */
	@Override
	public Result<?> getDingtalkAccessToken() {
		try {
			DingTalkClient clientDingTalkClient = new DefaultDingTalkClient(dingtalkConfig.getGetTokenUrl());
	        OapiGettokenRequest request = new OapiGettokenRequest();
	        // 填写步骤一创建应用的Appkey
	        request.setAppkey(dingtalkConfig.getAppId());
	        // 填写步骤一创建应用的Appsecret
	        request.setAppsecret(dingtalkConfig.getAppSecret());
	        request.setHttpMethod("GET");
	        OapiGettokenResponse response =clientDingTalkClient.execute(request);
	        if(response.getErrcode() == 0){
	            return Result.success(response.getAccessToken());
	        }
	        return Result.failed("获取钉钉accessToken失败，请检查参数配置");
		} catch (Exception e) {
			log.error("获取钉钉accessToken失败:{}-{}",e.getMessage(),e);
		}
		return Result.failed("获取钉钉accessToken异常");
	}

	/**
     * 说明：获取钉钉unionid
     * @author WCH
     * @param accessToken,code
     * @datetime 2022-9-26 15:31:15
     * @return 钉钉unionid
    */
	@Override
	public Result<?> getDingtalkUserId(String accessToken, String code) {
		try {
			// 通过临时授权码获取授权用户的个人信息
	        DefaultDingTalkClient client2 = new DefaultDingTalkClient(dingtalkConfig.getGetUserInfoUrl());
	        OapiSnsGetuserinfoBycodeRequest reqBycodeRequest = new OapiSnsGetuserinfoBycodeRequest();
	        // 通过扫描二维码，跳转指定的redirect_uri后，向url中追加的code临时授权码
	        reqBycodeRequest.setTmpAuthCode(code);
	        OapiSnsGetuserinfoBycodeResponse bycodeResponse = client2.execute( reqBycodeRequest, dingtalkConfig.getAppId(), dingtalkConfig.getAppSecret());
	        if(bycodeResponse.getErrcode() != 0){
	            return Result.failed(bycodeResponse.getErrmsg());
	        }
	        // 根据unionid获取userid
            String unionid = bycodeResponse.getUserInfo().getUnionid();
            DingTalkClient clientDingTalkClient = new DefaultDingTalkClient(dingtalkConfig.getGetUnionIdUrl());
            OapiUserGetbyunionidRequest reqGetbyunionidRequest = new OapiUserGetbyunionidRequest();
            reqGetbyunionidRequest.setUnionid(unionid);
            OapiUserGetbyunionidResponse oapiUserGetbyunionidResponse = clientDingTalkClient.execute(reqGetbyunionidRequest, accessToken);
            if (oapiUserGetbyunionidResponse.getErrcode() != 0) {
            	//异常返回提醒
                return Result.failed(oapiUserGetbyunionidResponse.getErrmsg());
            }
            // 根据userId获取用户信息
            String userid = oapiUserGetbyunionidResponse.getResult().getUserid();
            return Result.success(userid);
		} catch (Exception e) {
			log.error("获取钉钉userid失败,access_token:{}-code:{}-异常信息：{}",accessToken,code,e);
		}
		return Result.failed("获取钉钉userid异常");
	}

	/**
     * 说明：获取钉钉用户详细信息
     * @author WCH
     * @param userid
     * @datetime 2022-9-26 15:31:15
    */
	@Override
	public Result<?> getDingtalkUserInfo(String accessToken,String userid) {
		try {
			DingTalkClient clientDingTalkClient2 = new DefaultDingTalkClient(dingtalkConfig.getGetUserByUserIdUrl());
	        OapiV2UserGetRequest reqGetRequest = new OapiV2UserGetRequest();
	        reqGetRequest.setUserid(userid);
	        reqGetRequest.setLanguage("zh_CN");
	        OapiV2UserGetResponse rspGetResponse = clientDingTalkClient2.execute(reqGetRequest, accessToken);
	        if( rspGetResponse.getErrcode()!=0) {
	        	return Result.failed(rspGetResponse.getErrmsg());
	        }
	        return Result.success(rspGetResponse.getResult());
		} catch (Exception e) {
			log.error("获取钉钉用户详细信息失败,access_token:{}-userid:{}-异常信息：{}",accessToken,userid,e);
		}
		return Result.failed("获取钉钉登录用户信息异常");
	}

}
