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
import cn.assassinx.assassin.console.dto.TenantTemplateDTO;
import cn.assassinx.assassin.console.entity.Tenant;
import cn.assassinx.assassin.console.service.IProductTemplateService;
import cn.assassinx.assassin.console.service.ITenantService;
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
@RequestMapping("/tenant")
public class TenantController {

	private final ITenantService tenantService;

	private final IProductTemplateService productTemplateService;

	@GetMapping("/page")
	public AssassinResult page(Page page, Tenant tenant) {
		LambdaQueryWrapper<Tenant> lambdaQueryWrapper = new QueryWrapper<Tenant>().lambda();
		if (null != tenant.getTenantName()) {
			lambdaQueryWrapper.like(Tenant::getTenantName, tenant.getTenantName());
		}
		return new AssassinResult(tenantService.page(page, lambdaQueryWrapper));
	}

	@GetMapping("/{tenantId}")
	@PreAuthorize("@access.accessed('tenant_view')")
	public AssassinResult detail(@PathVariable String tenantId) {
		return new AssassinResult(tenantService.getById(tenantId));
	}

	@PostMapping
	@PreAuthorize("@access.accessed('tenant_add')")
	public AssassinResult save(@RequestBody @Valid Tenant tenant) {
		return new AssassinResult(tenantService.save(tenant));
	}

	@PutMapping
	@PreAuthorize("@access.accessed('tenant_edit')")
	public AssassinResult update(@RequestBody @Valid Tenant tenant) {
		return new AssassinResult(tenantService.updateById(tenant));
	}

	@DeleteMapping("/{tenantId}")
	@PreAuthorize("@access.accessed('tenant_delete')")
	public AssassinResult delete(@PathVariable String tenantId) {
		return new AssassinResult(tenantService.removeById(tenantId));
	}

	@DeleteMapping("/batch/{tenantIds}")
	@PreAuthorize("@access.accessed('tenant_delete')")
	public AssassinResult deleteBatch(@PathVariable String tenantIds) {
		return new AssassinResult(tenantService.removeByIds(Arrays.asList(tenantIds.split(","))));
	}

	@GetMapping("/transfer")
	public AssassinResult transfer() {
		return new AssassinResult(productTemplateService.findTemplateTransferVOList());
	}

	@GetMapping("/target/{tenantId}")
	public AssassinResult target(@PathVariable String tenantId) {
		return new AssassinResult(productTemplateService.findTargetKeysByTenantId(tenantId));
	}

	@PostMapping("/tenantTemplate")
	@PreAuthorize("@access.accessed('tenant_template')")
	public AssassinResult saveTenantTemplate(@RequestBody TenantTemplateDTO tenantTemplateDTO) {
		tenantService.saveTenantTemplate(tenantTemplateDTO.getTenantId(), tenantTemplateDTO.getTemplateIds());
		return new AssassinResult(null);
	}
}
