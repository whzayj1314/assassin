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

package cn.assassinx.assassin.console.mapper;

import cn.assassinx.assassin.console.entity.TenantTemplate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Barton
 */
public interface TenantTemplateMapper extends BaseMapper<TenantTemplate> {

	/**
	 * 新增租户模版
	 *
	 * @param tenantId
	 * @param templateIds
	 */
	void insertTenantTemplate(@Param("tenantId") String tenantId, @Param("templateIds") List<String> templateIds);
}
