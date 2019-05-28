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

package cn.assassinx.assassin.tool.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Barton
 */
public class ForestNodeManager<T extends INode> {

	private List<T> list;

	private List<String> parentIds = new ArrayList<>();

	public ForestNodeManager(List<T> items) {
		list = items;
	}

	public INode getTreeNodeAT(String id) {
		for (INode forestNode : list) {
			if (forestNode.getId().equals(id)) {
				return forestNode;
			}
		}
		return null;
	}

	public void addParentId(String parentId) {
		parentIds.add(parentId);
	}

	public List<T> getRoot() {
		List<T> roots = new ArrayList<>();
		for (T forestNode : list) {
			if (forestNode.getParentId() == null || parentIds.contains(forestNode.getId())) {
				roots.add(forestNode);
			}
		}
		return roots;
	}
}
