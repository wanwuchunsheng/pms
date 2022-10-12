package com.pms.common.pojo;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysUserInfo implements Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 用户id
	 */
	private Long id;
	/**
	 * 登录名
	 */
	private String userCode;
	/**
	 * 用户名称
	 */
	private String userName;
	
	/**
	 * 用户密码
	 */
	private String pwd;
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
	/**
	 * 邮箱用户id
	 */
	private String email;
	/**
	 * 手机号码
	 */
	private String mobile;
	/**
	 * 电话
	 */
	private String phone;
	/**
	 * 性别
	 */
	private int sex;
	/**
	 * 头像地址
	 */
	private String avatar;
	/**
	 * 住址
	 */
	private String address;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 是否启用 0启用1禁用
	 */
	private int enable;
	/**
	 * 租户id
	 */
	private long tenantId;
	private Date updateTime;
	private long updateUserId;
	private String updateUserName;

}
