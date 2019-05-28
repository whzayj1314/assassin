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

import cn.assassinx.assassin.common.constant.AssassinConstant;
import cn.assassinx.assassin.console.entity.Product;
import cn.assassinx.assassin.console.entity.User;
import cn.assassinx.assassin.console.exception.UserAccessNotFoundException;
import cn.assassinx.assassin.console.exception.UserRoleNotFoundException;
import cn.assassinx.assassin.console.mapper.AccessMapper;
import cn.assassinx.assassin.console.mapper.ProductMapper;
import cn.assassinx.assassin.console.mapper.UserMapper;
import cn.assassinx.assassin.core.security.AssassinUserDetails;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Barton
 */
@Service
@AllArgsConstructor
public class AssassinUserServiceImpl implements UserDetailsService {

	private final AccessMapper accessMapper;

	private final ProductMapper productMapper;

	private final UserMapper userMapper;

	@Override
	public UserDetails loadUserByUsername(String s) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String clientId = new String(Base64Utils.decodeFromString(request.getHeader("Authorization").split(" ")[1])).split(":")[0];
		Product product = productMapper.selectProductByClientId(clientId);
		User user = userMapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getAccount, s));
		String[] access = findAccess(product.getProductType(), product.getProductId(), s);
		int roleCount = userMapper.selectCountUserRoleByProductIdAndAccount(product.getProductId(), s);
		if (null == user) {
			throw new UsernameNotFoundException("");
		}
		if (AssassinConstant.PRODUCT_ACCESS_RULE_ROLE.equals(product.getAccessRule()) || AssassinConstant.PRODUCT_ACCESS_RULE_ACCESS.equals(product.getAccessRule())) {
			if (roleCount < 1) {
				throw new UserRoleNotFoundException("");
			}
		}
		if (AssassinConstant.PRODUCT_ACCESS_RULE_ACCESS.equals(product.getAccessRule()) && access.length < 1) {
			throw new UserAccessNotFoundException("");
		}
		Collection<? extends GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(access);
		org.springframework.security.core.userdetails.User securityUser = new org.springframework.security.core.userdetails.User(user.getAccount(), "{bcrypt}" + user.getPassword(), authorities);
		Map<String, Object> userExtend = new LinkedHashMap<>();
		userExtend.put("user_id", user.getUserId());
		userExtend.put("nickname", user.getNickname());
		return new AssassinUserDetails(securityUser, userExtend, clientId, "0".equals(user.getLockFlag()));
	}

	private String[] findAccess(String productType, String productId, String account) {
		if (AssassinConstant.PRODUCT_TYPE_IN.equals(productType)) {
			List<String> accessList = accessMapper.selectAccessByProductIdAndAccount(productId, account);
			String[] access = new String[accessList.size()];
			accessList.toArray(access);
			return access;
		} else if (AssassinConstant.PRODUCT_TYPE_MULTI_TENANT.equals(productType)) {
			List<String> accessList = accessMapper.selectTenantAccessByProductIdAndAccount(productId, account);
			String[] access = new String[accessList.size()];
			accessList.toArray(access);
			return access;
		} else {
			return null;
		}
	}
}
