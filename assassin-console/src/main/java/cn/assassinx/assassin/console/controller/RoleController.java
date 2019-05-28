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
import cn.assassinx.assassin.console.dto.RoleAccessDTO;
import cn.assassinx.assassin.console.entity.Role;
import cn.assassinx.assassin.console.service.IRoleService;
import cn.assassinx.assassin.console.valid.Single;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * @author Barton
 */
@RestController
@AllArgsConstructor
@RequestMapping("/role")
public class RoleController {

	private final IRoleService roleService;

	@GetMapping("/list/all")
	public AssassinResult listAll() {
		return new AssassinResult(roleService.list(new QueryWrapper<Role>().lambda().orderByAsc(Role::getProductId, Role::getCreateTime)));
	}

	@GetMapping("/page")
	public AssassinResult page(Page page, Role role) {
		return new AssassinResult(roleService.selectInRolePage(page, role));
	}

	@GetMapping("/{roleId}")
	@PreAuthorize("@access.accessed('role_view')")
	public AssassinResult detail(@PathVariable String roleId) {
		return new AssassinResult(roleService.getById(roleId));
	}

	@PostMapping
	@PreAuthorize("@access.accessed('role_add')")
	public AssassinResult save(@RequestBody @Validated({Single.class}) Role role) {
		return new AssassinResult(roleService.save(role));
	}

	@PutMapping
	@PreAuthorize("@access.accessed('role_edit')")
	public AssassinResult update(@RequestBody @Validated({Single.class}) Role role) {
		return new AssassinResult(roleService.updateById(role));
	}

	@DeleteMapping("/{roleId}")
	@PreAuthorize("@access.accessed('role_delete')")
	public AssassinResult delete(@PathVariable String roleId) {
		return new AssassinResult(roleService.removeById(roleId));
	}

	@DeleteMapping("/batch/{roleIds}")
	@PreAuthorize("@access.accessed('role_delete')")
	public AssassinResult deleteBatch(@PathVariable String roleIds) {
		return new AssassinResult(roleService.removeByIds(Arrays.asList(roleIds.split(","))));
	}

	@GetMapping("/checked/{roleId}")
	public AssassinResult checkedKeys(@PathVariable String roleId) {
		return new AssassinResult(roleService.findCheckedKeysByRoleId(roleId));
	}

	@PostMapping("/roleAccess")
	@PreAuthorize("@access.accessed('role_access')")
	public AssassinResult saveRoleAccess(@RequestBody RoleAccessDTO roleAccessDTO) {
		roleService.saveRoleAccess(roleAccessDTO.getRoleId(), roleAccessDTO.getAccessIds());
		return new AssassinResult(null);
	}
}
