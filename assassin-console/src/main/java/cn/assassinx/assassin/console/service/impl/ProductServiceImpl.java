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
import cn.assassinx.assassin.console.entity.*;
import cn.assassinx.assassin.console.exception.AssassinServiceException;
import cn.assassinx.assassin.console.mapper.*;
import cn.assassinx.assassin.console.service.IProductService;
import cn.assassinx.assassin.console.vo.MutexRuleVO;
import cn.assassinx.assassin.console.vo.ProductVO;
import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import java.util.List;

/**
 * @author Barton
 */
@Service
@AllArgsConstructor
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

	private final ProductMapper productMapper;

	private final ProductAppMapper productAppMapper;

	private final ProductTemplateMapper productTemplateMapper;

	private final AssassinClientDetailsMapper assassinClientDetailsMapper;

	private final RoleMapper roleMapper;

	private final RoleAccessMapper roleAccessMapper;

	private final AccessMapper accessMapper;

	private final TemplateAccessMapper templateAccessMapper;

	private final UserRoleMapper userRoleMapper;

	private final MessageSource messageSource;

	@Override
	public List<String> selectMutexClientsByClientId(String clientId) {
		Product product = productMapper.selectProductByClientId(clientId);
		if (null == product.getMutexRules()) {
			return null;
		}
		JSONArray rules = JSON.parseArray(product.getMutexRules());
		List<String> mutexClients = new ArrayList<>();
		if (null != rules) {
			rules.forEach(rule -> addClient(mutexClients, ((JSONObject) rule).getString("rule"), clientId));
		}
		return mutexClients;
	}

	@Override
	public ProductVO getProductVOById(String productId) {
		Product product = productMapper.selectById(productId);
		ProductVO productVO = new ProductVO();
		BeanUtil.copyProperties(product, productVO);
		List<ProductApp> productApps = productAppMapper.selectList(new QueryWrapper<ProductApp>().lambda().eq(ProductApp::getProductId, productId));
		List<String> appIds = new ArrayList<>();
		productApps.forEach(productApp -> appIds.add(productApp.getAppId()));
		productVO.setAppIds(appIds);
		return productVO;
	}

	@Override
	public List<MutexRuleVO> findMutexRuleVOByProductId(String productId) {
		return productMapper.selectMutexRuleVOByProductId(productId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean save(ProductVO productVO) {
		Product product = new Product();
		BeanUtil.copyProperties(productVO, product);
		checkBeforeSaveOrUpdate(product, false);
		productMapper.insert(product);
		resetProductApp(productVO.getAppIds(), product.getProductId());
		return true;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateById(ProductVO productVO) {
		Product product = new Product();
		BeanUtil.copyProperties(productVO, product);
		checkBeforeSaveOrUpdate(product, true);
		resetProductApp(productVO.getAppIds(), product.getProductId());
		return SqlHelper.retBool(productMapper.updateById(product));
	}

	private void checkBeforeSaveOrUpdate(Product product, boolean isUpdate) {
		if (productMapper.selectCount(new QueryWrapper<Product>().lambda()
			.ne(isUpdate, Product::getProductId, product.getProductId())
			.eq(Product::getProductName, product.getProductName())) > 0) {
			throw new AssassinServiceException(AssassinConstant.PRODUCT_NAME_REPEAT_CODE, messageSource.getMessage("product.name.repeat", null, LocaleContextHolder.getLocale()));
		}
	}

	public void resetProductApp(List<String> appIds, String productId) {
		productAppMapper.delete(new QueryWrapper<ProductApp>().lambda().eq(ProductApp::getProductId, productId));
		ProductApp productApp = new ProductApp();
		productApp.setProductId(productId);
		appIds.forEach(appId -> {
				productApp.setAppId(appId);
				productAppMapper.insert(productApp);
			}
		);
	}

	private void addClient(List<String> mutexClients, String rule, String clientId) {
		if (rule.contains(clientId)) {
			String[] clients = rule.split(",");
			if (clients[0].equals(clientId)) {
				mutexClients.add(clients[1]);
			} else {
				mutexClients.add(clients[0]);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean removeById(Serializable id) {
		return removeProduct(id) && removeClient(id) && removeRole(id) && removeAccess(id);
	}

	private boolean removeProduct(Serializable id) {
		return SqlHelper.retBool(productMapper.deleteById(id))
			&& SqlHelper.retBool(productAppMapper.delete(new QueryWrapper<ProductApp>().lambda().eq(ProductApp::getProductId, id)))
			&& SqlHelper.retBool(productTemplateMapper.delete(new QueryWrapper<ProductTemplate>().lambda().eq(ProductTemplate::getProductId, id)));
	}

	private boolean removeClient(Serializable id) {
		return SqlHelper.retBool(assassinClientDetailsMapper.delete(new QueryWrapper<AssassinClientDetails>()
			.lambda().eq(AssassinClientDetails::getProductId, id)));
	}

	private boolean removeRole(Serializable id) {
		return SqlHelper.retBool(roleMapper.delete(new QueryWrapper<Role>().lambda().eq(Role::getProductId, id)))
			&& SqlHelper.retBool(roleAccessMapper.delete(new QueryWrapper<RoleAccess>().lambda()
			.inSql(RoleAccess::getRoleId, "select role_id from role where product_id = '" + id + "'")))
			&& SqlHelper.retBool(userRoleMapper.delete(new QueryWrapper<UserRole>().lambda()
			.inSql(UserRole::getRoleId, "select role_id from role where product_id = '" + id + "'")));
	}

	private boolean removeAccess(Serializable id) {
		return SqlHelper.retBool(accessMapper.delete(new QueryWrapper<Access>().lambda().eq(Access::getProductId, id)))
			&& SqlHelper.retBool(templateAccessMapper.delete(new QueryWrapper<TemplateAccess>().lambda()
			.inSql(TemplateAccess::getAccessId, "select access_id from access where product_id = '" + id + "'")))
			&& SqlHelper.retBool(roleAccessMapper.delete(new QueryWrapper<RoleAccess>().lambda()
			.inSql(RoleAccess::getAccessId, "select access_id from access where product_id = '" + id + "'")));
	}
}
