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
import cn.assassinx.assassin.console.dto.TemplateAccessDTO;
import cn.assassinx.assassin.console.entity.ProductTemplate;
import cn.assassinx.assassin.console.service.IProductTemplateService;
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
@RequestMapping("/template")
public class ProductTemplateController {

	private final IProductTemplateService productTemplateService;

	@GetMapping("/list/all")
	public AssassinResult listAll() {
		return new AssassinResult(productTemplateService.list(new QueryWrapper<ProductTemplate>().lambda().orderByAsc(ProductTemplate::getProductId, ProductTemplate::getCreateTime)));
	}

	@GetMapping("/page")
	public AssassinResult page(Page page, ProductTemplate productTemplate) {
		return new AssassinResult(productTemplateService.findProductTemplatePage(page, productTemplate));
	}

	@GetMapping("/{templateId}")
	@PreAuthorize("@access.accessed('template_view')")
	public AssassinResult detail(@PathVariable String templateId) {
		return new AssassinResult(productTemplateService.getById(templateId));
	}

	@PostMapping
	@PreAuthorize("@access.accessed('template_add')")
	public AssassinResult save(@RequestBody @Valid ProductTemplate productTemplate) {
		return new AssassinResult(productTemplateService.save(productTemplate));
	}

	@PutMapping
	@PreAuthorize("@access.accessed('template_edit')")
	public AssassinResult update(@RequestBody @Valid ProductTemplate productTemplate) {
		return new AssassinResult(productTemplateService.updateById(productTemplate));
	}

	@DeleteMapping("/{templateId}")
	@PreAuthorize("@access.accessed('template_delete')")
	public AssassinResult delete(@PathVariable String templateId) {
		return new AssassinResult(productTemplateService.removeById(templateId));
	}

	@DeleteMapping("/batch/{templateIds}")
	@PreAuthorize("@access.accessed('template_delete')")
	public AssassinResult deleteBatch(@PathVariable String templateIds) {
		return new AssassinResult(productTemplateService.removeByIds(Arrays.asList(templateIds.split(","))));
	}

	@GetMapping("/checked/{templateId}")
	public AssassinResult checkedKeys(@PathVariable String templateId) {
		return new AssassinResult(productTemplateService.findCheckedKeysByTemplateId(templateId));
	}

	@PostMapping("/templateAccess")
	@PreAuthorize("@access.accessed('template_access')")
	public AssassinResult saveTemplateAccess(@RequestBody TemplateAccessDTO templateAccessDTO) {
		productTemplateService.saveTemplateAccess(templateAccessDTO.getTemplateId(), templateAccessDTO.getAccessIds());
		return new AssassinResult(null);
	}
}
