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
import cn.assassinx.assassin.common.constant.SecurityConstant;
import cn.assassinx.assassin.console.entity.AssassinClientDetails;
import cn.assassinx.assassin.console.entity.Product;
import cn.assassinx.assassin.console.exception.AssassinServiceException;
import cn.assassinx.assassin.console.mapper.AssassinClientDetailsMapper;
import cn.assassinx.assassin.console.mapper.ProductMapper;
import cn.assassinx.assassin.console.service.IClientDetailsService;
import cn.assassinx.assassin.console.vo.AssassinClientDetailsVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.redis.core.RedisTemplate;
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
public class ClientDetailsServiceImpl extends ServiceImpl<AssassinClientDetailsMapper, AssassinClientDetails> implements IClientDetailsService {

	private final AssassinClientDetailsMapper assassinClientDetailsMapper;

	private final ProductMapper productMapper;

	private final MessageSource messageSource;

	private final RedisTemplate redisTemplate;

	@Override
	public AssassinClientDetailsVO getAssassinClientDetailsVOById(String clientId) {
		return assassinClientDetailsMapper.selectAssassinClientDetailsVOByClientId(clientId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean removeById(Serializable id) {
		String productId = assassinClientDetailsMapper.selectById(id).getProductId();
		Product product = new Product();
		product.setMutexRules("");
		product.setProductId(productId);
		return SqlHelper.retBool(assassinClientDetailsMapper.deleteById(id))
			&& SqlHelper.retBool(productMapper.updateById(product));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		List<String> productIdList = assassinClientDetailsMapper.selectBatchIds(idList)
			.stream().map(p -> p.getProductId()).distinct().collect(Collectors.toList());
		Product product = new Product();
		product.setMutexRules("");
		return SqlHelper.retBool(assassinClientDetailsMapper.deleteBatchIds(idList))
			&& SqlHelper.retBool(productMapper.update(product, new QueryWrapper<Product>().lambda().in(Product::getProductId, productIdList)));
	}

	@Override
	public boolean save(AssassinClientDetails assassinClientDetails) {
		checkBeforeSaveOrUpdate(assassinClientDetails, false);
		return SqlHelper.retBool(assassinClientDetailsMapper.insert(assassinClientDetails));
	}

	@Override
	public boolean updateById(AssassinClientDetails assassinClientDetails) {
		checkBeforeSaveOrUpdate(assassinClientDetails, true);
		redisTemplate.delete(SecurityConstant.CLIENT_CACHE_KEY + assassinClientDetails.getClientId());
		return SqlHelper.retBool(assassinClientDetailsMapper.updateById(assassinClientDetails));
	}

	private void checkBeforeSaveOrUpdate(AssassinClientDetails assassinClientDetails, boolean isUpdate) {
		if (productMapper.selectCount(new QueryWrapper<Product>().lambda().eq(Product::getProductId, assassinClientDetails.getProductId())) != 1) {
			throw new AssassinServiceException(AssassinConstant.CLIENT_PRODUCT_NULL_CODE, messageSource.getMessage("client.product.null", null, LocaleContextHolder.getLocale()));
		}
		if (assassinClientDetailsMapper.selectCount(new QueryWrapper<AssassinClientDetails>().lambda()
			.ne(isUpdate, AssassinClientDetails::getClientId, assassinClientDetails.getClientId())
			.eq(AssassinClientDetails::getClientId, assassinClientDetails.getClientId())) > 0) {
			throw new AssassinServiceException(AssassinConstant.CLIENT_ID_REPEAT_CODE, messageSource.getMessage("client.clientId.repeat", null, LocaleContextHolder.getLocale()));
		}
	}
}
