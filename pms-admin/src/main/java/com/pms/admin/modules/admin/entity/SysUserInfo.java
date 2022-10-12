package com.pms.admin.modules.admin.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@TableName("sys_user_info")
public class SysUserInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@TableId(type = IdType.AUTO) 
	private long id;
	private String userCode;
	private String userName;
	/**
	 * 钉钉用户id
	 */
	private String dingtalkUserId;
	/**
	 * 钉钉用户id
	 */
	private String dingtalkUnionId;
	/**
	 * 钉钉部门ID
	 */
	private String dingtalkDeptId;
	private String pwd;
	private String email;
	private String mobile;
	private String phone;
	private int sex;
	private String avatar;
	private String address;
	private String remark;
	private int enable;
	private long tenantId;
	private Date updateTime;
	private long updateUserId;
	private String updateUserName;
	

}
