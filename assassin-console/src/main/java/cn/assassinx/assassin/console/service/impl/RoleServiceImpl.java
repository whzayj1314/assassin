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
import cn.assassinx.assassin.common.constant.AssassinConstant;
import cn.assassinx.assassin.console.async.event.AccessChangedEvent;
import cn.assassinx.assassin.console.entity.Product;
import cn.assassinx.assassin.console.entity.Role;
import cn.assassinx.assassin.console.entity.RoleAccess;
import cn.assassinx.assassin.console.entity.UserRole;
import cn.assassinx.assassin.console.exception.AssassinServiceException;
import cn.assassinx.assassin.console.mapper.ProductMapper;
import cn.assassinx.assassin.console.mapper.RoleAccessMapper;
import cn.assassinx.assassin.console.mapper.RoleMapper;
import cn.assassinx.assassin.console.mapper.UserRoleMapper;
import cn.assassinx.assassin.console.service.IClientDetailsService;
import cn.assassinx.assassin.console.service.IRoleService;
import cn.assassinx.assassin.console.vo.RoleVO;
import cn.assassinx.assassin.console.vo.TransferVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import java.util.stream.Collectors;

/**
 * @author Barton
 */
@Service
@AllArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

	private final RoleMapper roleMapper;

	private final RoleAccessMapper roleAccessMapper;

	private final UserRoleMapper userRoleMapper;

	private final ProductMapper productMapper;

	private final MessageSource messageSource;

	private final IClientDetailsService clientDetailsService;

	@Override
	public IPage<RoleVO> selectInRolePage(Page page, Role role) {
		return roleMapper.selectInnerRolePageVO(page, role);
	}

	@Override
	public List<String> findCheckedKeysByRoleId(String roleId) {
		List<RoleAccess> roleAccesses = roleAccessMapper.selectList(new QueryWrapper<RoleAccess>().lambda().eq(RoleAccess::getRoleId, roleId));
		return roleAccesses.stream().map(roleAccess -> roleAccess.getAccessId()).collect(Collectors.toList());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveRoleAccess(String roleId, List<String> accessIds) {
		List<String> oldAccessIds = new ArrayList<>();
		List<RoleAccess> roleAccesses = roleAccessMapper.selectList(new QueryWrapper<RoleAccess>().lambda().eq(RoleAccess::getRoleId, roleId));
		if (null != roleAccesses && roleAccesses.size() > 0) {
			oldAccessIds = roleAccesses.stream().map(roleAccess -> roleAccess.getAccessId()).collect(Collectors.toList());
		}
		roleAccessMapper.delete(new QueryWrapper<RoleAccess>().lambda().eq(RoleAccess::getRoleId, roleId));
		if (null != accessIds && accessIds.size() > 0) {
			roleAccessMapper.insertRoleAccess(roleId, accessIds);
		}
		List<String> changedAccessIds = MathUtil.differenceSetAndUnion(oldAccessIds, accessIds);
		if (null != changedAccessIds && changedAccessIds.size() > 0) {
			AssassinContextHolder.publishEvent(new AccessChangedEvent(this,
				userRoleMapper.selectTokenRemove(roleId)));
		}
	}

	@Override
	public List<TransferVO> findRoleTransferVOList() {
		return roleMapper.selectInnerRoleTransferVOList();
	}

	@Override
	public List<String> findTargetKeysByUserId(String userId) {
		return roleMapper.selectRoleIdsByUserId(userId);
	}

	@Override
	public IPage<Role> selectTenantRolePage(Page page, String tenantId, String productId) {
		return roleMapper.selectPage(page, new QueryWrapper<Role>().lambda().eq(Role::getProductId, productId)
			.eq(Role::getTenantId, tenantId));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean removeById(Serializable id) {
		return SqlHelper.retBool(roleMapper.deleteById(id))
			&& SqlHelper.retBool(roleAccessMapper.delete(new QueryWrapper<RoleAccess>().lambda().eq(RoleAccess::getRoleId, id)))
			&& SqlHelper.retBool(userRoleMapper.delete(new QueryWrapper<UserRole>().lambda().eq(UserRole::getRoleId, id)));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		return SqlHelper.retBool(roleMapper.deleteBatchIds(idList))
			&& SqlHelper.retBool(roleAccessMapper.delete(new QueryWrapper<RoleAccess>().lambda().in(RoleAccess::getRoleId, idList)))
			&& SqlHelper.retBool(userRoleMapper.delete(new QueryWrapper<UserRole>().lambda().in(UserRole::getRoleId, idList)));
	}

	@Override
	public boolean save(Role role) {
		checkBeforeSaveOrUpdate(role, false);
		return SqlHelper.retBool(roleMapper.insert(role));
	}

	@Override
	public boolean updateById(Role role) {
		checkBeforeSaveOrUpdate(role, true);
		return SqlHelper.retBool(roleMapper.updateById(role));
	}

	private void checkBeforeSaveOrUpdate(Role role, boolean isUpdate) {
		if (productMapper.selectCount(new QueryWrapper<Product>().lambda()
			.eq(Product::getProductId, role.getProductId())
			.eq(null != role.getTenantId(), Product::getProductType, AssassinConstant.PRODUCT_TYPE_MULTI_TENANT)
			.eq(null == role.getTenantId(), Product::getProductType, AssassinConstant.PRODUCT_TYPE_IN)) != 1) {
			throw new AssassinServiceException(AssassinConstant.ROLE_PRODUCT_NULL_CODE, messageSource.getMessage("role.product.null", null, LocaleContextHolder.getLocale()));
		}
		if (roleMapper.selectCount(new QueryWrapper<Role>().lambda()
			.eq(Role::getRoleName, role.getRoleName())
			.ne(isUpdate, Role::getRoleId, role.getRoleId())
			.eq(Role::getProductId, role.getProductId())
		) > 0) {
			throw new AssassinServiceException(AssassinConstant.ROLE_NAME_REPEAT_CODE, messageSource.getMessage("role.name.repeat", null, LocaleContextHolder.getLocale()));
		}
	}
}


