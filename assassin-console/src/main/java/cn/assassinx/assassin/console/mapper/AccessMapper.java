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

import cn.assassinx.assassin.console.entity.Access;
import cn.assassinx.assassin.console.vo.AccessVO;
import cn.assassinx.assassin.console.vo.MenuVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Barton
 */
public interface AccessMapper extends BaseMapper<Access> {

	/**
	 * 根据产品id，account查找权限list(内部产品)
	 *
	 * @param productId
	 * @param account
	 * @return
	 */
	List<String> selectAccessByProductIdAndAccount(@Param("productId") String productId, @Param("account") String account);

	/**
	 * 根据产品id，account查找权限list(多租户产品)
	 *
	 * @param productId
	 * @param account
	 * @return
	 */
	List<String> selectTenantAccessByProductIdAndAccount(@Param("productId") String productId, @Param("account") String account);

	/**
	 * 查询权限树
	 *
	 * @param access
	 * @return
	 */
	List<AccessVO> tree(Access access);

	/**
	 * 查询assassin权限菜单
	 *
	 * @param userId
	 * @param productId
	 * @param accessType
	 * @param accessIds
	 * @return
	 */
	List<MenuVO> selectAssassinMenuVO(@Param("userId") String userId, @Param("productId") String productId, @Param("accessType") String accessType, @Param("accessIds") List<String> accessIds);
}
