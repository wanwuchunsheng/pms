package com.pms.admin.modules.dingtalk.controller;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dingtalk.api.response.OapiV2UserGetResponse.UserGetResponse;
import com.pms.common.pojo.SysPermission;
import com.pms.admin.modules.admin.service.IAdminService;
import com.pms.admin.modules.admin.service.ISysUserInfoService;
import com.pms.admin.modules.dingtalk.service.IDingTalkService;
import com.pms.common.constant.Constants;
import com.pms.common.pojo.Result;
import com.pms.common.pojo.SysUserInfo;
import com.pms.common.redis.IGlobalCache;
import com.pms.security.pojo.AdminUserDetails;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>钉钉扫码登录</p>
 *
 * @author WCH
 * Date 2022/5/10
 * @version 1.0
 */
@RestController
@RequestMapping("/dingtalk")
@Slf4j
@Validated
public class DingTalkController {
	
	@Autowired
	private IDingTalkService dingTalkService;
	
	@Autowired
	private ISysUserInfoService sysUserInfoService;
	
	@Autowired
	private IAdminService adminService;
	
	@Autowired
	private IGlobalCache globalCache;
	

    /***
     * @Deprecated PC端企业内部应用
     *               钉钉扫码登录
     * @param code 钉钉扫码返回的code
     * @return
     */
    @SuppressWarnings("unchecked")
	@GetMapping(value="/dingtalkCallback")
    @ApiOperation(value = "钉钉扫码登录接口", notes = "钉钉扫码登录")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "query", name = "code", value = "钉钉临时授权码", dataType = "String",required = true)
    })
    public Result<?> dingdingCallback( @NotBlank(message = "授权码不能为空") String code) {
       	//1、获取钉钉access_token
    	Result<?> resAccessToken = dingTalkService.getDingtalkAccessToken();
    	if(resAccessToken.getCode() != 200) {
    		return resAccessToken;
    	}
        //2、根据access_token和code获取用户userid
        Result<?> resUserId = dingTalkService.getDingtalkUserId(Convert.toStr(resAccessToken.getData()), code);
        if(resUserId.getCode() != 200) {
    		return resUserId;
    	}
        //3、根据userId获取用户信息
        Result<?> resUserInfo = dingTalkService.getDingtalkUserInfo(Convert.toStr(resAccessToken.getData()),Convert.toStr(resUserId.getData()));
        if(resUserInfo.getCode() != 200) {
    		return resUserInfo;
    	}
        //4、用户表补全钉钉信息
        UserGetResponse dingtalkUser = (UserGetResponse) resUserInfo.getData();
        Result<?> resSysUserInfo = sysUserInfoService.updateUserInfoByMobile(dingtalkUser);
        if(resSysUserInfo.getCode()!=200) {
        	return resSysUserInfo;
        }
        //5、查询用户信息
        SysUserInfo sui = sysUserInfoService.getAdminByUsername(dingtalkUser.getMobile(), dingtalkUser.getUserid());
        if(ObjectUtil.isEmpty(sui)) {
        	//注册用户
        	sui = sysUserInfoService.saveUserInfo(dingtalkUser);
        	//绑定默认角色
        	sysUserInfoService.saveRole(sui);
        }
        //6、查询用户：角色-资源
        List<SysPermission> resList = sysUserInfoService.getPermissionList(sui.getId(),Constants.PROPERTYTYPE_WEB);
        //7、查询用户所有角色
        List<com.pms.common.pojo.SysRole> roleList = sysUserInfoService.getRoleList(sui.getId());
        //8、获取本地授权服务器local_access_token
        Result<?> resLocalAccessToken = adminService.getAuthAccessToken();
        if(resLocalAccessToken.getCode()!=200) {
        	return resLocalAccessToken;
        }
        //9、封装redis存储对象
		Map<String,Object> localMap = (Map<String, Object>) resLocalAccessToken.getData();
		sui.setDingtalkDeptId(Convert.toStr(dingtalkUser.getDeptIdList()));
		sui.setDingtalkUserId(dingtalkUser.getUserid());
		sui.setDingtalkUnionId(dingtalkUser.getUnionid());
		AdminUserDetails adminUser = new AdminUserDetails(sui, resList, roleList);
		adminUser.setAccessToken(Convert.toStr(localMap.get("access_token")));
		adminUser.setDingtalkAccessToken(Convert.toStr(resAccessToken.getData()));
		log.info("____{}",JSONUtil.parse(adminUser));
        //10、存入redis
        globalCache.set(Constants.JUSTAUTH+adminUser.getAccessToken(), JSONUtil.parse(adminUser) , Constants.REDIS_TOKEN_TIMEOUT);
        //隐藏信息
        adminUser.getPmsUserInfo().setPwd(null);
        adminUser.setDingtalkAccessToken(null);
        adminUser.setRoleList(null);
        adminUser.setPerList(null);
        return Result.success(adminUser);
    }
    
    
    /***
     * @Deprecated PC端企业内部应用
     *               钉钉标准oauth2.0登录
     * @param authCode 钉钉oauth2.0授权码
     * @return
     */
    @SuppressWarnings("unchecked")
	@GetMapping(value="/dingtalkAuth")
    @ApiOperation(value = "钉钉标准oauth2.0登录接口", notes = "钉钉标准oauth2.0登录")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "query", name = "authCode", value = "钉钉oauth2.0授权码", dataType = "String",required = true)
    })
    public Result<?> dingtalkOAuth( @NotBlank(message = "钉钉oauth2.0授权码不能为空") String authCode) {
       	//1、获取钉钉access_token
    	Result<?> resAuthToken = adminService.getDingtalkOAuth(authCode);
    	if(resAuthToken.getCode() != 200) {
    		return resAuthToken;
    	}
    	//2、获取unionId
        Result<?> resUnionId = dingTalkService.getDingtalkOAuthUnionId(Convert.toStr(resAuthToken.getData()));
        if(resUnionId.getCode() != 200) {
    		return resUnionId;
    	}
        //3、获取免登陆access_token
    	Result<?> resAccessToken = dingTalkService.getDingtalkAccessToken();
    	if(resAccessToken.getCode() != 200) {
    		return resAccessToken;
    	}
    	//3-1、获取用户详细信息
    	Result<?> resUserInfo = dingTalkService.getDingtalkOAuthUserInfo(Convert.toStr(resAccessToken.getData()),Convert.toStr(resUnionId.getData()));
    	//4、用户表补全钉钉信息
        UserGetResponse dingtalkUser = (UserGetResponse) resUserInfo.getData();
        Result<?> resSysUserInfo = sysUserInfoService.updateUserInfoByMobile(dingtalkUser);
        if(resSysUserInfo.getCode()!=200) {
        	return resSysUserInfo;
        }
        //5、查询用户信息
        SysUserInfo sui = sysUserInfoService.getAdminByUsername(dingtalkUser.getMobile(), dingtalkUser.getUserid());
        if(ObjectUtil.isEmpty(sui)) {
        	//注册用户
        	sui = sysUserInfoService.saveUserInfo(dingtalkUser);
        	//绑定默认角色
        	sysUserInfoService.saveRole(sui);
        }
        //6、查询用户：角色-资源
        List<SysPermission> resList = sysUserInfoService.getPermissionList(sui.getId(),Constants.PROPERTYTYPE_WEB);
        //7、查询用户所有角色
        List<com.pms.common.pojo.SysRole> roleList = sysUserInfoService.getRoleList(sui.getId());
        //8、获取本地授权服务器local_access_token
        Result<?> resLocalAccessToken = adminService.getAuthAccessToken();
        if(resLocalAccessToken.getCode()!=200) {
        	return resLocalAccessToken;
        }
        //9、封装redis存储对象
		Map<String,Object> localMap = (Map<String, Object>) resLocalAccessToken.getData();
		sui.setDingtalkDeptId(Convert.toStr(dingtalkUser.getDeptIdList()));
		sui.setDingtalkUserId(dingtalkUser.getUserid());
		sui.setDingtalkUnionId(dingtalkUser.getUnionid());
		AdminUserDetails adminUser = new AdminUserDetails(sui, resList, roleList);
		adminUser.setAccessToken(Convert.toStr(localMap.get("access_token")));
		adminUser.setDingtalkAccessToken(Convert.toStr(resAccessToken.getData()));
        //10、存入redis
        globalCache.set(Constants.JUSTAUTH+adminUser.getAccessToken(), JSONUtil.parse(adminUser) , Constants.REDIS_TOKEN_TIMEOUT);
        //隐藏信息
        adminUser.getPmsUserInfo().setPwd(null);
        adminUser.setDingtalkAccessToken(null);
        adminUser.setRoleList(null);
        adminUser.setPerList(null);
        return Result.success(adminUser);
    }
}
