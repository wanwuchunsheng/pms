package com.pms.auth.modules.service;

import java.util.List;

import com.pms.common.pojo.SysPermission;
import com.pms.common.pojo.SysUserInfo;

public interface UmsAdminService {

	SysUserInfo getAdminByUsername(String username);

	List<SysPermission> getPermissionList(Long id);

}
