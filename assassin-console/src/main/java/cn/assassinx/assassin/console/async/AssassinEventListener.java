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

package cn.assassinx.assassin.console.async;

import cn.assassinx.assassin.console.async.event.AccessChangedEvent;
import cn.assassinx.assassin.console.service.IProductService;
import cn.assassinx.assassin.core.security.AssassinUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Barton
 */
@Slf4j
@Component
public class AssassinEventListener {

	@Autowired
	@Qualifier("redisTokenStore")
	TokenStore redisTokenStore;

	@Autowired
	IProductService productService;

	@Async
	@EventListener
	public void afterAccessChanged(AccessChangedEvent accessChangedEvent) {
		log.info("afterAccessChange:,{}", accessChangedEvent.toString());
		List<TokenRemove> tokenRemoveList = accessChangedEvent.getTokenRemoveList();
		if (null != tokenRemoveList && tokenRemoveList.size() > 0) {
			removeToken(tokenRemoveList);
		}
	}

	@Async
	@EventListener
	public void afterLoginSucceed(AuthenticationSuccessEvent authenticationSuccessEvent) {
		Authentication authentication = (Authentication) authenticationSuccessEvent.getSource();
		if (authentication.getPrincipal() instanceof AssassinUserDetails) {
			log.info("afterLoginSuccess:,{}", authenticationSuccessEvent.toString());
			AssassinUserDetails assassinUserDetails = (AssassinUserDetails) authentication.getPrincipal();
			List<String> mutexClients = productService.selectMutexClientsByClientId(assassinUserDetails.getClientId());
			if (null != mutexClients && mutexClients.size() > 0) {
				removeToken(mutexClients.stream().map(str -> new TokenRemove(str, assassinUserDetails.getUsername())).collect(Collectors.toList()));
			}
		}
	}

	public void removeToken(List<TokenRemove> tokenRemoveList) {
		tokenRemoveList.stream().map(tokenRemove -> redisTokenStore
			.findTokensByClientIdAndUserName(tokenRemove.getClientId(), tokenRemove.getAccount()))
			.filter(p -> null != p && p.size() > 0).collect(Collectors.toList())
			.forEach(p -> p.forEach(q -> redisTokenStore.removeAccessToken(q)));
	}
}
