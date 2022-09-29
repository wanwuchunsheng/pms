package com.pms.security.config.utils;


import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>属性配置类</p>
 *
 * @author WCH
 * Date 2022/5/11
 * @version 1.0
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    /*
    ======= 配置示例 ======
    # 自定义 jwt 配置
    jwt:
        cert-info:
            # 证书存放位置
            public-key-location: myKey.cer
        claims:
            # 令牌的鉴发方：即授权服务器的地址
            issuer: http://os:9000
    */
    /**
     * 证书信息（内部静态类）
     * 证书存放位置...
     */
    private CertInfo certInfo;

    /**
     * 证书声明（内部静态类）
     * 发证方...
     */
    private Claims claims;

    @Getter
    @Setter
    public static class Claims {
        /**
         * 发证方
         */
        private String issuer;
        /**
         * 有效期
         */
        //private Integer expiresAt;
    }

    @Getter
    @Setter
    public static class CertInfo {
        /**
         * 证书存放位置
         */
        private String publicKeyLocation;
    }
	
}
