package com.pms.common.pojo;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @Author scott
 * @since 2018-12-19
 */
@Getter
@Setter
public class SysRole implements Serializable {

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
