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

package cn.assassinx.assassin.tool.minio.service.impl;

import cn.assassinx.assassin.tool.minio.repository.MinioRepository;
import cn.assassinx.assassin.tool.minio.service.IMinioService;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Barton
 */
@Service
@AllArgsConstructor
public class MinioServiceImpl implements IMinioService {

	private final MinioRepository minioRepository;

	@Override
	@SneakyThrows
	public String upload(String bucket, MultipartFile file) {
		String fileName = IdUtil.fastSimpleUUID() + "." + getExt(file.getOriginalFilename());
		minioRepository.put(bucket, fileName, file.getInputStream(), file.getContentType());
		return fileName;
	}

	/**
	 * 得到文件后缀名
	 *
	 * @param filename 文件完整名称
	 * @return 文件后缀
	 */
	private String getExt(String filename) {
		String extName = StrUtil.subAfter(filename, '.', true);
		return StrUtil.isNotBlank(extName) ? extName : "";
	}
}
