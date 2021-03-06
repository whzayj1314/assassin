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

package cn.assassinx.assassin.sample.mutli;

import cn.assassinx.assassin.client.annotation.EnableAssassinResourceServer;
import cn.assassinx.assassin.client.util.AssassinResult;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Barton
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableAssassinResourceServer
public class AssassinMutliTenantSampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssassinMutliTenantSampleApplication.class, args);
	}

	@RestController
	@RequestMapping("/test")
	public class TestController{

		@GetMapping("/1")
		@PreAuthorize("@access.accessed('test1')")
		public AssassinResult test1(){
			return new AssassinResult("test1");
		}

		@GetMapping("/2")
		@PreAuthorize("@access.accessed('test2')")
		public AssassinResult test2(){
			return new AssassinResult("test2");
		}

	}

}
