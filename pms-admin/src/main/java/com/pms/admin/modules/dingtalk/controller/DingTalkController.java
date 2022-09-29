package com.pms.admin.modules.dingtalk.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dingtalk.api.response.OapiV2UserGetResponse.UserGetResponse;
import com.pms.admin.config.redis.IGlobalCache;
import com.pms.admin.modules.admin.service.IAdminService;
import com.pms.admin.modules.dingtalk.service.IDingTalkService;
import com.pms.common.pojo.Result;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
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
	private IDingTalkService  dingTalkService;
	
	@Autowired
	private IAdminService  adminService;
	
	@Autowired
	private IGlobalCache globalCache;

    /***
     * 扫码后的回调方法
     * @param code 钉钉扫码返回的code
     * @return
     */
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
        //4、获取本地授权服务器local_access_token
        Result<?> resLocalAccessToken = adminService.getAuthAccessToken();
        if(resLocalAccessToken.getCode()!=200) {
        	return resLocalAccessToken;
        }
        //5、本地token信息绑定钉钉登录信息
        Map<String,Object> map = MapUtil.createMap(Map.class);
        //钉钉用户对象
        UserGetResponse dingtalkUser = (UserGetResponse) resUserInfo.getData();
        //本地local_access_token对象
        Map<String,Object> localMap = (Map<String, Object>) resLocalAccessToken.getData();
        map.put("dingtalk_access_token", resAccessToken.getData());
        map.put("access_token", localMap.get("access_token"));
        map.put("email", dingtalkUser.getEmail());
        map.put("mobile", dingtalkUser.getMobile());
        map.put("name", dingtalkUser.getName());
        map.put("userid", dingtalkUser.getUserid());
        map.put("unionid", dingtalkUser.getUnionid());
        //6、存入redis
        globalCache.set(Convert.toStr(localMap.get("access_token")), map, 60*4);
        return Result.success(map);
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
