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

import cn.assassinx.assassin.console.async.TokenRemove;
import cn.assassinx.assassin.console.entity.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Barton
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {

	/**
	 * 保存用户角色
	 *
	 * @param userId
	 * @param roleIds
	 */
	void insertUserRole(@Param("userId") String userId, @Param("roleIds") List<String> roleIds);

	/**
	 * 查找因为角色变更而需要删除的token
	 *
	 * @param account
	 * @param roleIds
	 * @return
	 */
	List<TokenRemove> selectTokenRemoveByRoleIds(@Param("account") String account, @Param("roleIds") List<String> roleIds);

	/**
	 * 查找因为角色变更而需要删除的token
	 *
	 * @param roleId
	 * @return
	 */
	List<TokenRemove> selectTokenRemove(@Param("roleId") String roleId);

	/**
	 * 查询租户用户角色
	 *
	 * @param clientId
	 * @param userId
	 * @return
	 */
	List<UserRole> selectTenantUserRoleList(@Param("productId") String clientId, @Param("userId") String userId);
}
