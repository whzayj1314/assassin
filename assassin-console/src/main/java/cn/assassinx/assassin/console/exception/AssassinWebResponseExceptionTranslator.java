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

package cn.assassinx.assassin.console.exception;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;

/**
 * @author Barton
 */
@AllArgsConstructor
public class AssassinWebResponseExceptionTranslator implements WebResponseExceptionTranslator {

	private final MessageSource messageSource;

	@Override
	public ResponseEntity translate(Exception e) {
		OAuth2Exception oAuth2Exception = null;
		if (InternalAuthenticationServiceException.class.isInstance(e) || InvalidGrantException.class.isInstance(e)) {
			if (UserRoleNotFoundException.class.isInstance(e.getCause()) || UserAccessNotFoundException.class.isInstance(e.getCause())) {
				oAuth2Exception = (OAuth2Exception) e.getCause();
				return ResponseEntity
					.status(oAuth2Exception.getHttpErrorCode())
					.body(new AssassinOAuth2Exception(messageSource.getMessage(oAuth2Exception.getOAuth2ErrorCode(), null, LocaleContextHolder.getLocale()), oAuth2Exception.getHttpErrorCode()));
			}
			return ResponseEntity
				.status(HttpStatus.UNAUTHORIZED)
				.body(new AssassinOAuth2Exception(e.getMessage(), HttpStatus.UNAUTHORIZED.value()));
		}
		oAuth2Exception = (OAuth2Exception) e;
		return ResponseEntity
			.status(oAuth2Exception.getHttpErrorCode())
			.body(new AssassinOAuth2Exception(oAuth2Exception.getOAuth2ErrorCode(), oAuth2Exception.getHttpErrorCode()));
	}
}
