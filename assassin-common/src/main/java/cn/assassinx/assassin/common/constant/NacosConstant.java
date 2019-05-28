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

package cn.assassinx.assassin.common.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Barton
 */
@Component
public class NacosConstant {

	public static String NACOS_ADDRESS;

	@Value("${spring.cloud.nacos.config.server-addr}")
	public void setNacosAddress(String nacosAddress) {
		NACOS_ADDRESS = nacosAddress;
	}

	public static String ACTIVE;

	@Value("${spring.profiles.active}")
	public void setACTIVE(String active) {
		NacosConstant.ACTIVE = active;
	}

	public static String APPLICATION_NAME;

	@Value("${spring.application.name}")
	public void setApplicationName(String applicationName) {
		APPLICATION_NAME = applicationName;
	}

	public static final String SENTINEL_GROUP_ID = "SENTINEL";

	public static final String SENTINEL_FLOW_DATA_ID = "-flow-";

	public static final String SENTINEL_DEGRADE_DATA_ID = "-degrade-";

	public static final String GATEWAY_GROUP_ID = "GATEWAY";

	public static final String GATEWAY_ROUTER_DATA_ID = "-router-";

	public static final String NACOS_CONFIG_SUFFIX_JSON = ".json";
}
