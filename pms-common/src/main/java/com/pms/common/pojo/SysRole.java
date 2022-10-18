package com.pms.common.pojo;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
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
	private Date updateTime;
	private Long updateUserId;
	private String updateUserName;
}
