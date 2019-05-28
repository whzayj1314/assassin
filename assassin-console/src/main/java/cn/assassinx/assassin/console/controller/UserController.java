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
import cn.assassinx.assassin.client.util.SecurityUtil;
import cn.assassinx.assassin.console.dto.UserRoleDTO;
import cn.assassinx.assassin.console.entity.User;
import cn.assassinx.assassin.console.service.IAccessService;
import cn.assassinx.assassin.console.service.IRoleService;
import cn.assassinx.assassin.console.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Barton
 */
@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

	private final IUserService userService;

	private final IRoleService roleService;

	private final IAccessService accessService;

	@GetMapping("/currentUser")
	public AssassinResult getCurrentUser() {
		return new AssassinResult(userService.selectCurrentUserVO());
	}

	@GetMapping("/amount")
	public AssassinResult amount() {
		return new AssassinResult(userService.count());
	}

	@GetMapping("/page")
	public AssassinResult page(Page page, User user) {
		LambdaQueryWrapper<User> lambdaQueryWrapper = new QueryWrapper<User>().lambda();
		if (null != user.getAccount()) {
			lambdaQueryWrapper.like(User::getAccount, user.getAccount());
		}
		if (null != user.getNickname()) {
			lambdaQueryWrapper.like(User::getNickname, user.getNickname());
		}
		if (null != user.getMobilePhoneNumber()) {
			lambdaQueryWrapper.like(User::getMobilePhoneNumber, user.getMobilePhoneNumber());
		}
		if (null != user.getLockFlag()) {
			lambdaQueryWrapper.eq(User::getLockFlag, user.getLockFlag());
		}
		return new AssassinResult(userService.page(page, lambdaQueryWrapper));
	}

	@GetMapping("/{userId}")
	@PreAuthorize("@access.accessed('user_view')")
	public AssassinResult detail(@PathVariable String userId) {
		return new AssassinResult(userService.getById(userId));
	}

	@PostMapping
	@PreAuthorize("@access.accessed('user_add')")
	public AssassinResult save(@RequestBody @Valid User user) {
		user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
		user.setLockFlag(user.getLockFlag().equals("true") ? "0" : "1");
		return new AssassinResult(userService.saveOrUpdate(user));
	}

	@PutMapping
	@PreAuthorize("@access.accessed('user_edit')")
	public AssassinResult update(@RequestBody @Valid User user) {
		user.setPassword(null);
		user.setLockFlag(user.getLockFlag().equals("true") ? "0" : "1");
		return new AssassinResult(userService.saveOrUpdate(user));
	}

	@DeleteMapping("/{userId}")
	@PreAuthorize("@access.accessed('user_delete')")
	public AssassinResult delete(@PathVariable String userId) {
		return new AssassinResult(userService.removeById(userId));
	}

	@DeleteMapping("/batch/{userIds}")
	@PreAuthorize("@access.accessed('user_delete')")
	public AssassinResult deleteBatch(@PathVariable String userIds) {
		return new AssassinResult(userService.removeByIds(Arrays.asList(userIds.split(","))));
	}


	@GetMapping("/transfer")
	public AssassinResult transfer() {
		return new AssassinResult(roleService.findRoleTransferVOList());
	}

	@GetMapping("/target/{userId}")
	public AssassinResult target(@PathVariable String userId) {
		return new AssassinResult(roleService.findTargetKeysByUserId(userId));
	}

	@PostMapping("/userRole")
	@PreAuthorize("@access.accessed('user_role')")
	public AssassinResult saveUserRole(@RequestBody UserRoleDTO userRoleDTO) {
		userService.saveUserRole(userRoleDTO.getUserId(), userRoleDTO.getRoleIds());
		return new AssassinResult(null);
	}

	@GetMapping("/menu")
	public AssassinResult menu() {
		return new AssassinResult(accessService.findAssassinMenuVO());
	}

	@GetMapping("/access")
	public AssassinResult assess() {
		return new AssassinResult(SecurityUtil.getAuthentication().getAuthorities()
			.stream()
			.map(authority -> ((GrantedAuthority) authority).getAuthority())
			.collect(Collectors.toList()));
	}
}
