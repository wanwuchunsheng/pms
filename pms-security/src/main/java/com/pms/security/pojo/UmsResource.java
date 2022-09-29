package com.pms.security.pojo;
import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UmsResource implements Serializable {
    private Long id;

    //@ApiModelProperty(value = "创建时间")
    private Date createTime;

    //@ApiModelProperty(value = "资源名称")
    private String name;

    //@ApiModelProperty(value = "资源URL")
    private String url;

    //@ApiModelProperty(value = "描述")
    private String description;

    //@ApiModelProperty(value = "资源分类ID")
    private Long categoryId;

    private static final long serialVersionUID = 1L;

    
}