package com.pms.auth.modules.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pms.auth.config.redis.IGlobalCache;
import com.pms.auth.modules.service.AdminUserDetails;
import com.pms.common.pojo.Result;

import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import lombok.extern.log4j.Log4j2;

/**
 * <p>用户信息接口</p>
 *
 * @author WCH
 * Date 2022/5/10
 * @version 1.0
 */
@RestController
@RequestMapping("/oauth2")
@Log4j2
public class EndPointController {
	
	@Autowired
    private JwtDecoder jwtDecoder;
	
	@Autowired
	private IGlobalCache globalCache;

    /**
     * 获取用户信息
     * @return
     */
    @GetMapping("/user")
    public Result<?> oauth2UserInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("_______>{}",authentication);
        
        
        return Result.success(authentication);
    }
    
    
    /**
     * 获取用户信息
     * @return
     */
    @GetMapping("/getUserInfo")
    public Result<?> getUserInfo(HttpServletRequest request){
    	String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer")) {
            String authToken = authHeader.substring(7);
            String username = jwtDecoder.decode(authToken).getSubject();
            //redis保存信息
            Object bean = globalCache.get(username);
        	if (bean == null) {
        		Result.failed(HttpStatus.HTTP_UNAUTHORIZED, "认证信息失效，重新登录");
        	}
        	AdminUserDetails userDetails = JSONUtil.toBean(JSONUtil.toJsonStr(bean), AdminUserDetails.class);
        	//赋值权限
            //UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            //authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            //SecurityContextHolder.getContext().setAuthentication(authentication);
        	UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            //authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            return Result.success(authentication);
        }
        return Result.failed();
    }
}