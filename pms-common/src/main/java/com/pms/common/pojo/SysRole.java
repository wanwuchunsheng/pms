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
	private long id;
	private String roleCode;
	private String roleName;
	private int sort;
	private int enable;
	private String remark;
	private long tenantId;
	private Date updateTime;
	private long updateUserId;
	private String updateUserName;
}
