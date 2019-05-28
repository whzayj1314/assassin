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

import cn.assassinx.assassin.console.entity.Product;
import cn.assassinx.assassin.console.vo.MutexRuleVO;
import cn.assassinx.assassin.console.vo.ProductVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Barton
 */
public interface IProductService extends IService<Product> {

	/**
	 * 根据clientId查找互斥的clients
	 *
	 * @param clientId
	 * @return
	 */
	List<String> selectMutexClientsByClientId(String clientId);

	/**
	 * 根据productId获取productVO
	 *
	 * @param productId
	 * @return
	 */
	ProductVO getProductVOById(String productId);

	/**
	 * 根据productId查询互斥规则
	 *
	 * @param productId
	 * @return
	 */
	List<MutexRuleVO> findMutexRuleVOByProductId(String productId);

	/**
	 * 新增产品
	 *
	 * @param productVO
	 * @return
	 */
	boolean save(ProductVO productVO);

	/**
	 * 保存产品
	 *
	 * @param productVO
	 * @return
	 */
	boolean updateById(ProductVO productVO);
}
