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

import cn.assassinx.assassin.client.util.SecurityUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;

/**
 * @author Barton
 */
@Component("access")
public class Access {

	public boolean accessed(String access) {
		Authentication authentication = SecurityUtil.getAuthentication();
		if (null == authentication) {
			return false;
		}
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		String tenantId = SecurityUtil.getTenantId();
		if (null != tenantId) {
			return authorities.stream().map(GrantedAuthority::getAuthority).filter(StringUtils::hasText)
				.anyMatch(x -> PatternMatchUtils.simpleMatch(tenantId + "_" + access, x));
		}
		return authorities.stream().map(GrantedAuthority::getAuthority).filter(StringUtils::hasText)
			.anyMatch(x -> PatternMatchUtils.simpleMatch(access, x));
	}
}
