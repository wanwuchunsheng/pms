package com.pms.admin.modules.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dingtalk.api.response.OapiV2UserGetResponse.UserGetResponse;
import com.pms.admin.modules.admin.dao.SysResouceDao;
import com.pms.admin.modules.admin.dao.SysRoleDao;
import com.pms.admin.modules.admin.dao.SysUserInfoDao;
import com.pms.admin.modules.admin.dao.SysUserRoleDao;
import com.pms.admin.modules.admin.entity.SysRole;
import com.pms.admin.modules.admin.entity.SysUserRole;
import com.pms.admin.modules.admin.service.ISysUserInfoService;
import com.pms.common.pojo.Result;
import com.pms.common.pojo.SysResouce;
import com.pms.common.pojo.SysUserInfo;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SysUserInfoServiceImpl implements ISysUserInfoService{
	
	@Autowired
	private SysUserInfoDao sysUserInfoDao;
	
	@Autowired
	private SysResouceDao sysResouceDao;
	
	@Autowired
	private SysRoleDao sysRoleDao;
	
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	
	@Autowired
	public PasswordEncoder passwordEncoder;

	@Override
	public Result<?> updateUserInfoByMobile(UserGetResponse dingtalkUser) {
		try {
			sysUserInfoDao.updateSysUserInfoByUserId(dingtalkUser.getMobile(),dingtalkUser.getUserid(),dingtalkUser.getUnionid(),JSONUtil.toJsonStr(dingtalkUser.getDeptIdList()));
			return Result.success(null);
		} catch (Exception e) {
			log.error("绑定钉钉相关信息异常，{}",e.getMessage());
		}
		return Result.failed("绑定钉钉相关信息异常");
	}
	
	@Override
	public SysUserInfo getAdminByUsername(String mobile, String dingtalkUserId) {
		try {
			QueryWrapper<SysUserInfo> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("mobile", mobile);
			queryWrapper.eq("dingtalk_user_id", dingtalkUserId);
			List<SysUserInfo> list = sysUserInfoDao.selectList(queryWrapper);
			if(list!=null && list.size()>0) {
				return list.get(0);
			}
		} catch (Exception e) {
			log.error("钉钉手机号码查询用户信息为空{}",e.getMessage());
		}
		return null;
	}

	/**
	 * 查询用户所有权限的资源
	 * @author WCH
	 * @datetime 2022-10-9 13:42:21
	 * 
	 * */
	@Override
	public List<SysResouce> getPermissionList(Long userId) {
		return sysResouceDao.getPermissionList(userId);
	}

	/**
	 * 钉钉扫码登录
	 *   发现该用户管理系统未注册，给默认注册
	 * 
	 * 
	 * */
	@Override
	public SysUserInfo saveUserInfo(UserGetResponse dingtalkUser) {
		SysUserInfo sui = new SysUserInfo();
		sui.setDingtalkDeptId(Convert.toStr(dingtalkUser.getDeptIdList()) );
		sui.setDingtalkUnionId(dingtalkUser.getUnionid());
		sui.setDingtalkUserId(dingtalkUser.getUserid());
		sui.setUserCode(dingtalkUser.getName());
		sui.setUserName(dingtalkUser.getName());
		sui.setAvatar(dingtalkUser.getAvatar());
		sui.setEmail(dingtalkUser.getEmail());
		sui.setMobile(dingtalkUser.getMobile());
		sui.setSex(2);
		sui.setRemark(dingtalkUser.getRemark());
		sui.setEnable(0);
		sui.setPwd(passwordEncoder.encode("123456"));
		sysUserInfoDao.insert(sui);
		return sui;
	}

	/**
	 * 钉钉扫码登录
	 *   发现该用户管理系统未注册，给默认注册，并绑定默认角色（游览角色>id=1）
	 * 
	 * 
	 * */
	@Override
	public SysRole saveRole(SysUserInfo sui) {
		QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("role_code", "DefualtRole");
		SysRole sr = sysRoleDao.selectOne(queryWrapper);
		if(ObjectUtil.isNotEmpty(sr)) {
			SysUserRole sur = new SysUserRole();
			sur.setRoleId(sr.getId());
			sur.setUserId(sui.getId());
			sysUserRoleDao.insert(sur);
		}
		return sr;
	}

	/**
	 * 说明：根据用户id,查询所有角色
	 * @author WCH
	 * 
	 * */
	@Override
	public List<com.pms.common.pojo.SysRole> getRoleList(Long id) {
		return sysRoleDao.getRoleList(id);
	}

	/**
     * 查询用户信息
     * @return
     */
	@Override
	public Result<?> queryUserInfoByUserId(String userId) {
		QueryWrapper<SysUserInfo> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("id", userId);
		return Result.success(sysUserInfoDao.selectOne(queryWrapper));
	}
	

	
	

}
