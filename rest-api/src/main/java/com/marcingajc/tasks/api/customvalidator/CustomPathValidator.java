package com.marcingajc.tasks.api.customvalidator;

import com.marcingajc.tasks.api.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CustomPathValidator implements ConstraintValidator <PathValidator, Long> {

    @Autowired
    TaskRepository repository;

    private long path;

    @Override
    public void initialize(PathValidator pathValidator) {
        path = pathValidator.value();
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {

        boolean result;

        if (value != null) {
            result = repository.existsById(value);
        } else {
            result = true;
        }
        return result;
    }
}
