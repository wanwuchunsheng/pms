package com.pms.auth.modules.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pms.auth.modules.dao.UmsAdminDao;
import com.pms.auth.modules.dao.UmsPermissionDao;
import com.pms.common.pojo.UmsAdmin;
import com.pms.common.pojo.UmsPermission;

@Service
public class UmsAddminServiceImpl implements UmsAdminService{
	
	@Autowired
	private UmsAdminDao umsAdminDao;
	
	@Autowired
	private UmsPermissionDao umsPermissionDao;

	@Override
	public UmsAdmin getAdminByUsername(String username) {
		QueryWrapper<UmsAdmin> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("username", username);
		UmsAdmin bean = umsAdminDao.selectOne(queryWrapper);
		return bean;
	}

	@Override
	public List<UmsPermission> getPermissionList(Long id) {
		QueryWrapper<UmsPermission> queryWrapper = new QueryWrapper<>();
		List<UmsPermission> upList = umsPermissionDao.selectList(queryWrapper);
		return upList;
	}

	


}
