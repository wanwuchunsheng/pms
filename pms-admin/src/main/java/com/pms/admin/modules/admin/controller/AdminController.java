package com.pms.admin.modules.admin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pms.admin.config.constant.CommonConstant;
import com.pms.admin.modules.admin.service.ISysUserInfoService;
import com.pms.common.constant.Constants;
import com.pms.common.pojo.Result;
import com.pms.common.pojo.SysPermission;
import com.pms.security.pojo.AdminUserDetails;
import com.pms.security.service.DynamicSecurityService;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
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
@Validated
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
    @ApiOperation(value = "授权码模式获取本地accessToken接口", notes = "授权码模式获取本地accessToken")
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
    @ApiOperation(value = "获取资源权限接口", notes = "登录成功，获取资源权限接口")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "query", name = "environmentType", value = "环境类型（1.web环境权限资源  2.app权限资源）", dataTypeClass = Integer.class,  required = true)
    })
    public Result<?> getAccessToken(HttpServletRequest request,@NotNull(message = "环境类型不能为空") Integer environmentType){
    	try {
    		//1、获取access_token
            String authorization = request.getHeader(Constants.AUTHORIZATION);
            if(ObjectUtil.isNotEmpty(authorization)) {
            	 //获取用户信息
                String accessToken = authorization.substring(Constants.BEARER.length());
                //2、获取登录用户可访问资源权限
                AdminUserDetails user = dynamicSecurityService.loadDataSource(accessToken);
                //3、查询用户权限
                List<SysPermission> resList = sysUserInfoService.getPermissionList(user.getPmsUserInfo().getId(),Convert.toStr(environmentType));
                //4、按钮权限（用户拥有的权限集合）
                List<String> codeList = resList.stream()
                        .filter((permission) -> CommonConstant.MENU_TYPE_2.equals(permission.getMenuType()) && CommonConstant.STATUS_1.equals(permission.getStatus()))
                        .collect(ArrayList::new, (list, permission) -> list.add(permission.getPerms()), ArrayList::addAll);
                //
    			JSONArray authArray = new JSONArray();
    			this.getAuthJsonArray(authArray, resList);
    			List<SysPermission> allAuthList = sysUserInfoService.selectSysPermissionByAll(Convert.toStr(environmentType));
    			JSONArray allAuthArray = new JSONArray();
    			this.getAllAuthJsonArray(allAuthArray, allAuthList);
    			Map<String, Object> result = new HashMap<>();
                //所拥有的权限编码
    			result.put("codeList", codeList);
    			//按钮权限（用户拥有的权限集合）
    			result.put("auth", authArray);
    			//全部权限配置集合（按钮权限，访问权限）
    			result.put("allAuth", allAuthArray);
                // 系统安全模式
    			result.put("sysSafeMode", false);
                return Result.success(result);
            }
		} catch (Exception e) {
			log.error("获取资源权限异常,{}",e.getMessage());
		}
        return Result.failed(HttpStatus.HTTP_UNAUTHORIZED,"身份验证不通过");
    }
    
    /**
	  *  获取权限JSON数组
	 * @param jsonArray
	 * @param allList
	 */
	private void getAllAuthJsonArray(JSONArray jsonArray,List<SysPermission> allList) {
		JSONObject json = null;
		for (SysPermission permission : allList) {
			json = new JSONObject();
			json.append("action", permission.getPerms());
			json.append("status", permission.getStatus());
			//1显示2禁用
			json.append("type", permission.getPermsType());
			json.append("describe", permission.getName());
			jsonArray.add(json);
		}
	}
    
    /**
	  *  获取权限JSON数组
	 * @param jsonArray
	 * @param metaList
	 */
	private void getAuthJsonArray(JSONArray jsonArray,List<SysPermission> metaList) {
		for (SysPermission permission : metaList) {
			if(permission.getMenuType()==null) {
				continue;
			}
			JSONObject json = null;
			if(permission.getMenuType().equals(CommonConstant.MENU_TYPE_2) &&CommonConstant.STATUS_1.equals(permission.getStatus())) {
				json = new JSONObject();
				json.append("action", permission.getPerms());
				json.append("type", permission.getPermsType());
				json.append("describe", permission.getName());
				jsonArray.add(json);
			}
		}
	}
    
}