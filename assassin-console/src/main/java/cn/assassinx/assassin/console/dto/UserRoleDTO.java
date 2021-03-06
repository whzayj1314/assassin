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

package cn.assassinx.assassin.console.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Barton
 */
@Data
public class UserRoleDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String productId;
	private String userId;
	private List<String> roleIds;
}
