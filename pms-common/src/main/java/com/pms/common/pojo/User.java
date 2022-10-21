package com.pms.common.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User extends SysUserInfo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String accessToken;
	private String dingtalkAccessToken;

}
