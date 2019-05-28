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
import cn.assassinx.assassin.console.entity.ProductTemplate;
import cn.assassinx.assassin.console.entity.TemplateAccess;
import cn.assassinx.assassin.console.entity.TenantTemplate;
import cn.assassinx.assassin.console.exception.AssassinServiceException;
import cn.assassinx.assassin.console.mapper.ProductMapper;
import cn.assassinx.assassin.console.mapper.ProductTemplateMapper;
import cn.assassinx.assassin.console.mapper.TemplateAccessMapper;
import cn.assassinx.assassin.console.mapper.TenantTemplateMapper;
import cn.assassinx.assassin.console.service.IProductTemplateService;
import cn.assassinx.assassin.console.vo.ProductTemplateVO;
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
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Barton
 */
@Service
@AllArgsConstructor
public class ProductTemplateServiceImpl extends ServiceImpl<ProductTemplateMapper, ProductTemplate> implements IProductTemplateService {

	private final ProductTemplateMapper productTemplateMapper;

	private final TemplateAccessMapper templateAccessMapper;

	private final TenantTemplateMapper tenantTemplateMapper;

	private final ProductMapper productMapper;

	private final MessageSource messageSource;

	@Override
	public IPage<ProductTemplateVO> findProductTemplatePage(Page page, ProductTemplate productTemplate) {
		return productTemplateMapper.selectProductTemplateVOPage(page, productTemplate);
	}

	@Override
	public List<String> findCheckedKeysByTemplateId(String templateId) {
		List<TemplateAccess> templateAccesses = templateAccessMapper.selectList(new QueryWrapper<TemplateAccess>().lambda().eq(TemplateAccess::getTemplateId, templateId));
		return templateAccesses.stream().map(templateAccess -> templateAccess.getAccessId()).collect(Collectors.toList());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveTemplateAccess(String templateId, List<String> accessIds) {
		templateAccessMapper.delete(new QueryWrapper<TemplateAccess>().lambda().eq(TemplateAccess::getTemplateId, templateId));
		if (null != accessIds && accessIds.size() > 0) {
			templateAccessMapper.insertTemplateAccess(templateId, accessIds);
		}
	}

	@Override
	public List<TransferVO> findTemplateTransferVOList() {
		return productTemplateMapper.selectTemplateTransferVOList();
	}

	@Override
	public List<String> findTargetKeysByTenantId(String tenantId) {
		return productTemplateMapper.selectTemplateIdsByTenantId(tenantId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean removeById(Serializable id) {
		return SqlHelper.retBool(productTemplateMapper.deleteById(id))
			&& SqlHelper.retBool(templateAccessMapper.delete(new QueryWrapper<TemplateAccess>().lambda().eq(TemplateAccess::getTemplateId, id)))
			&& SqlHelper.retBool(tenantTemplateMapper.delete(new QueryWrapper<TenantTemplate>().lambda().eq(TenantTemplate::getTemplateId, id)));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		return SqlHelper.retBool(productTemplateMapper.deleteBatchIds(idList))
			&& SqlHelper.retBool(templateAccessMapper.delete(new QueryWrapper<TemplateAccess>().lambda().in(TemplateAccess::getTemplateId, idList)))
			&& SqlHelper.retBool(tenantTemplateMapper.delete(new QueryWrapper<TenantTemplate>().lambda().in(TenantTemplate::getTemplateId, idList)));
	}

	@Override
	public boolean save(ProductTemplate productTemplate) {
		checkBeforeSaveOrUpdate(productTemplate, false);
		return SqlHelper.retBool(productTemplateMapper.insert(productTemplate));
	}

	@Override
	public boolean updateById(ProductTemplate productTemplate) {
		checkBeforeSaveOrUpdate(productTemplate, true);
		return SqlHelper.retBool(productTemplateMapper.updateById(productTemplate));
	}

	private void checkBeforeSaveOrUpdate(ProductTemplate productTemplate, boolean isUpdate) {
		if (productMapper.selectCount(new QueryWrapper<Product>().lambda()
			.eq(Product::getProductId, productTemplate.getProductId())
			.eq(Product::getProductType, AssassinConstant.PRODUCT_TYPE_MULTI_TENANT)) != 1) {
			throw new AssassinServiceException(AssassinConstant.TEMPLATE_PRODUCT_NULL_CODE, messageSource.getMessage("template.product.null", null, LocaleContextHolder.getLocale()));
		}
		if (productTemplateMapper.selectCount(new QueryWrapper<ProductTemplate>().lambda()
			.eq(ProductTemplate::getTemplateName, productTemplate.getTemplateName())
			.ne(isUpdate, ProductTemplate::getTemplateId, productTemplate.getTemplateId())
			.eq(ProductTemplate::getProductId, productTemplate.getProductId())) > 0) {
			throw new AssassinServiceException(AssassinConstant.TEMPLATE_NAME_REPEAT_CODE, messageSource.getMessage("template.name.repeat", null, LocaleContextHolder.getLocale()));
		}
	}
}
