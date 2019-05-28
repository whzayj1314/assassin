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

package cn.assassinx.assassin.console.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author Barton
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("access")
public class Access extends BaseEntity {

	private static final long serialVersionUID = 1L;
	@TableId(value = "access_id", type = IdType.UUID)
	private String accessId;
	@NotNull(message = "{access.accessType.notNull}")
	private String accessType;
	@NotNull(message = "{access.productId.notNull}")
	private String productId;
	private String parentId;
	@NotNull(message = "{access.accessName.notNull}")
	private String accessName;
	@NotNull(message = "{access.accessAlias.notNull}")
	private String accessAlias;
	private String uri;
	private String remark;
	private Integer sort;
}
