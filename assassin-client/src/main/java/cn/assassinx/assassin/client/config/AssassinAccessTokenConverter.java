/*
 * Copyright (c) 2019-2029, Barton Wu (396264893@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.assassinx.assassin.client.config;

import cn.assassinx.assassin.core.security.AssassinUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Barton
 */
public class AssassinAccessTokenConverter extends DefaultAccessTokenConverter {

	public AssassinAccessTokenConverter() {
		super.setUserTokenConverter(new AssassinUserAuthenticationConverter());
	}

	private class AssassinUserAuthenticationConverter extends DefaultUserAuthenticationConverter {

		@Override
		public Map<String, ?> convertUserAuthentication(Authentication authentication) {
			Map<String, Object> response = new LinkedHashMap();
			response.put("user_name", authentication.getName());
			response.put("user_extend", ((AssassinUserDetails) authentication.getPrincipal()).getUserExtend());
			if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
				response.put("authorities", AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
			}
			return response;
		}

		@Override
		public Authentication extractAuthentication(Map<String, ?> map) {
			if (map.containsKey("user_name")) {
				Map<String, Object> principal = new LinkedHashMap();
				principal.put("user_name", map.get("user_name"));
				principal.put("client_id", map.get("client_id"));
				principal.put("user_extend", map.get("user_extend"));
				Collection<? extends GrantedAuthority> authorities = getAuthorities(map);
				return new UsernamePasswordAuthenticationToken(principal, "N/A", authorities);
			} else {
				return null;
			}
		}

		private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {
			if (!map.containsKey("authorities")) {
				return null;
			} else {
				Object authorities = map.get("authorities");
				if (authorities instanceof String) {
					return AuthorityUtils.commaSeparatedStringToAuthorityList((String) authorities);
				} else if (authorities instanceof Collection) {
					return AuthorityUtils.commaSeparatedStringToAuthorityList(StringUtils.collectionToCommaDelimitedString((Collection) authorities));
				} else {
					throw new IllegalArgumentException("Authorities must be either a String or a Collection");
				}
			}
		}

	}
}
