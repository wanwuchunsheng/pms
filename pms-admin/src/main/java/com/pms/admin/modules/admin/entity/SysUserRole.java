package com.pms.admin.modules.admin.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@TableName("sys_user_role")
public class SysUserRole implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@TableId(type = IdType.AUTO) 
	private long id;
	private long userId;
	private long roleId;
	private long tenantId;
	

}
