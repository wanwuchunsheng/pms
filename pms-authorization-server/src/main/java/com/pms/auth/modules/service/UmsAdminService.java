package com.pms.auth.modules.service;

import java.util.List;

import com.pms.common.pojo.SysResouce;
import com.pms.common.pojo.SysUserInfo;

public interface UmsAdminService {

	SysUserInfo getAdminByUsername(String username);

	List<SysResouce> getPermissionList(Long id);

}
