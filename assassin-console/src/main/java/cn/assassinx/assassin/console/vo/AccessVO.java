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

package cn.assassinx.assassin.console.vo;

import cn.assassinx.assassin.console.entity.Access;
import cn.assassinx.assassin.console.vo.serializer.MenuSerializer;
import cn.assassinx.assassin.tool.tree.INode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Barton
 */
public class AccessVO extends Access implements INode {

	private static final long serialVersionUID = 1L;

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<INode> children;

	public void setChildren(List<INode> children) {
		this.children = children;
	}

	@Override
	public String getId() {
		return super.getAccessId();
	}

	@Override
	public List<INode> getChildren() {
		if (null == this.children) {
			this.children = new ArrayList<>();
			return this.children;
		}
		return this.children;
	}

	private String productName;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@JsonSerialize(using = MenuSerializer.class)
	private String title;

	private String key;

	private String value;

	public String getTitle() {
		return this.getAccessName();
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getKey() {
		return this.getAccessId();
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return this.getAccessId();
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "AccessVO{" +
			"children=" + children +
			", productName='" + productName + '\'' +
			", title='" + title + '\'' +
			", key='" + key + '\'' +
			", value='" + value + '\'' +
			'}';
	}
}
