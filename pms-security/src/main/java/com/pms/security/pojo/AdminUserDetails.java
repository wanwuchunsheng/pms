package com.pms.security.pojo;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.pms.common.pojo.SysResouce;
import com.pms.common.pojo.SysUserInfo;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AdminUserDetails implements UserDetails {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SysUserInfo pmsUserInfo;
    
    private List<SysResouce> pmsResouceList;
  
    public AdminUserDetails() {
    	
    }
    
    public AdminUserDetails(SysUserInfo pmsUserInfo, List<SysResouce> pmsResouceList) { 
        this.pmsUserInfo = pmsUserInfo;
        this.pmsResouceList = pmsResouceList;
    }

    /**
     * 
     * list.stream().map().collect()方法,
     * 可以获取list中JavaBean的某个字段,转成一个新的list
     * 
     * */
    @Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
        //返回当前用户的权限
        return pmsResouceList.stream()
                .filter(permission -> permission.getResPath()!=null)
                .map(permission ->new SimpleGrantedAuthority(permission.getResPath()))
                .collect(Collectors.toList());
    }

    
    @Override
    public String getPassword() {
        return pmsUserInfo.getPwd();
    }

    
    @Override
    public String getUsername() {
        return pmsUserInfo.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return pmsUserInfo.getEnable()==0;
    }

	
}
