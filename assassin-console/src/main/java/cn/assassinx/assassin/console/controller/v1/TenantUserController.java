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
import cn.assassinx.assassin.console.dto.TenantUserDTO;
import cn.assassinx.assassin.console.dto.UserRoleDTO;
import cn.assassinx.assassin.console.entity.User;
import cn.assassinx.assassin.console.service.ITenantService;
import cn.assassinx.assassin.console.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author Barton
 */
@RestController
@AllArgsConstructor
@RequestMapping("/v1/tenant/user")
public class TenantUserController {

	private final ITenantService tenantService;

	private final IUserService userService;

	@GetMapping("/page/{tenantId}")
	public AssassinResult page(Page page, @PathVariable String tenantId) {
		return new AssassinResult(userService.page(page, new QueryWrapper<User>().lambda()
			.inSql(User::getUserId, "SELECT user_id FROM tenant_user WHERE tenant_id = '" + tenantId + "'")));
	}

	@PostMapping
	public AssassinResult saveTenantUser(@RequestBody TenantUserDTO tenantUserDTO) {
		tenantService.saveTenantUsers(tenantUserDTO);
		return new AssassinResult(null);
	}

	@PostMapping("/role")
	public AssassinResult saveUserRole(@RequestBody UserRoleDTO userRoleDTO) {
		userService.saveTenantUserRole(userRoleDTO.getUserId(), userRoleDTO.getRoleIds(), userRoleDTO.getProductId());
		return new AssassinResult(null);
	}
}
