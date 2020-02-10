package com.ucsmy.core.utils;

import com.ucsmy.core.vo.RetMsg;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

public class HibernateValidateUtils {
	private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	@Deprecated
	public static String getErrors(Object obj) {
		Set<ConstraintViolation<Object>> constraintViolations = validator.validate(obj);// 验证某个对象,，其实也可以只验证其中的某一个属性的

		for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
			return constraintViolation.getMessage();
		}
		return null;
	}

	public static RetMsg getError(Object obj) {
		Set<ConstraintViolation<Object>> constraintViolations = validator.validate(obj);// 验证某个对象,，其实也可以只验证其中的某一个属性的

		for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
			return RetMsg.error(constraintViolation.getMessage());
		}
		return RetMsg.success();
	}

	public static RetMsg getError(Object... objs) {
		for(Object obj : objs) {
			RetMsg msg = getError(obj);
			if(!msg.isSuccess()) {
				return msg;
			}
		}
		return RetMsg.success();
	}
}
