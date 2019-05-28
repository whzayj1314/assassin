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

package cn.assassinx.assassin.client.filter;


import cn.assassinx.assassin.client.config.IgnoreUrlProperties;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

/**
 * @author Barton
 */
@AllArgsConstructor
public class AssassinFilter extends OncePerRequestFilter {

	private final TokenStore redisTokenStore;

	private final IgnoreUrlProperties ignoreUrlProperties;

	private final MessageSource messageSource;

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
		AntPathMatcher antPathMatcher = new AntPathMatcher();
		if (ignoreUrlProperties.getIgnores().stream().anyMatch(url -> antPathMatcher.match(url, httpServletRequest.getRequestURI()))) {
			filterChain.doFilter(httpServletRequest, httpServletResponse);
		} else {
			String bearToken = httpServletRequest.getHeader("Authorization");
			if (null != bearToken) {
				OAuth2AccessToken oAuth2AccessToken = redisTokenStore.readAccessToken(bearToken.substring(7, bearToken.length()));
				if (null == oAuth2AccessToken) {
					unauthorizedResult(httpServletRequest, httpServletResponse);
				} else {
					if (null == ignoreUrlProperties.getDemonstrate() || "false".equals(ignoreUrlProperties.getDemonstrate())) {
						filterChain.doFilter(httpServletRequest, httpServletResponse);
					} else {
						if ("GET".equals(httpServletRequest.getMethod())) {
							filterChain.doFilter(httpServletRequest, httpServletResponse);
						} else {
							forbiddenResult(httpServletRequest, httpServletResponse);
						}
					}
				}
			} else {
				unauthorizedResult(httpServletRequest, httpServletResponse);
			}
		}
	}

	private void unauthorizedResult(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
		String[] lang = (null != httpServletRequest.getHeader("Lang") ? httpServletRequest.getHeader("Lang") : "zh-CN").split("-");
		httpServletResponse.setStatus(401);
		httpServletResponse.setCharacterEncoding("utf-8");
		httpServletResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		String write = "{\"code\":401,\"message\":\"" + messageSource.getMessage("Assassin.invalid", null, new Locale(lang[0], lang[1])) + "\",\"data\":null}";
		ServletOutputStream out = httpServletResponse.getOutputStream();
		out.write(write.getBytes());
		out.flush();
		out.close();
	}

	private void forbiddenResult(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
		String[] lang = (null != httpServletRequest.getHeader("Lang") ? httpServletRequest.getHeader("Lang") : "zh-CN").split("-");
		httpServletResponse.setStatus(403);
		httpServletResponse.setCharacterEncoding("utf-8");
		httpServletResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		String write = "{\"code\":403,\"message\":\"" + messageSource.getMessage("Assassin.demoNotAllowed", null, new Locale(lang[0], lang[1])) + "\",\"data\":null}";
		ServletOutputStream out = httpServletResponse.getOutputStream();
		out.write(write.getBytes());
		out.flush();
		out.close();
	}
}
