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

import cn.assassinx.assassin.console.vo.serializer.MenuSerializer;
import cn.assassinx.assassin.tool.tree.INode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Barton
 */
public class MenuVO implements INode, Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String path;
	@JsonSerialize(using = MenuSerializer.class)
	private String name;
	private String parentId;
	private String icon;
	private String exact;
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<INode> children;

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getParentId() {
		return parentId;
	}

	@Override
	public List<INode> getChildren() {
		if (null == children) {
			children = new ArrayList<>();
			return children;
		}
		return children;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public void setChildren(List<INode> children) {
		this.children = children;
	}

	public String getExact() {
		return exact;
	}

	public void setExact(String exact) {
		this.exact = exact;
	}

	@Override
	public String toString() {
		return "MenuVO{" +
			"id='" + id + '\'' +
			", path='" + path + '\'' +
			", name='" + name + '\'' +
			", parentId='" + parentId + '\'' +
			", icon='" + icon + '\'' +
			", exact='" + exact + '\'' +
			", children=" + children +
			'}';
	}
}
