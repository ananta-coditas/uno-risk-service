package com.ambry.exception;

import com.ambry.transformer.core.validator.ValidationErrors;

public class ValidationException extends RuntimeException {

	private ValidationErrors validationErrors;

	public ValidationException(String msg) {
	    super(msg);
	    validationErrors = new ValidationErrors();
	    validationErrors.rejectValue("", msg);
	}

	public ValidationException(String msg, ValidationErrors validationErrors) {
		super(msg);
		this.validationErrors = validationErrors;
	}

	public ValidationException(ValidationErrors validationErrors) {
		this.validationErrors = validationErrors;
	}

	public ValidationErrors getValidationErrors() {
		return validationErrors;
	}

	public void setValidationErrors(ValidationErrors validationErrors) {
		this.validationErrors = validationErrors;
	}
}
