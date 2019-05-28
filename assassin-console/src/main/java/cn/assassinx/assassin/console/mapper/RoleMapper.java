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

import cn.assassinx.assassin.console.entity.Role;
import cn.assassinx.assassin.console.vo.RoleVO;
import cn.assassinx.assassin.console.vo.TransferVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Barton
 */
public interface RoleMapper extends BaseMapper<Role> {

	/**
	 * 分页查询RoleVO
	 *
	 * @param page
	 * @param role
	 * @return
	 */
	IPage<RoleVO> selectInnerRolePageVO(Page page, @Param("role") Role role);

	/**
	 * 查询穿梭框角色列表
	 *
	 * @return
	 */
	List<TransferVO> selectInnerRoleTransferVOList();

	/**
	 * 查询角色已有权限id
	 *
	 * @param userId
	 * @return
	 */
	List<String> selectRoleIdsByUserId(@Param("userId") String userId);
}
