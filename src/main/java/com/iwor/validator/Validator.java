package com.iwor.validator;

public interface Validator<T> {

    ValidationResult validate(T object);
}
