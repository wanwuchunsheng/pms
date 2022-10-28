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

	private Long id;
	private String roleCode;
	private String roleName;
	private Integer sort;
	private Integer enable;
	private String remark;
	private Long tenantId;
	private String updateTime;
	private Long updateUserId;
	private String updateUserName;

}
