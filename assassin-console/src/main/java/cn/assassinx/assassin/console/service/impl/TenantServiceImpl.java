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
import cn.assassinx.assassin.console.dto.TenantUserDTO;
import cn.assassinx.assassin.console.entity.Tenant;
import cn.assassinx.assassin.console.entity.TenantTemplate;
import cn.assassinx.assassin.console.entity.TenantUser;
import cn.assassinx.assassin.console.exception.AssassinServiceException;
import cn.assassinx.assassin.console.mapper.TenantMapper;
import cn.assassinx.assassin.console.mapper.TenantTemplateMapper;
import cn.assassinx.assassin.console.mapper.TenantUserMapper;
import cn.assassinx.assassin.console.service.ITenantService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author Barton
 */
@Service
@AllArgsConstructor
public class TenantServiceImpl extends ServiceImpl<TenantMapper, Tenant> implements ITenantService {

	private final TenantTemplateMapper tenantTemplateMapper;

	private final TenantMapper tenantMapper;

	private final TenantUserMapper tenantUserMapper;

	private final MessageSource messageSource;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveTenantTemplate(String tenantId, List<String> templateIds) {
		tenantTemplateMapper.delete(new QueryWrapper<TenantTemplate>().lambda().eq(TenantTemplate::getTenantId, tenantId));
		if (null != templateIds && templateIds.size() > 0) {
			tenantTemplateMapper.insertTenantTemplate(tenantId, templateIds);
		}
	}

	@Override
	public void saveTenantUsers(TenantUserDTO tenantUserDTO) {
		String tenantId = tenantUserDTO.getTenantId();
		List<String> userIds = tenantUserDTO.getUserIds();
		if (tenantUserMapper.selectCount(new QueryWrapper<TenantUser>().lambda()
			.eq(TenantUser::getTenantId, tenantId)
			.in(TenantUser::getUserId, userIds)) > 0) {
			throw new AssassinServiceException(AssassinConstant.TENANT_USER_REPEAT_CODE, messageSource.getMessage("tenant.name.repeat", null, LocaleContextHolder.getLocale()));
		}
		tenantUserMapper.insertTenantUser(tenantId, userIds);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean removeById(Serializable id) {
		return SqlHelper.retBool(tenantMapper.deleteById(id))
			&& SqlHelper.retBool(tenantTemplateMapper.delete(new QueryWrapper<TenantTemplate>().lambda().eq(TenantTemplate::getTenantId, id)))
			&& SqlHelper.retBool(tenantUserMapper.delete(new QueryWrapper<TenantUser>().lambda().eq(TenantUser::getTenantId, id)));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		return SqlHelper.retBool(tenantMapper.deleteBatchIds(idList))
			&& SqlHelper.retBool(tenantTemplateMapper.delete(new QueryWrapper<TenantTemplate>().lambda().in(TenantTemplate::getTenantId, idList)))
			&& SqlHelper.retBool(tenantUserMapper.delete(new QueryWrapper<TenantUser>().lambda().in(TenantUser::getTenantId, idList)));
	}

	@Override
	public boolean save(Tenant tenant) {
		checkBeforeSaveOrUpdate(tenant, false);
		return SqlHelper.retBool(tenantMapper.insert(tenant));
	}

	@Override
	public boolean updateById(Tenant tenant) {
		checkBeforeSaveOrUpdate(tenant, true);
		return SqlHelper.retBool(tenantMapper.updateById(tenant));
	}

	private void checkBeforeSaveOrUpdate(Tenant tenant, boolean isUpdate) {
		if (tenantMapper.selectCount(new QueryWrapper<Tenant>().lambda()
			.ne(isUpdate, Tenant::getTenantId, tenant.getTenantId())
			.eq(Tenant::getTenantName, tenant.getTenantName())) > 0) {
			throw new AssassinServiceException(AssassinConstant.TENANT_NAME_REPEAT_CODE, messageSource.getMessage("tenant.name.repeat", null, LocaleContextHolder.getLocale()));
		}
	}
}
