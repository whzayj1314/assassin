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

package cn.assassinx.assassin.tool.minio.config;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Barton
 */
@Configuration
@AllArgsConstructor
public class MinioConfig {

	private final MinioProperties minioProperties;

	@Bean
	public MinioClient initMinioClient() throws InvalidPortException, InvalidEndpointException {
		MinioClient minioClient = new MinioClient(minioProperties.getEndpoint(), minioProperties.getAccessKey(), minioProperties.getSecretKey());
		minioClient.setTimeout(minioProperties.getConnectTimeout(), minioProperties.getWriteTimeout(), minioProperties.getReadTimeout());
		initBuckets(minioClient);
		return minioClient;
	}

	private void initBuckets(MinioClient minioClient) {
		minioProperties.getBuckets().forEach(bucket -> {
			try {
				if (minioClient.bucketExists(bucket)) {
					return;
				}
				minioClient.makeBucket(bucket);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
