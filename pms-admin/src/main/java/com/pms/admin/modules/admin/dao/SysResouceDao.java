package com.pms.admin.modules.admin.dao;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pms.common.pojo.SysResouce;
/**
 * @Date: 2019-11-27 11:29:02
 * @Description: 持久层接口
 * @author WCH
 */
@Repository
public interface SysResouceDao extends BaseMapper<SysResouce> {

	/**
	 * 查询用户所有权限的资源
	 * @author WCH
	 * @datetime 2022-10-9 13:42:21
	 * 
	 * */
	List<SysResouce> getPermissionList(@Param("userId") Long userId);


}


