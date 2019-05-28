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

package cn.assassinx.assassin.console.service;

import cn.assassinx.assassin.console.dto.TenantUserDTO;
import cn.assassinx.assassin.console.entity.Tenant;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Barton
 */
public interface ITenantService extends IService<Tenant> {

	/**
	 * 保存租户模版
	 *
	 * @param tenantId
	 * @param templateIds
	 */
	void saveTenantTemplate(String tenantId, List<String> templateIds);

	/**
	 * 保存租户用户
	 *
	 * @param tenantUserDTO
	 * @return
	 */
	void saveTenantUsers(TenantUserDTO tenantUserDTO);
}
