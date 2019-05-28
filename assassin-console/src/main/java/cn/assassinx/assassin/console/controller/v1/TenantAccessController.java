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

package cn.assassinx.assassin.console.controller.v1;

import cn.assassinx.assassin.client.util.AssassinResult;
import cn.assassinx.assassin.console.entity.Access;
import cn.assassinx.assassin.console.service.IAccessService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Barton
 */
@RestController
@AllArgsConstructor
@RequestMapping("/v1/tenant")
public class TenantAccessController {

	private final IAccessService accessService;

	@GetMapping("/access/menu")
	public AssassinResult menu(String productId) {
		return new AssassinResult(accessService.findTenantMenuVO(productId));
	}

	@GetMapping("/access/tree")
	public AssassinResult tree(Access access) {
		return new AssassinResult(accessService.findAccessTree(access));
	}
}
