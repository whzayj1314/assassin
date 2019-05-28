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

import java.util.List;

/**
 * @author Barton
 */
public interface INode {

	/**
	 * 获取主键Id
	 *
	 * @return
	 */
	String getId();

	/**
	 * 获取父亲Id
	 *
	 * @return
	 */
	String getParentId();

	/**
	 * 获取孩子
	 *
	 * @return
	 */
	List<INode> getChildren();
}
