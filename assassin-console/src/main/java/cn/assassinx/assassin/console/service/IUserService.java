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

import cn.assassinx.assassin.console.entity.User;
import cn.assassinx.assassin.console.vo.UserVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Barton
 */
public interface IUserService extends IService<User> {

	/**
	 * 查询当前用户
	 *
	 * @return
	 */
	UserVO selectCurrentUserVO();

	/**
	 * 保存用户角色
	 *
	 * @param userId
	 * @param roleIds
	 */
	void saveUserRole(String userId, List<String> roleIds);

	/**
	 * 保存多租户用户角色
	 *
	 * @param userId
	 * @param roleIds
	 * @param productId
	 */
	void saveTenantUserRole(String userId, List<String> roleIds, String productId);
}
