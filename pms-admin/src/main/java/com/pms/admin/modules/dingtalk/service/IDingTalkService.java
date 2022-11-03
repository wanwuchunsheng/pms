package com.pms.admin.modules.dingtalk.service;

import com.pms.common.pojo.Result;

public interface IDingTalkService {

	Result<?> getDingtalkAccessToken();

	Result<?> getDingtalkUserId(String accessToken, String code);

	Result<?> getDingtalkUserInfo(String accessToken,String userid);

	Result<?> getDingtalkOAuthUnionId(String accessToken);

	Result<?> getDingtalkOAuthUserInfo(String accessToken, String unionId);

}
