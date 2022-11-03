package com.pms.admin.modules.admin.service;

import java.util.List;

import com.dingtalk.api.response.OapiV2UserGetResponse.UserGetResponse;
import com.pms.admin.modules.admin.entity.SysRole;
import com.pms.common.pojo.Result;
import com.pms.common.pojo.SysUserInfo;
import com.pms.common.pojo.SysPermission;

public interface ISysUserInfoService {

	Result<?> updateUserInfoByMobile(UserGetResponse dingtalkUser);

	SysUserInfo getAdminByUsername(String mobile,String dingtalkUserId);

	List<SysPermission> getPermissionList(Long userId, String propertyType);

	SysUserInfo saveUserInfo(UserGetResponse dingtalkUser);

	SysRole saveRole(SysUserInfo sui);

	List<com.pms.common.pojo.SysRole> getRoleList(Long id);

	/**
     * 查询用户信息
     * @return
     */
	Result<?> queryUserInfoByUserId(String userId);

	List<SysPermission> selectSysPermissionByAll(String environmentType);

	

}
