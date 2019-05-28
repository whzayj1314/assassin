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
import cn.assassinx.assassin.console.entity.Access;
import cn.assassinx.assassin.console.service.IAccessService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Barton
 */
@RestController
@AllArgsConstructor
@RequestMapping("/access")
public class AccessController {

	private final IAccessService accessService;

	@GetMapping("/tree")
	public AssassinResult tree(Access access) {
		return new AssassinResult(accessService.findAccessTree(access));
	}

	@GetMapping("/{accessId}")
	@PreAuthorize("@access.accessed('access_view')")
	public AssassinResult detail(@PathVariable String accessId) {
		return new AssassinResult(accessService.getById(accessId));
	}

	@PostMapping
	@PreAuthorize("@access.accessed('access_add')")
	public AssassinResult save(@RequestBody @Valid Access access) {
		return new AssassinResult(accessService.save(access));
	}

	@PutMapping
	@PreAuthorize("@access.accessed('access_edit')")
	public AssassinResult update(@RequestBody @Valid Access access) {
		return new AssassinResult(accessService.updateById(access));
	}

	@DeleteMapping("/{accessId}")
	@PreAuthorize("@access.accessed('access_delete')")
	public AssassinResult delete(@PathVariable String accessId) {
		return new AssassinResult(accessService.removeById(accessId));
	}
}
