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

package cn.assassinx.assassin.client.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Barton
 */
public class SecurityUtil {

	public static Integer getExpire(int day, int hour, int minute, int second) {
		Calendar cal = Calendar.getInstance();
		cal.add(6, day);
		cal.set(11, hour);
		cal.set(12, minute);
		cal.set(13, second);
		cal.set(14, 0);
		return Integer.valueOf(String.valueOf((cal.getTimeInMillis() - System.currentTimeMillis()) / 1000));
	}

	public static Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	public static Map getAssassinUserDetails() {
		return (LinkedHashMap) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	public static String getUserId() {
		Map<String, Object> principal = (LinkedHashMap) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Map<String, Object> userExtend = (LinkedHashMap) principal.get("user_extend");
		return (String) userExtend.get("user_id");
	}

	public static String getClientId() {
		Map<String, Object> principal = (LinkedHashMap) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return (String) principal.get("client_id");
	}

	public static String getAccount() {
		Map<String, Object> principal = (LinkedHashMap) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return (String) principal.get("user_name");
	}

	public static String getTenantId() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("TenantId");
	}
}
