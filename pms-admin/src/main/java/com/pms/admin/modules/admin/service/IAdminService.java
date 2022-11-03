package com.pms.admin.modules.admin.service;

import com.pms.common.pojo.Result;

public interface IAdminService {

	Result<?> getAuthAccessToken();

	Result<?> revokeAccessToken(String accessToken);

	Result<?> getDingtalkOAuth(String authCode);
	
	
}
