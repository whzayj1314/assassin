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

package cn.assassinx.assassin.tool.minio.endpoint;

import cn.assassinx.assassin.tool.minio.service.IMinioService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Barton
 */
@RestController
@AllArgsConstructor
@RequestMapping("/minio")
public class MinioEndpoint {

	private final IMinioService minioService;

	@PostMapping("/{bucket}")
	public String upload(@PathVariable String bucket, @RequestBody MultipartFile file) {
		return minioService.upload(bucket, file);
	}
}
