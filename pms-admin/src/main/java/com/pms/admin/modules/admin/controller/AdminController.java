package com.pms.admin.modules.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pms.admin.modules.admin.dao.SysUserRoleDao;
import com.pms.admin.modules.admin.service.IAdminService;
import com.pms.admin.modules.admin.service.ISysUserInfoService;
import com.pms.common.pojo.Result;
import com.pms.common.pojo.SysUserInfo;

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
	private IAdminService  adminService;
	
	@Autowired
	private ISysUserInfoService  sysUserInfoService;
	
	
	/**
     * 获取access_token
     * @return
     */
    @GetMapping("/login")
    @ApiOperation(value = "授权码模式获取accessToken接口", notes = "授权码模式获取accessToken")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "query", name = "code", value = "授权码", dataType = "String",required = true)
    })
    public Result<?> Login(String code){
        return Result.success(code);
    }

    /**
     * 获取access_token
     * @return
     */
    @GetMapping("/getAccessToken")
    @ApiOperation(value = "获取access_token接口", notes = "获取access_token")
    public Result<?> getAccessToken(){
        return adminService.getAuthAccessToken();
    }
    
    /**
     * 注销access_token
     * @return
     */
    @GetMapping("/revokeAccessToken")
    @ApiOperation(value = "注销access_token接口", notes = "注销access_token")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "query", name = "accessToken", value = "本地access_token", dataType = "String",required = true)
    })
    public Result<?> revokeAccessToken(String accessToken){
        return adminService.revokeAccessToken(accessToken);
    }
    
    /**
     * 查询用户信息
     * @return
     */
    @GetMapping("/queryUserInfo")
    @ApiOperation(value = "查询用户信息接口", notes = "查询用户信息")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "query", name = "userId", value = "用户Id", dataType = "String")
    })
    public Result<?> queryUserInfoByUserId(String userId){
        return sysUserInfoService.queryUserInfoByUserId(userId);
    }
    
}