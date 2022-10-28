package com.pms.common.constant;

/**
 * @Description: 常量
 */
public class Constants {
	
	
	/** oauth2.0 默认配置 */
    public static final String AUTHORIZATION = "authorization";
    public static final String BEARER = "Bearer ";
    public static final String JUSTAUTH ="JUSTAUTH::" ;
    public static final String JUSTRES ="JUSTRES::" ;
    public static final int REDIS_TOKEN_TIMEOUT = 60*24*8 ;
    
    /**钉钉扫码，注册本地默认角色*/
    public static final String DEFAULT_ROLE = "DefualtRole";
    public static final String DEFAULT_PWD = "123456";
    
    /** 登录后，查询资源权限，根据不同环境返回不通权限菜单 */
    public static final String PROPERTYTYPE_WEB = "1";
    public static final String PROPERTYTYPE_APP = "2";
    
}
