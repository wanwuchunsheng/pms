package com.pms.admin.modules.dingtalk.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dingtalk.api.response.OapiV2UserGetResponse.UserGetResponse;
import com.pms.admin.modules.admin.service.IAdminService;
import com.pms.admin.modules.admin.service.ISysUserInfoService;
import com.pms.admin.modules.dingtalk.service.IDingTalkService;
import com.pms.common.constant.Constants;
import com.pms.common.pojo.Result;
import com.pms.common.pojo.SysResouce;
import com.pms.common.pojo.SysUserInfo;
import com.pms.common.pojo.User;
import com.pms.common.redis.IGlobalCache;
import com.pms.security.pojo.AdminUserDetails;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

/**
 * <p>钉钉扫码登录</p>
 *
 * @author WCH
 * Date 2022/5/10
 * @version 1.0
 */
@RestController
@RequestMapping("/dingtalk")
@Log4j2
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
     * 扫码后的回调方法
     * @param code 钉钉扫码返回的code
     * @return
     */
    @SuppressWarnings("unchecked")
	@GetMapping(value="/dingtalkCallback")
    @ApiOperation(value = "钉钉扫码登录接口", notes = "钉钉扫码登录")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "query", name = "code", value = "钉钉临时授权码", dataType = "String",required = true)
    })
    public Result<?> dingdingCallback(String code) {
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
    		return resUserId;
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
        List<SysResouce> resList = sysUserInfoService.getPermissionList(sui.getId());
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
		AdminUserDetails adminUser = new AdminUserDetails(sui,resList,roleList);
		adminUser.setAccessToken(Convert.toStr(localMap.get("access_token")));
		adminUser.setDingtalkAccessToken(Convert.toStr(resAccessToken.getData()));
        //10、存入redis
        globalCache.set(Constants.JUSTAUTH+adminUser.getAccessToken(), JSONUtil.parse(adminUser) , Constants.REDIS_TOKEN_TIMEOUT);
        adminUser.getPmsUserInfo().setPwd(null);
        adminUser.setDingtalkAccessToken(null);
        return Result.success(adminUser);
    }

	
	/**
     * 钉钉扫码登录
     * 
     * @return
     */
    @GetMapping("/getUserInfo")
    @ApiOperation(value = "获取钉钉用户个人信息接口", notes = "获取钉钉用户个人信息")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "query", name = "accessToken", value = "钉钉access_token", dataType = "String",required = true),
        @ApiImplicitParam(paramType = "query", name = "userid", value = "钉钉userid", dataType = "String",required = true)
    })
    public Result<?> getUserInfo(String accessToken,String userid){
        return dingTalkService.getDingtalkUserInfo(accessToken,userid);
    }
    
}
