package com.pms.auth.modules.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pms.auth.modules.dao.SysPermissionDao;
import com.pms.auth.modules.dao.SysUserInfoDao;
import com.pms.auth.modules.service.UmsAdminService;
import com.pms.common.pojo.SysPermission;
import com.pms.common.pojo.SysUserInfo;

@Service
public class UmsAddminServiceImpl implements UmsAdminService{
	
	@Autowired
	private SysUserInfoDao sysUserInfoDao;
	
	@Autowired
	private SysPermissionDao sysResouceDao;

	@Override
	public SysUserInfo getAdminByUsername(String username) {
		QueryWrapper<SysUserInfo> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_code", username);
		SysUserInfo bean = sysUserInfoDao.selectOne(queryWrapper);
		return bean;
	}

	@Override
	public List<SysPermission> getPermissionList(Long id) {
		QueryWrapper<SysPermission> queryWrapper = new QueryWrapper<>();
		List<SysPermission> upList = sysResouceDao.selectList(queryWrapper);
		return upList;
	}

	


}
