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

import cn.assassinx.assassin.console.entity.ProductTemplate;
import cn.assassinx.assassin.console.vo.ProductTemplateVO;
import cn.assassinx.assassin.console.vo.TransferVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Barton
 */
public interface ProductTemplateMapper extends BaseMapper<ProductTemplate> {

	/**
	 * 查询多租户产品模版页
	 *
	 * @param page
	 * @param productTemplate
	 * @return
	 */
	IPage<ProductTemplateVO> selectProductTemplateVOPage(Page page, @Param("template") ProductTemplate productTemplate);

	/**
	 * 查询穿梭框
	 *
	 * @return
	 */
	List<TransferVO> selectTemplateTransferVOList();

	/**
	 * 根据租户id查询模版id
	 *
	 * @param tenantId
	 * @return
	 */
	List<String> selectTemplateIdsByTenantId(@Param("tenantId") String tenantId);

}
