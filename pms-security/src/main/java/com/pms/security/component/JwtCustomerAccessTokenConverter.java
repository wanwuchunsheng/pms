package com.pms.security.component;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.security.core.authority.AuthorityUtils;

public class JwtCustomerAccessTokenConverter extends DefaultAccessTokenConverter {
	 
		public JwtCustomerAccessTokenConverter() {
			super.setUserTokenConverter(new CustomerUserAuthenticationConverter());
		}
	 
		private class CustomerUserAuthenticationConverter extends DefaultUserAuthenticationConverter {
	 
			@Override
			public Map<String, ?> convertUserAuthentication(Authentication authentication) {
				LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
				//这里添加你的参数
				response.put("id", ((PiUserDetails) authentication.getPrincipal()).getId());
				response.put("username", ((PiUserDetails) authentication.getPrincipal()).getUsername());
				if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
					response.put("authorities", AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
				}
	 
				return response;
			}
		}


}
