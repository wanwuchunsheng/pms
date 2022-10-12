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
@TableName("sys_resouce")
public class SysResouce implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.AUTO) 
	private long id;
	private long uid;
	private long pid;
	/**
	 * 资源图标
	 */
	private String icon;
	/**
	 * 资源名称
	 */
	private String resName;
	/**
	 * 资源类型
	 */
	private String resType;
	/**
	 * 资源路径
	 */
	private String resPath;
	/**
	 * 权限
	 */
	private String permission;
	/**
	 * 资源等级
	 */
	private int level;
	/**
	 * 排序
	 */
	private int sort;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 租户扩展字段
	 */
	private long tenantId;
	private Date updateTime;
	private long updateUserId;
	private String updateUserName;

}
