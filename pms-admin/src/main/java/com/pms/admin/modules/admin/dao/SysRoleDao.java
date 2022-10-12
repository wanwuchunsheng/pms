package com.pms.admin.modules.admin.dao;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pms.admin.modules.admin.entity.SysRole;
/**
 * @Date: 2019-11-27 11:29:02
 * @Description: 持久层接口
 * @author WCH
 */
@Repository
public interface SysRoleDao extends BaseMapper<SysRole> {

	/**
	 * 说明：根据用户id,查询所有角色
	 * @author WCH
	 * 
	 * */
	List<com.pms.common.pojo.SysRole> getRoleList(@Param("id") Long id);


}


