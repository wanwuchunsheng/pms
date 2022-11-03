package com.pms.admin.modules.admin.dao;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pms.admin.modules.admin.entity.SysPermission;
/**
 * @Date: 2019-11-27 11:29:02
 * @Description: 持久层接口
 * @author WCH
 */
@Repository
public interface SysAdminPermissionDao extends BaseMapper<SysPermission> {

	/**
	 * 查询用户所有权限的资源
	 *    登录中获取
	 * @author WCH
	 * @datetime 2022-10-9 13:42:21
	 * 
	 * */
	List<com.pms.common.pojo.SysPermission> getPermissionList(@Param("userId") Long userId,@Param("propertyType") String propertyType);

	/**
	 * 查询所有权限的资源
	 * @author WCH
	 * @datetime 2022-10-9 13:42:21
	 * 
	 * */
	List<com.pms.common.pojo.SysPermission> selectSysPermissionByAll(@Param("environmentType") String environmentType, @Param("menuType") Integer menuType);



}


