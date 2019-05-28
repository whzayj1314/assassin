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
import cn.assassinx.assassin.console.entity.Product;
import cn.assassinx.assassin.console.service.IProductService;
import cn.assassinx.assassin.console.vo.ProductVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Barton
 */
@RestController
@AllArgsConstructor
@RequestMapping("/product")
public class ProductController {

	private final IProductService productService;

	@GetMapping("/list/all")
	public AssassinResult listAll() {
		return new AssassinResult(productService.list(new QueryWrapper<Product>().lambda().orderByAsc(Product::getCreateTime)));
	}

	@GetMapping("/{productId}")
	@PreAuthorize("@access.accessed('product_view')")
	public AssassinResult detail(@PathVariable String productId) {
		return new AssassinResult(productService.getProductVOById(productId));
	}

	@PostMapping
	@PreAuthorize("@access.accessed('product_add')")
	public AssassinResult save(@RequestBody @Valid ProductVO productVO) {
		return new AssassinResult(productService.save(productVO));
	}

	@PutMapping
	@PreAuthorize("@access.accessed('product_edit')")
	public AssassinResult update(@RequestBody @Valid ProductVO productVO) {
		return new AssassinResult(productService.updateById(productVO));
	}

	@DeleteMapping("/{productId}")
	@PreAuthorize("@access.accessed('product_delete')")
	public AssassinResult delete(@PathVariable String productId) {
		return new AssassinResult(productService.removeById(productId));
	}

	@GetMapping("/in/amount")
	public AssassinResult InAmount() {
		return new AssassinResult(productService.count(new QueryWrapper<Product>().lambda().eq(Product::getProductType, "0")));
	}

	@GetMapping("/in/list")
	public AssassinResult InList() {
		return new AssassinResult(productService.list(new QueryWrapper<Product>().lambda().eq(Product::getProductType, "0").orderByAsc(Product::getCreateTime)));
	}

	@GetMapping("/multi/list")
	public AssassinResult multiList() {
		return new AssassinResult(productService.list(new QueryWrapper<Product>().lambda().eq(Product::getProductType, "1").orderByAsc(Product::getCreateTime)));
	}

	@GetMapping("/tenant/amount")
	public AssassinResult TenantAmount() {
		return new AssassinResult(productService.count(new QueryWrapper<Product>().lambda().eq(Product::getProductType, "1")));
	}

	@GetMapping("/mutex/{productId}")
	public AssassinResult mutexRule(@PathVariable String productId) {
		return new AssassinResult(productService.findMutexRuleVOByProductId(productId));
	}

	@PutMapping("/mutex")
	@PreAuthorize("@access.accessed('product_rule')")
	public AssassinResult saveMutexRule(@RequestBody Product product) {
		return new AssassinResult(productService.updateById(product));
	}
}
