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

import cn.assassinx.assassin.console.entity.User;
import cn.assassinx.assassin.console.vo.UserVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Barton
 */
public interface UserMapper extends BaseMapper<User> {

	/**
	 * 根据clientId和account返回UserVo
	 *
	 * @param clientId
	 * @param account
	 * @return
	 */
	UserVO selectUserVoByClientIdAndAccount(@Param("clientId") String clientId, @Param("account") String account);

	/**
	 * 根据productId和account查询产品User
	 *
	 * @param productId
	 * @param account
	 * @return
	 */
	int selectCountUserRoleByProductIdAndAccount(@Param("productId") String productId, @Param("account") String account);
}
