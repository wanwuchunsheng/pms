package com.pms.auth.config.security;

import java.security.KeyStore;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ClientSettings;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import cn.hutool.core.lang.UUID;
import lombok.SneakyThrows;

/**
 * <p>授权服务配置</p>
 *
 * @author wch
 * Date 2022/5/10
 * @version 1.0
 * 
 *  /oauth2/revoke  注销请求
 *  
 * 
 */
@Configuration(proxyBeanMethods = false)
public class AuthorizationServerConfiguration {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	/**
     * 授权配置	
     * // @Order 表示加载优先级；HIGHEST_PRECEDENCE为最高优先级
     *
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        // 定义授权服务配置器
        OAuth2AuthorizationServerConfigurer<HttpSecurity> authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer<>();

        // 获取授权服务器相关的请求端点
        RequestMatcher authorizationServerEndpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();

        http
                // 拦截对 授权服务器 相关端点的请求
                .requestMatcher(authorizationServerEndpointsMatcher)
                // 拦载到的请求需要认证确认（登录）
                .authorizeRequests()
                // 其余所有请求都要认证
                .anyRequest().authenticated()
             .and()
                // 忽略掉相关端点的csrf（跨站请求）：对授权端点的访问可以是跨站的
                .csrf(csrf -> csrf.ignoringRequestMatchers(authorizationServerEndpointsMatcher))
                // 表单登录
                .formLogin()
              .and()
                .logout()
              .and()
                // 应用 授权服务器的配置
                .apply(authorizationServerConfigurer);
        return http.build();
    }

    /**
     * 注册客户端
     *	 id：仅表示客户端在数据库中的这个记录
         client_id：唯一标示客户端；请求token时，以此作为客户端的账号
         client_name：客户端的名称，可以省略
         client_secret：密码
     * @param jdbcTemplate 操作数据库
     * @return 客户端仓库
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        // 1、操作数据库对象,检查当前客户端是否已注册
        JdbcRegisteredClientRepository registeredClientRepository = new JdbcRegisteredClientRepository(jdbcTemplate);
        // 客户端ID
        String clientId_1 = "my_client";
        // 查询客户端是否存在
        RegisteredClient registeredClient_1 = registeredClientRepository.findByClientId(clientId_1);

        // 2、添加客户端
        // 数据库中没有
        if (registeredClient_1 == null) {
            registeredClient_1 = this.createRegisteredClientAuthorizationCode(clientId_1);
            registeredClientRepository.save(registeredClient_1);
        }

        // 3、返回客户端仓库
        return registeredClientRepository;
    }

    /**
     * 定义客户端（令牌申请方式：授权码模式）
     * @param clientId 客户端ID
     * @return
     */
    private RegisteredClient createRegisteredClientAuthorizationCode(final String clientId) {
        // JWT（Json Web Token）的配置项：TTL、是否复用refrechToken等等
        TokenSettings tokenSettings = TokenSettings.builder()
                // 令牌存活时间：2小时
                .accessTokenTimeToLive(Duration.ofHours(8))
                // 令牌可以刷新，重新获取
                .reuseRefreshTokens(true)
                // 刷新时间：30天（30天内当令牌过期时，可以用刷新令牌重新申请新令牌，不需要再认证）
                .refreshTokenTimeToLive(Duration.ofDays(30))
                .build();
        // 客户端相关配置
        ClientSettings clientSettings = ClientSettings.builder()
                // 是否需要用户授权确认
                .requireAuthorizationConsent(false)
                .build();

        return RegisteredClient
                // 客户端ID和密码
                .withId(UUID.randomUUID().toString())
                //.withId(id)
                .clientId(clientId)
                .clientSecret(this.passwordEncoder.encode("123456"))
                // 客户端名称：可省略
                .clientName("授权服务中心")
                // 授权方法
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                // 密码模式 - 已废弃
                //.authorizationGrantType(AuthorizationGrantType.PASSWORD)
                // 授权模式
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                // 客户端模式
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                // 刷新令牌（授权码模式）
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                /* 回调地址：
                 * 授权服务器向当前客户端响应时调用下面地址；
                 * 不在此列的地址将被拒统；
                 * 只能使用IP或域名，不能使用localhost
                 */
                .redirectUri("http://127.0.0.1:8000/login/oauth2/code/myClient")
                .redirectUri("http://127.0.0.1:8000")
                // 授权范围（当前客户端的授权范围）
                .scope(OidcScopes.OPENID)
                // JWT（Json Web Token）配置项
                .tokenSettings(tokenSettings)
                // 客户端配置项
                .clientSettings(clientSettings)
                .build();
    }

    /**
     * 令牌的发放记录
     *
     * @param jdbcTemplate               操作数据库
     * @param registeredClientRepository 客户端仓库
     * @return 授权服务
    */
    @Bean
    public OAuth2AuthorizationService auth2AuthorizationService(JdbcTemplate jdbcTemplate,
            RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
    }
	
    /**
     * 把资源拥有者授权确认操作保存到数据库
     * 资源拥有者（Resource Owner）对客户端的授权记录
     *
     * @param jdbcTemplate               操作数据库
     * @param registeredClientRepository 客户端仓库
     * @return
     */
    @Bean
    public OAuth2AuthorizationConsentService auth2AuthorizationConsentService(
            JdbcTemplate jdbcTemplate,
            RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
    }
    

    /**
     * 加载jwk资源
     * 用于生成令牌
     * @return
     */
    @SneakyThrows
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        // 证书的路径
        String path = "myjks.jks";
        // 证书别名
        String alias = "myjks";
        // keystore 密码
        String pass = "123456";

        ClassPathResource resource = new ClassPathResource(path);
        KeyStore jks = KeyStore.getInstance("jks");
        char[] pin = pass.toCharArray();
        jks.load(resource.getInputStream(), pin);
        RSAKey rsaKey = RSAKey.load(jks, alias, pin);

        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }


    /**
     * <p>授权服务器元信息配置</p>
     * <p>
     * 授权服务器本身也提供了一个配置工具来配置其元信息，大多数都使用默认配置即可，唯一需要配置的其实只有授权服务器的地址issuer
     * 在生产中这个地方应该配置为域名
     *
     * @return
     */
    @Bean
    public ProviderSettings providerSettings() {
        return ProviderSettings.builder().issuer("http://os.com:9000/pms-auth-server").build();
    }

}
