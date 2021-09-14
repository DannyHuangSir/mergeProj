package com.twfhclife.generic.validator;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.twfhclife.generic.annotation.ValidateString;

public class StringValidator implements ConstraintValidator<ValidateString, String> {

	private List<String> valueList;

	@Override
	public void initialize(ValidateString constraintAnnotation) {
		valueList = new ArrayList<String>();
		for (String val : constraintAnnotation.acceptedValues()) {
			valueList.add(val.toUpperCase());
		}
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (!valueList.contains(value.toUpperCase())) {
			return false;
		}
		return true;
	}

}