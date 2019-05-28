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

import cn.assassinx.assassin.client.util.SecurityUtil;
import cn.assassinx.assassin.common.constant.AssassinConstant;
import cn.assassinx.assassin.console.entity.Access;
import cn.assassinx.assassin.console.entity.Product;
import cn.assassinx.assassin.console.entity.RoleAccess;
import cn.assassinx.assassin.console.entity.TemplateAccess;
import cn.assassinx.assassin.console.exception.AssassinServiceException;
import cn.assassinx.assassin.console.mapper.AccessMapper;
import cn.assassinx.assassin.console.mapper.ProductMapper;
import cn.assassinx.assassin.console.mapper.RoleAccessMapper;
import cn.assassinx.assassin.console.mapper.TemplateAccessMapper;
import cn.assassinx.assassin.console.service.IAccessService;
import cn.assassinx.assassin.console.vo.AccessVO;
import cn.assassinx.assassin.console.vo.MenuVO;
import cn.assassinx.assassin.tool.tree.ForestNodeMerger;
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
import java.util.stream.Collectors;

/**
 * @author Barton
 */
@Service
@AllArgsConstructor
public class AccessServiceImpl extends ServiceImpl<AccessMapper, Access> implements IAccessService {

	private final ProductMapper productMapper;

	private final AccessMapper accessMapper;

	private final RoleAccessMapper roleAccessMapper;

	private final TemplateAccessMapper templateAccessMapper;

	private final MessageSource messageSource;

	@Override
	public List<AccessVO> findAccessTree(Access access) {
		return ForestNodeMerger.merge(accessMapper.tree(access));
	}

	@Override
	public List<MenuVO> findAssassinMenuVO() {
		String productId = productMapper.selectProductByClientId(SecurityUtil.getClientId()).getProductId();
		List<String> result = new ArrayList<>();
		List<MenuVO> allMenuVOList = accessMapper.selectAssassinMenuVO(null, productId, "1", null);
		List<MenuVO> menuVOList = accessMapper.selectAssassinMenuVO(SecurityUtil.getUserId(), productId, null, null);
		menuVOList.forEach(menuVO -> assemblingMenuVO(allMenuVOList, result, menuVO));
		return ForestNodeMerger.merge(accessMapper.selectAssassinMenuVO(null, productId, "1", result.stream().distinct().collect(Collectors.toList())));
	}

	@Override
	public List<MenuVO> findTenantMenuVO(String productId) {
		List<String> result = new ArrayList<>();
		List<MenuVO> allMenuVOList = accessMapper.selectAssassinMenuVO(null, productId, "1", null);
		List<MenuVO> menuVOList = accessMapper.selectAssassinMenuVO(SecurityUtil.getUserId(), productId, null, null);
		menuVOList.forEach(menuVO -> assemblingMenuVO(allMenuVOList, result, menuVO));
		return ForestNodeMerger.merge(accessMapper.selectAssassinMenuVO(null, productId, "1", result.stream().distinct().collect(Collectors.toList())));
	}

	private void assemblingMenuVO(List<MenuVO> menuVOList, List<String> result, MenuVO menuVO) {
		if (null != menuVO.getParentId()) {
			result.add(menuVO.getParentId());
			assemblingMenuVO(menuVOList, result, menuVOList.stream().filter(menuVO1 -> menuVO1.getId().equals(menuVO.getParentId())).findFirst().get());
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean removeById(Serializable id) {
		return SqlHelper.retBool(accessMapper.deleteById(id))
			&& SqlHelper.retBool(roleAccessMapper.delete(new QueryWrapper<RoleAccess>().lambda().eq(RoleAccess::getAccessId, id)))
			&& SqlHelper.retBool(templateAccessMapper.delete(new QueryWrapper<TemplateAccess>().lambda().eq(TemplateAccess::getAccessId, id)));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		return SqlHelper.retBool(accessMapper.deleteBatchIds(idList))
			&& SqlHelper.retBool(roleAccessMapper.delete(new QueryWrapper<RoleAccess>().lambda().in(RoleAccess::getAccessId, idList)))
			&& SqlHelper.retBool(templateAccessMapper.delete(new QueryWrapper<TemplateAccess>().lambda().in(TemplateAccess::getAccessId, idList)));
	}

	@Override
	public boolean save(Access access) {
		checkBeforeSaveOrUpdate(access, false);
		return SqlHelper.retBool(accessMapper.insert(access));
	}

	@Override
	public boolean updateById(Access access) {
		checkBeforeSaveOrUpdate(access, true);
		return SqlHelper.retBool(accessMapper.updateById(access));
	}

	private void checkBeforeSaveOrUpdate(Access access, boolean isUpdate) {
		if (productMapper.selectCount(new QueryWrapper<Product>().lambda().eq(Product::getProductId, access.getProductId())) != 1) {
			throw new AssassinServiceException(AssassinConstant.ACCESS_PRODUCT_NULL_CODE, messageSource.getMessage("access.product.null", null, LocaleContextHolder.getLocale()));
		}
		if (accessMapper.selectCount(new QueryWrapper<Access>().lambda()
			.eq(Access::getAccessName, access.getAccessName())
			.ne(isUpdate, Access::getAccessId, access.getAccessId())
			.eq(Access::getProductId, access.getProductId())
			.apply(null == access.getParentId() ? "( parent_id IS NULL OR parent_id = '' )" : "parent_id = '" + access.getParentId() + "'")) > 0) {
			throw new AssassinServiceException(AssassinConstant.ACCESS_NAME_REPEAT_CODE, messageSource.getMessage("access.accessName.repeat", null, LocaleContextHolder.getLocale()));
		}
		if (accessMapper.selectCount(new QueryWrapper<Access>().lambda()
			.eq(Access::getAccessAlias, access.getAccessAlias())
			.ne(isUpdate, Access::getAccessId, access.getAccessId())
			.eq(Access::getProductId, access.getProductId())) > 0) {
			throw new AssassinServiceException(AssassinConstant.ACCESS_ALIAS_REPEAT_CODE, messageSource.getMessage("access.accessAlias.repeat", null, LocaleContextHolder.getLocale()));
		}
	}
}
