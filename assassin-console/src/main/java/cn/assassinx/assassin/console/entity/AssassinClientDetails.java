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


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Barton
 */
@Data
@TableName("oauth_client_details")
public class AssassinClientDetails implements Serializable {

	private static final long serialVersionUID = 1L;
	@TableId(value = "client_id", type = IdType.INPUT)
	@NotNull(message = "{client.clientId.notNull}")
	private String clientId;
	@NotNull(message = "{client.clientSecret.notNull}")
	private String clientSecret;
	private String scope;
	@NotNull(message = "{client.authorizedGrantTypes.notNull}")
	private String authorizedGrantTypes;
	@NotNull(message = "{client.productId.notNull}")
	private String productId;
	@NotNull(message = "{client.tokenRuleId.notNull}")
	private String tokenRuleId;
	private String remark;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createTime;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updateTime;
	@TableField(fill = FieldFill.INSERT)
	private String createUserId;
	@TableField(fill = FieldFill.UPDATE)
	private String updateUserId;
}
