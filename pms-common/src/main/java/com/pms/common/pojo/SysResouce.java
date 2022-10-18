package com.pms.common.pojo;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysResouce implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
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
	 * 权限
	 */
	private String permission;
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
	 * 是否启用 0启用1禁用
	 */
	private Integer enable;
	/**
	 * 租户扩展字段
	 */
	private Long tenantId;
	private Date updateTime;
	private Long updateUserId;
	private String updateUserName;

}
