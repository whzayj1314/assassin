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

package cn.assassinx.assassin.console.controller;

import cn.assassinx.assassin.client.util.AssassinResult;
import cn.assassinx.assassin.console.entity.TokenRule;
import cn.assassinx.assassin.console.service.ITokenRuleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;

/**
 * @author Barton
 */
@RestController
@AllArgsConstructor
@RequestMapping("/tokenRule")
public class TokenRuleController {

	private final ITokenRuleService tokenRuleService;

	@GetMapping("/list/all")
	public AssassinResult listAll() {
		return new AssassinResult(tokenRuleService.list(new QueryWrapper<TokenRule>().lambda().orderByAsc(TokenRule::getCreateTime)));
	}

	@GetMapping("/page")
	public AssassinResult page(Page page, TokenRule tokenRule) {
		LambdaQueryWrapper<TokenRule> lambdaQueryWrapper = new QueryWrapper<TokenRule>().lambda();
		if (null != tokenRule.getRuleName()) {
			lambdaQueryWrapper.like(TokenRule::getRuleName, tokenRule.getRuleName());
		}
		if (null != tokenRule.getRuleType()) {
			lambdaQueryWrapper.eq(TokenRule::getRuleType, tokenRule.getRuleType());
		}
		return new AssassinResult(tokenRuleService.page(page, lambdaQueryWrapper));
	}

	@GetMapping("/{tokenRuleId}")
	@PreAuthorize("@access.accessed('rule_view')")
	public AssassinResult detail(@PathVariable String tokenRuleId) {
		return new AssassinResult(tokenRuleService.getById(tokenRuleId));
	}

	@PostMapping
	@PreAuthorize("@access.accessed('rule_add')")
	public AssassinResult save(@RequestBody @Valid TokenRule tokenRule) {
		return new AssassinResult(tokenRuleService.save(tokenRule));
	}

	@PutMapping
	@PreAuthorize("@access.accessed('rule_edit')")
	public AssassinResult update(@RequestBody @Valid TokenRule tokenRule) {
		return new AssassinResult(tokenRuleService.updateById(tokenRule));
	}

	@DeleteMapping("/{tokenRuleId}")
	@PreAuthorize("@access.accessed('rule_delete')")
	public AssassinResult delete(@PathVariable String tokenRuleId) {
		return new AssassinResult(tokenRuleService.removeById(tokenRuleId));
	}

	@DeleteMapping("/batch/{tokenRuleIds}")
	@PreAuthorize("@access.accessed('rule_delete')")
	public AssassinResult deleteBatch(@PathVariable String tokenRuleIds) {
		return new AssassinResult(tokenRuleService.removeByIds(Arrays.asList(tokenRuleIds.split(","))));
	}
}
