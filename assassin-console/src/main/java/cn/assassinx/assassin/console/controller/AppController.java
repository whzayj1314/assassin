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
import cn.assassinx.assassin.console.entity.App;
import cn.assassinx.assassin.console.service.IAppService;
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
@RequestMapping("/app")
public class AppController {

	private final IAppService appService;

	@GetMapping("/list/all")
	public AssassinResult listAll() {
		return new AssassinResult(appService.list(new QueryWrapper<App>().lambda().orderByAsc(App::getCreateTime)));
	}

	@GetMapping("/page")
	public AssassinResult page(Page page, App app) {
		return new AssassinResult(null == app.getAppName()
			? appService.page(page, new QueryWrapper<App>().lambda())
			: appService.page(page, new QueryWrapper<App>().lambda().like(App::getAppName, app.getAppName())));
	}

	@GetMapping("/{appId}")
	@PreAuthorize("@access.accessed('app_view')")
	public AssassinResult detail(@PathVariable String appId) {
		return new AssassinResult(appService.getById(appId));
	}

	@PostMapping
	@PreAuthorize("@access.accessed('app_add')")
	public AssassinResult save(@RequestBody @Valid App app) {
		return new AssassinResult(appService.save(app));
	}

	@PutMapping
	@PreAuthorize("@access.accessed('app_edit')")
	public AssassinResult update(@RequestBody @Valid App app) {
		return new AssassinResult(appService.updateById(app));
	}

	@DeleteMapping("/{appId}")
	@PreAuthorize("@access.accessed('app_delete')")
	public AssassinResult delete(@PathVariable String appId) {
		return new AssassinResult(appService.removeById(appId));
	}

	@DeleteMapping("/batch/{appIds}")
	@PreAuthorize("@access.accessed('app_delete')")
	public AssassinResult deleteBatch(@PathVariable String appIds) {
		return new AssassinResult(appService.removeByIds(Arrays.asList(appIds.split(","))));
	}
}
