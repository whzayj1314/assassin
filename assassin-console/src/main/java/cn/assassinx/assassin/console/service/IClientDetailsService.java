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

import cn.assassinx.assassin.console.entity.AssassinClientDetails;
import cn.assassinx.assassin.console.vo.AssassinClientDetailsVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Barton
 */
public interface IClientDetailsService extends IService<AssassinClientDetails> {

	/**
	 * 根据clientId查询客户端详情
	 *
	 * @param clientId
	 * @return
	 */
	AssassinClientDetailsVO getAssassinClientDetailsVOById(String clientId);
}
