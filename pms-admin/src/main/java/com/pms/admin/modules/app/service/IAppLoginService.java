package com.pms.admin.modules.app.service;

import com.pms.common.pojo.Result;

public interface IAppLoginService {

	Result<?> getDingtalkAccessToken();

	Result<?> getDingtalkUserId(String accessToken, String code);

	Result<?> getDingtalkUserInfo(String accessToken, String userId);

}
