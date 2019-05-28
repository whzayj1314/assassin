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

/**
 * @author Barton
 */
public class SecurityConstant {

	public static final String DEFAULT_FIND_STATEMENT = "SELECT" +
		" a.client_id," +
		" CONCAT(DEFAULT_FIND_STATEMENT '{noop}', a.client_secret ) AS client_secret," +
		" GROUP_CONCAT(DISTINCT(d.app_name)) AS resource_ids," +
		" a.scope," +
		" a.authorized_grant_types," +
		" a.web_server_redirect_uri," +
		" a.authorities," +
		" a.access_token_validity," +
		" a.refresh_token_validity," +
		" a.additional_information," +
		" a.autoapprove" +
		" FROM" +
		" oauth_client_details a" +
		" JOIN product b ON a.product_id = b.product_id AND b.del_flag = '0'" +
		" JOIN product_app c ON b.product_id = c.product_id" +
		" JOIN app d ON c.app_id = d.app_id and d.del_flag = '0'" +
		" GROUP BY a.client_id" +
		" ORDER BY" +
		" client_id";

	/**
	 * 如果使用数据库计算token的有效时间，则不能使用缓存
	 * public static final String DEFAULT_SELECT_STATEMENT = "select client_id, CONCAT('{noop}',client_secret) AS client_secret, resource_ids, scope, authorized_grant_types, " +
	 * "web_server_redirect_uri, authorities, TIMESTAMPDIFF(SECOND,CURRENT_TIME,CONCAT(DATE_FORMAT(DATE_ADD(CURRENT_DATE,interval 1 day),'%Y-%m-%d'),' 03:00:00')) AS access_token_validity, refresh_token_validity, additional_information, " +
	 * "autoapprove from oauth_client_details where client_id = ?";
	 */
	public static final String DEFAULT_SELECT_STATEMENT = "SELECT" +
		" a.client_id," +
		" CONCAT( '{noop}', a.client_secret ) AS client_secret," +
		" GROUP_CONCAT(DISTINCT(d.app_name)) AS resource_ids," +
		" a.scope," +
		" a.authorized_grant_types," +
		" a.web_server_redirect_uri," +
		" a.authorities," +
		" a.access_token_validity," +
		" a.refresh_token_validity," +
		" a.additional_information," +
		" a.autoapprove" +
		" FROM" +
		" oauth_client_details a" +
		" JOIN product b ON a.product_id = b.product_id AND b.del_flag = '0'" +
		" JOIN product_app c ON b.product_id = c.product_id" +
		" JOIN app d ON c.app_id = d.app_id AND d.del_flag ='0'" +
		" WHERE a.client_id = ?" +
		" GROUP BY a.client_id";

	public static final String CLIENT_CACHE_KEY = "oauth:client:details:";
}
