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

package cn.assassinx.assassin.console.service.impl;

import cn.assassinx.assassin.common.constant.SecurityConstant;
import cn.assassinx.assassin.console.service.ITokenRuleService;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import javax.sql.DataSource;

/**
 * @author Barton
 */
public class AssassinClientDetailServiceImpl extends JdbcClientDetailsService {

	private final RedisTemplate redisTemplate;

	private final ITokenRuleService tokenRuleService;

	public AssassinClientDetailServiceImpl(DataSource dataSource, RedisTemplate redisTemplate, ITokenRuleService tokenRuleService) {
		super(dataSource);
		this.redisTemplate = redisTemplate;
		this.tokenRuleService = tokenRuleService;
	}

	@Override
	@SneakyThrows
	public ClientDetails loadClientByClientId(String clientId) throws InvalidClientException {
		if (null != redisTemplate.opsForValue().get(SecurityConstant.CLIENT_CACHE_KEY + clientId)) {
			BaseClientDetails baseClientDetails = (BaseClientDetails) redisTemplate.opsForValue().get(SecurityConstant.CLIENT_CACHE_KEY + clientId);
			baseClientDetails.setAccessTokenValiditySeconds(tokenRuleService.selectTokenValiditySecondsByClientId(clientId));
			return baseClientDetails;
		}
		BaseClientDetails baseClientDetails = (BaseClientDetails) super.loadClientByClientId(clientId);
		baseClientDetails.setAccessTokenValiditySeconds(tokenRuleService.selectTokenValiditySecondsByClientId(clientId));
		redisTemplate.opsForValue().set(SecurityConstant.CLIENT_CACHE_KEY + clientId, baseClientDetails);
		return baseClientDetails;
	}
}
