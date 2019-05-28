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

package cn.assassinx.assassin.console.service.impl;

import cn.assassinx.assassin.common.constant.AssassinConstant;
import cn.assassinx.assassin.console.entity.App;
import cn.assassinx.assassin.console.entity.ProductApp;
import cn.assassinx.assassin.console.exception.AssassinServiceException;
import cn.assassinx.assassin.console.mapper.AppMapper;
import cn.assassinx.assassin.console.mapper.ProductAppMapper;
import cn.assassinx.assassin.console.service.IAppService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author Barton
 */
@Service
@AllArgsConstructor
public class AppServiceImpl extends ServiceImpl<AppMapper, App> implements IAppService {

	private final AppMapper appMapper;

	private final ProductAppMapper productAppMapper;

	private final MessageSource messageSource;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean removeById(Serializable id) {
		return SqlHelper.retBool(appMapper.deleteById(id))
			&& SqlHelper.retBool(productAppMapper.delete(new QueryWrapper<ProductApp>().lambda().eq(ProductApp::getAppId, id)));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		return SqlHelper.retBool(appMapper.deleteBatchIds(idList))
			&& SqlHelper.retBool(productAppMapper.delete(new QueryWrapper<ProductApp>().lambda().in(ProductApp::getAppId, idList)));
	}

	@Override
	public boolean save(App app) {
		checkBeforeSaveOrUpdate(app, false);
		return SqlHelper.retBool(appMapper.insert(app));
	}

	@Override
	public boolean updateById(App app) {
		checkBeforeSaveOrUpdate(app, true);
		return SqlHelper.retBool(appMapper.updateById(app));
	}

	private void checkBeforeSaveOrUpdate(App app, boolean isUpdate) {
		if (appMapper.selectCount(new QueryWrapper<App>().lambda()
			.ne(isUpdate, App::getAppId, app.getAppId())
			.eq(App::getAppName, app.getAppName())) > 0) {
			throw new AssassinServiceException(AssassinConstant.APP_NAME_REPEAT_CODE, messageSource.getMessage("app.name.repeat", null, LocaleContextHolder.getLocale()));
		}
	}
}
