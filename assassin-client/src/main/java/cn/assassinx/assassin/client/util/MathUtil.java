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

package cn.assassinx.assassin.client.util;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author Barton
 */
public class MathUtil {

	public static List<String> differenceSetLeft(List<String> a, List<String> b) {
		return a.stream().filter(item -> !b.contains(item)).collect(toList());
	}

	public static List<String> differenceSetRight(List<String> a, List<String> b) {
		return b.stream().filter(item -> !a.contains(item)).collect(toList());
	}

	public static List<String> differenceSetAndUnion(List<String> a, List<String> b) {
		List<String> reduce1 = a.stream().filter(item -> !b.contains(item)).collect(toList());
		List<String> reduce2 = b.stream().filter(item -> !a.contains(item)).collect(toList());
		reduce1.addAll(reduce2);
		return reduce1;
	}
}
