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

import cn.assassinx.assassin.console.entity.Role;
import cn.assassinx.assassin.console.vo.RoleVO;
import cn.assassinx.assassin.console.vo.TransferVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Barton
 */
public interface IRoleService extends IService<Role> {

	/**
	 * 分页查询内部产品角色VO
	 *
	 * @param page
	 * @param role
	 * @return
	 */
	IPage<RoleVO> selectInRolePage(Page page, Role role);

	/**
	 * 查找已有权限
	 *
	 * @param roleId
	 * @return
	 */
	List<String> findCheckedKeysByRoleId(String roleId);

	/**
	 * 保存角色权限
	 *
	 * @param roleId
	 * @param accessIds
	 */
	void saveRoleAccess(String roleId, List<String> accessIds);

	/**
	 * 查找角色穿梭框列表
	 *
	 * @return
	 */
	List<TransferVO> findRoleTransferVOList();

	/**
	 * 根据userId查询用户已有角色的roleId集合
	 *
	 * @param userId
	 * @return
	 */
	List<String> findTargetKeysByUserId(String userId);

	/**
	 * 查询租户角色列表
	 *
	 * @param page
	 * @param tenantId
	 * @param productId
	 * @return
	 */
	IPage<Role> selectTenantRolePage(Page page, String tenantId, String productId);
}
