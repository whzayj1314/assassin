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

package cn.assassinx.assassin.console.config;

import cn.assassinx.assassin.common.constant.SecurityConstant;
import cn.assassinx.assassin.console.exception.AssassinWebResponseExceptionTranslator;
import cn.assassinx.assassin.console.service.ITokenRuleService;
import cn.assassinx.assassin.console.service.impl.AssassinClientDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;

/**
 * @author Barton
 */
@Configuration
@EnableAuthorizationServer
public class OAuth2ServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtAccessTokenConverter jwtAccessTokenConverter;

	@Autowired
	DataSource dataSource;

	@Autowired
	RedisTemplate redisTemplate;

	@Autowired
	RedisConnectionFactory redisConnectionFactory;

	@Autowired
	ITokenRuleService tokenRuleService;

	@Autowired
	@Qualifier("redisTokenStore")
	TokenStore redisTokenStore;

	@Autowired
	MessageSource messageSource;


	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) {
		security.allowFormAuthenticationForClients().checkTokenAccess("isAuthenticated()").tokenKeyAccess("permitAll()");
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		AssassinClientDetailServiceImpl assassinClientDetailService = new AssassinClientDetailServiceImpl(dataSource, redisTemplate, tokenRuleService);
		assassinClientDetailService.setFindClientDetailsSql(SecurityConstant.DEFAULT_FIND_STATEMENT);
		assassinClientDetailService.setSelectClientDetailsSql(SecurityConstant.DEFAULT_SELECT_STATEMENT);
		clients.withClientDetails(assassinClientDetailService);
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
		endpoints.authenticationManager(authenticationManager)
			.exceptionTranslator(new AssassinWebResponseExceptionTranslator(messageSource))
			.tokenStore(redisTokenStore)
			.accessTokenConverter(jwtAccessTokenConverter)
			.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
	}
}
