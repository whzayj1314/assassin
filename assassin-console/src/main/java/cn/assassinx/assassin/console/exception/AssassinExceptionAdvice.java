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

import cn.assassinx.assassin.client.util.AssassinResult;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Barton
 */
@RestControllerAdvice
public class AssassinExceptionAdvice {

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public AssassinResult errorHandler(Exception ex) {
		return new AssassinResult(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null);
	}

	@ExceptionHandler(AssassinServiceException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public AssassinResult errorHandler(AssassinServiceException ex) {
		return new AssassinResult(HttpStatus.BAD_REQUEST.value(), ex.getMsg(), null);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public AssassinResult validationErrorHandler(MethodArgumentNotValidException ex) {
		List<String> errorInformation = ex.getBindingResult().getAllErrors()
			.stream()
			.map(ObjectError::getDefaultMessage)
			.collect(Collectors.toList());
		return new AssassinResult(HttpStatus.BAD_REQUEST.value(), errorInformation.toString(), null);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public AssassinResult validationErrorHandler(ConstraintViolationException ex) {
		List<String> errorInformation = ex.getConstraintViolations()
			.stream()
			.map(ConstraintViolation::getMessage)
			.collect(Collectors.toList());
		return new AssassinResult(HttpStatus.BAD_REQUEST.value(), errorInformation.toString(), null);
	}
}
