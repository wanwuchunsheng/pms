package com.pms.admin.modules.admin.dao;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pms.common.pojo.SysUserInfo;
/**
 * @Date: 2019-11-27 11:29:02
 * @Description: 持久层接口
 * @author WCH
 */
@Repository
public interface SysUserInfoDao extends BaseMapper<SysUserInfo> {

	void updateSysUserInfoByUserId(@Param("mobile") String mobile, @Param("userId") String userId, @Param("unionId") String unionId,@Param("deptIds") String deptIds);


}


