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

package cn.assassinx.assassin.console.sentinel;

import cn.assassinx.assassin.common.constant.NacosConstant;
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import com.alibaba.csp.sentinel.init.InitFunc;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.List;

/**
 * init rules
 *
 * @author Barton
 */
public class DataSourceInitFunc implements InitFunc {

	@Override
	public void init() throws Exception {
		ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new NacosDataSource<>(NacosConstant.NACOS_ADDRESS, NacosConstant.SENTINEL_GROUP_ID,
			NacosConstant.APPLICATION_NAME + NacosConstant.SENTINEL_FLOW_DATA_ID + NacosConstant.ACTIVE + NacosConstant.NACOS_CONFIG_SUFFIX_JSON,
			source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {
			}));
		FlowRuleManager.register2Property(flowRuleDataSource.getProperty());
		ReadableDataSource<String, List<DegradeRule>> degradeRuleDataSource = new NacosDataSource<>(NacosConstant.NACOS_ADDRESS, NacosConstant.SENTINEL_GROUP_ID,
			NacosConstant.APPLICATION_NAME + NacosConstant.SENTINEL_DEGRADE_DATA_ID + NacosConstant.ACTIVE + NacosConstant.NACOS_CONFIG_SUFFIX_JSON,
			source -> JSON.parseObject(source, new TypeReference<List<DegradeRule>>() {
			}));
		DegradeRuleManager.register2Property(degradeRuleDataSource.getProperty());
	}
}
