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
import cn.assassinx.assassin.console.entity.AssassinClientDetails;
import cn.assassinx.assassin.console.service.IClientDetailsService;
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
@RequestMapping("/client")
public class AssassinClientDetailsController {
	private final IClientDetailsService clientDetailsService;

	@GetMapping("/page")
	public AssassinResult page(Page page, AssassinClientDetails assassinClientDetails) {
		LambdaQueryWrapper<AssassinClientDetails> lambdaQueryWrapper = new QueryWrapper<AssassinClientDetails>().lambda();
		if (null != assassinClientDetails.getClientId()) {
			lambdaQueryWrapper.like(AssassinClientDetails::getClientId, assassinClientDetails.getClientId());
		}
		if (null != assassinClientDetails.getProductId()) {
			lambdaQueryWrapper.eq(AssassinClientDetails::getProductId, assassinClientDetails.getProductId());
		}
		return new AssassinResult(clientDetailsService.page(page, lambdaQueryWrapper));
	}

	@GetMapping("/{clientId}")
	@PreAuthorize("@access.accessed('client_view')")
	public AssassinResult detail(@PathVariable String clientId) {
		return new AssassinResult(clientDetailsService.getAssassinClientDetailsVOById(clientId));
	}

	@PostMapping
	@PreAuthorize("@access.accessed('client_add')")
	public AssassinResult save(@RequestBody @Valid AssassinClientDetails assassinClientDetails) {
		assassinClientDetails.setScope("all");
		return new AssassinResult(clientDetailsService.save(assassinClientDetails));
	}

	@PutMapping
	@PreAuthorize("@access.accessed('client_edit')")
	public AssassinResult update(@RequestBody @Valid AssassinClientDetails assassinClientDetails) {
		assassinClientDetails.setScope("all");
		return new AssassinResult(clientDetailsService.updateById(assassinClientDetails));
	}

	@DeleteMapping("/{clientId}")
	@PreAuthorize("@access.accessed('client_delete')")
	public AssassinResult delete(@PathVariable String clientId) {
		return new AssassinResult(clientDetailsService.removeById(clientId));
	}

	@DeleteMapping("/batch/{clientIds}")
	@PreAuthorize("@access.accessed('client_delete')")
	public AssassinResult deleteBatch(@PathVariable String clientIds) {
		return new AssassinResult(clientDetailsService.removeByIds(Arrays.asList(clientIds.split(","))));
	}
}
