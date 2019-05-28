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

import cn.assassinx.assassin.client.exception.AuthExceptionEntryPoint;
import cn.assassinx.assassin.client.filter.AssassinFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author Barton
 */
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Autowired
	IgnoreUrlProperties ignoreUrlProperties;

	@Value("${spring.application.name}")
	String resourceId;

	@Autowired
	DefaultTokenServices defaultTokenServices;

	@Autowired
	@Qualifier("redisTokenStore")
	TokenStore redisTokenStore;

	@Autowired
	MessageSource messageSource;


	@Override
	public void configure(HttpSecurity http) throws Exception {
		ExpressionUrlAuthorizationConfigurer<HttpSecurity>
			.ExpressionInterceptUrlRegistry registry = http
			.authorizeRequests();
		ignoreUrlProperties.getIgnores().forEach(url -> registry.antMatchers(url).permitAll());
		registry.anyRequest().authenticated()
			.and().csrf().disable();
		http.addFilterBefore(new AssassinFilter(redisTokenStore, ignoreUrlProperties, messageSource), UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.resourceId(resourceId).tokenServices(defaultTokenServices).authenticationEntryPoint(new AuthExceptionEntryPoint());
	}
}
