package io.multiversum.db.executor.core.exceptions;

public class InvalidDateValueException extends Exception {

	private static final long serialVersionUID = -8437754578289051631L;

	public InvalidDateValueException(String value, String format) {
		super(String.format("The string: '%s' has an invalid date format, requested: %s", value, format));
	}
	
}
