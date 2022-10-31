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
	private Long id;
	private Long uid;
	private Long pid;
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
	 * 路由地址
	 */
	private String routePath;
	/**
	 * 权限
	 */
	private String permission;
	/**
	 * 资源属性
	 * 1.web 2.app
	 */
	private Integer propertyType;
	/**
	 * 资源等级
	 */
	private Integer level;
	/**
	 * 排序
	 */
	private Integer sort;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 租户扩展字段
	 */
	private Long tenantId;
	private Date updateTime;
	private Long updateUserId;
	private String updateUserName;

}
