package com.pms.admin.modules.admin.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("sys_role")
public class SysRole implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;
	private String roleCode;
	private String roleName;
	private int sort;
	private int enable;
	private String remark;
	private long tenantId;
	private String updateTime;
	private long updateUserId;
	private String updateUserName;

}
