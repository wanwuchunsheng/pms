package com.pms.admin.modules.admin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pms.admin.modules.admin.service.ISysUserInfoService;
import com.pms.common.constant.Constants;
import com.pms.common.pojo.Result;
import com.pms.security.pojo.AdminUserDetails;
import com.pms.security.service.DynamicSecurityService;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpStatus;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

/**
 * <p>用户信息接口</p>
 *
 * @author WCH
 * Date 2022/5/10
 * @version 1.0
 */
@RestController
@RequestMapping("/admin")
@Log4j2
public class AdminController {
	
	@Autowired
	private ISysUserInfoService  sysUserInfoService;
	
	@Autowired
    private DynamicSecurityService dynamicSecurityService;
	
	/**
     * 获取access_token
     * @return
     */
    @GetMapping("/login")
    @ApiOperation(value = "授权码模式获取accessToken接口", notes = "授权码模式获取accessToken")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "query", name = "code", value = "授权码", dataTypeClass = Long.class,  required = true)
    })
    public Result<?> Login(@NotBlank(message = "用户Id不能为空") String code){
    	
        return Result.success("用户名密码登录还未实现，请使用钉钉扫码登录");
    }

    /**
     * 获取access_token
     * @return
     */
    @GetMapping("/getPermission")
    @ApiOperation(value = "获取资源权限接口", notes = "获取资源权限")
    public Result<?> getAccessToken(HttpServletRequest request){
    	try {
    		//1、获取access_token
            String authorization = request.getHeader(Constants.AUTHORIZATION);
            if(ObjectUtil.isNotEmpty(authorization)) {
            	 //获取用户信息
                String accessToken = authorization.substring(Constants.BEARER.length());
                //2、获取登录用户可访问资源权限
                AdminUserDetails user = dynamicSecurityService.loadDataSource(accessToken);
        		return Result.success(user.getResouceList());
            }
		} catch (Exception e) {
			log.error("获取资源权限信息异常，{}",e.getMessage());
		}
        return Result.failed(HttpStatus.HTTP_UNAUTHORIZED,"身份验证不通过");
    }
    
    
}