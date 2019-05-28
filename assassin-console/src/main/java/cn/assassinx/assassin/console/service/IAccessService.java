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

import cn.assassinx.assassin.console.entity.Access;
import cn.assassinx.assassin.console.vo.AccessVO;
import cn.assassinx.assassin.console.vo.MenuVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Barton
 */
public interface IAccessService extends IService<Access> {

	/**
	 * 查询权限树
	 *
	 * @param access
	 * @return
	 */
	List<AccessVO> findAccessTree(Access access);

	/**
	 * 查找assassin菜单
	 *
	 * @return
	 */
	List<MenuVO> findAssassinMenuVO();

	/**
	 * 获取多租户产品菜单
	 *
	 * @param productId
	 * @return
	 */
	List<MenuVO> findTenantMenuVO(String productId);
}
