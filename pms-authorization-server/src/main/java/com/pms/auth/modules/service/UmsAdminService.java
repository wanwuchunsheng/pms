package com.pms.auth.modules.service;

import java.util.List;

import com.pms.common.pojo.UmsAdmin;
import com.pms.common.pojo.UmsPermission;

public interface UmsAdminService {

	UmsAdmin getAdminByUsername(String username);

	List<UmsPermission> getPermissionList(Long id);

}
