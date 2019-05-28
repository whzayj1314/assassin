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

import cn.assassinx.assassin.client.util.AssassinContextHolder;
import cn.assassinx.assassin.client.util.MathUtil;
import cn.assassinx.assassin.client.util.SecurityUtil;
import cn.assassinx.assassin.common.constant.AssassinConstant;
import cn.assassinx.assassin.console.async.TokenRemove;
import cn.assassinx.assassin.console.async.event.AccessChangedEvent;
import cn.assassinx.assassin.console.entity.AssassinClientDetails;
import cn.assassinx.assassin.console.entity.TenantUser;
import cn.assassinx.assassin.console.entity.User;
import cn.assassinx.assassin.console.entity.UserRole;
import cn.assassinx.assassin.console.exception.AssassinServiceException;
import cn.assassinx.assassin.console.mapper.TenantUserMapper;
import cn.assassinx.assassin.console.mapper.UserMapper;
import cn.assassinx.assassin.console.mapper.UserRoleMapper;
import cn.assassinx.assassin.console.service.IClientDetailsService;
import cn.assassinx.assassin.console.service.IUserService;
import cn.assassinx.assassin.console.vo.UserVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Barton
 */
@Service
@AllArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

	private final UserMapper userMapper;

	private final UserRoleMapper userRoleMapper;

	private final TenantUserMapper tenantUserMapper;

	private final MessageSource messageSource;

	private final IClientDetailsService clientDetailsService;

	@Override
	public UserVO selectCurrentUserVO() {
		Map<String, Object> principal = SecurityUtil.getAssassinUserDetails();
		return userMapper.selectUserVoByClientIdAndAccount((String) principal.get("client_id"), (String) principal.get("user_name"));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveUserRole(String userId, List<String> roleIds) {
		String inSql = "select role_id a " +
			"from role a " +
			"join product b on a.product_id = b.product_id and b.del_flag = '0' and b.product_type = '0'";
		List<String> oldRoleIds = new ArrayList<>();
		List<UserRole> userRoles = userRoleMapper.selectList(new QueryWrapper<UserRole>().lambda().eq(UserRole::getUserId, userId).inSql(UserRole::getRoleId, inSql));
		if (null != userRoles && userRoles.size() > 0) {
			oldRoleIds = userRoles.stream().map(userRole -> userRole.getRoleId()).collect(Collectors.toList());
		}
		userRoleMapper.delete(new QueryWrapper<UserRole>().lambda().eq(UserRole::getUserId, userId).inSql(UserRole::getRoleId, inSql));
		if (null != roleIds && roleIds.size() > 0) {
			userRoleMapper.insertUserRole(userId, roleIds);
		}
		List<String> changedRoleIds = MathUtil.differenceSetAndUnion(oldRoleIds, roleIds);
		if (null != changedRoleIds && changedRoleIds.size() > 0) {
			AssassinContextHolder.publishEvent(new AccessChangedEvent(this,
				userRoleMapper.selectTokenRemoveByRoleIds(SecurityUtil.getAccount(), changedRoleIds)));
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveTenantUserRole(String userId, List<String> roleIds, String productId) {
		List<UserRole> userRoles = userRoleMapper.selectTenantUserRoleList(userId, productId);
		List<String> oldRoleIds = new ArrayList<>();
		if (null != userRoles && userRoles.size() > 0) {
			oldRoleIds = userRoles.stream().map(userRole -> userRole.getRoleId()).collect(Collectors.toList());
			userRoleMapper.delete(new QueryWrapper<UserRole>().lambda().eq(UserRole::getUserId, userId).in(UserRole::getRoleId, oldRoleIds));
		}
		if (null != roleIds && roleIds.size() > 0) {
			userRoleMapper.insertUserRole(userId, roleIds);
		}
		List<String> changedRoleIds = MathUtil.differenceSetAndUnion(oldRoleIds, roleIds);
		if (null != changedRoleIds && changedRoleIds.size() > 0) {
			String account = userMapper.selectById(userId).getAccount();
			List<TokenRemove> tokenRemoveList = clientDetailsService.list(new QueryWrapper<AssassinClientDetails>()
				.lambda().eq(AssassinClientDetails::getProductId, productId)).stream()
				.map(assassinClientDetails -> new TokenRemove(assassinClientDetails.getClientId(), account)).collect(Collectors.toList());
			AssassinContextHolder.publishEvent(new AccessChangedEvent(this, tokenRemoveList));
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean removeById(Serializable id) {
		return SqlHelper.retBool(userMapper.deleteById(id))
			&& SqlHelper.retBool(userRoleMapper.delete(new QueryWrapper<UserRole>().lambda().eq(UserRole::getUserId, id)))
			&& SqlHelper.retBool(tenantUserMapper.delete(new QueryWrapper<TenantUser>().lambda().eq(TenantUser::getUserId, id)));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		return SqlHelper.retBool(userMapper.deleteBatchIds(idList))
			&& SqlHelper.retBool(userRoleMapper.delete(new QueryWrapper<UserRole>().lambda().in(UserRole::getUserId, idList)))
			&& SqlHelper.retBool(tenantUserMapper.delete(new QueryWrapper<TenantUser>().lambda().in(TenantUser::getUserId, idList)));
	}

	@Override
	public boolean save(User user) {
		checkBeforeSaveOrUpdate(user, false);
		return SqlHelper.retBool(userMapper.insert(user));
	}

	@Override
	public boolean updateById(User user) {
		checkBeforeSaveOrUpdate(user, true);
		return SqlHelper.retBool(userMapper.updateById(user));
	}

	private void checkBeforeSaveOrUpdate(User user, boolean isUpdate) {
		if (userMapper.selectCount(new QueryWrapper<User>().lambda()
			.ne(isUpdate, User::getUserId, user.getUserId())
			.eq(User::getAccount, user.getAccount())) > 0) {
			throw new AssassinServiceException(AssassinConstant.USER_ACCOUNT_REPEAT_CODE, messageSource.getMessage("user.account.repeat", null, LocaleContextHolder.getLocale()));
		}
		if (userMapper.selectCount(new QueryWrapper<User>().lambda()
			.ne(isUpdate, User::getUserId, user.getUserId())
			.eq(User::getNickname, user.getNickname())) > 0) {
			throw new AssassinServiceException(AssassinConstant.USER_NICKNAME_REPEAT_CODE, messageSource.getMessage("user.nickname.repeat", null, LocaleContextHolder.getLocale()));
		}
		if (userMapper.selectCount(new QueryWrapper<User>().lambda()
			.ne(isUpdate, User::getUserId, user.getUserId())
			.eq(User::getMobilePhoneNumber, user.getMobilePhoneNumber())) > 0) {
			throw new AssassinServiceException(AssassinConstant.USER_MOBILE_PHONE_NUMBER_REPEAT_CODE, messageSource.getMessage("user.mobilePhoneNumber.repeat", null, LocaleContextHolder.getLocale()));
		}
	}
}
