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
import cn.assassinx.assassin.console.dto.RoleAccessDTO;
import cn.assassinx.assassin.console.entity.Role;
import cn.assassinx.assassin.console.service.IRoleService;
import cn.assassinx.assassin.console.valid.Mutli;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;


/**
 * @author Barton
 */
@RestController
@AllArgsConstructor
@RequestMapping("/v1/tenant/role")
public class TenantRoleController {

	private final IRoleService roleService;

	@GetMapping("/page/{tenantId}")
	public AssassinResult getRolePage(Page page, @PathVariable String tenantId, String productId) {
		return new AssassinResult(roleService.selectTenantRolePage(page, tenantId, productId));
	}

	@PostMapping
	public AssassinResult save(@RequestBody @Validated({Mutli.class}) Role role) {
		return new AssassinResult(roleService.save(role));
	}

	@PutMapping
	public AssassinResult update(@RequestBody @Validated({Mutli.class}) Role role) {
		return new AssassinResult(roleService.updateById(role));
	}

	@DeleteMapping("/{roleId}")
	public AssassinResult delete(@PathVariable String roleId) {
		return new AssassinResult(roleService.removeById(roleId));
	}

	@DeleteMapping("/batch/{roleIds}")
	public AssassinResult deleteBatch(@PathVariable String roleIds) {
		return new AssassinResult(roleService.removeByIds(Arrays.asList(roleIds.split(","))));
	}

	@PostMapping("/access")
	public AssassinResult saveRoleAccess(@RequestBody RoleAccessDTO roleAccessDTO) {
		roleService.saveRoleAccess(roleAccessDTO.getRoleId(), roleAccessDTO.getAccessIds());
		return new AssassinResult(null);
	}
}
