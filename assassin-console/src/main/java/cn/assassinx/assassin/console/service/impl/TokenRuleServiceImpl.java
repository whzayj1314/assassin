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
import cn.assassinx.assassin.console.entity.AssassinClientDetails;
import cn.assassinx.assassin.console.entity.TokenRule;
import cn.assassinx.assassin.console.exception.AssassinServiceException;
import cn.assassinx.assassin.console.mapper.AssassinClientDetailsMapper;
import cn.assassinx.assassin.console.mapper.TokenRuleMapper;
import cn.assassinx.assassin.console.service.ITokenRuleService;
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

/**
 * @author Barton
 */
@Service
@AllArgsConstructor
public class TokenRuleServiceImpl extends ServiceImpl<TokenRuleMapper, TokenRule> implements ITokenRuleService {

	private final TokenRuleMapper tokenRuleMapper;

	private final AssassinClientDetailsMapper assassinClientDetailsMapper;

	private final MessageSource messageSource;

	@Override
	public Integer selectTokenValiditySecondsByClientId(String clientId) {
		TokenRule tokenRule = tokenRuleMapper.selectTokenRulesByClientId(clientId).get(0);
		if (null != tokenRule) {
			if ("0".equals(tokenRule.getRuleType())) {
				String[] tokenValidity = tokenRule.getTokenValidity().split(",");
				return SecurityUtil.getExpire(Integer.valueOf(tokenValidity[0]), Integer.valueOf(tokenValidity[1]), Integer.valueOf(tokenValidity[2]), Integer.valueOf(tokenValidity[3]));
			} else {
				return Integer.valueOf(tokenRule.getTokenValidity());
			}
		}
		return null;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean removeById(Serializable id) {
		AssassinClientDetails assassinClientDetails = new AssassinClientDetails();
		assassinClientDetails.setTokenRuleId("");
		return SqlHelper.retBool(tokenRuleMapper.deleteById(id))
			&& SqlHelper.retBool(assassinClientDetailsMapper.update(assassinClientDetails,
			new QueryWrapper<AssassinClientDetails>().lambda().eq(AssassinClientDetails::getTokenRuleId, id)));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		AssassinClientDetails assassinClientDetails = new AssassinClientDetails();
		assassinClientDetails.setTokenRuleId("");
		return SqlHelper.retBool(tokenRuleMapper.deleteBatchIds(idList))
			&& SqlHelper.retBool(assassinClientDetailsMapper.update(assassinClientDetails,
			new QueryWrapper<AssassinClientDetails>().lambda().in(AssassinClientDetails::getTokenRuleId, idList)));
	}

	@Override
	public boolean save(TokenRule tokenRule) {
		checkBeforeSaveOrUpdate(tokenRule, false);
		return SqlHelper.retBool(tokenRuleMapper.insert(tokenRule));
	}

	@Override
	public boolean updateById(TokenRule tokenRule) {
		checkBeforeSaveOrUpdate(tokenRule, true);
		return SqlHelper.retBool(tokenRuleMapper.updateById(tokenRule));
	}

	private void checkBeforeSaveOrUpdate(TokenRule tokenRule, boolean isUpdate) {
		if (tokenRuleMapper.selectCount(new QueryWrapper<TokenRule>().lambda()
			.ne(isUpdate, TokenRule::getRuleId, tokenRule.getRuleId())
			.eq(TokenRule::getRuleName, tokenRule.getRuleName())) > 0) {
			throw new AssassinServiceException(AssassinConstant.RULE_NAME_REPEAT_CODE, messageSource.getMessage("rule.name.repeat", null, LocaleContextHolder.getLocale()));
		}
	}
}
