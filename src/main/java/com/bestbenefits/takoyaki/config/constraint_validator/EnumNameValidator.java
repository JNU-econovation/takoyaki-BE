package com.bestbenefits.takoyaki.config.constraint_validator;

import com.bestbenefits.takoyaki.config.annotation.EnumName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EnumNameValidator implements ConstraintValidator<EnumName, String> {
    private List<String> validEnumList;

    @Override
    public void initialize(EnumName constraintAnnotation) {
        String methodName = "getName";
        Method method;

        try {
            method = constraintAnnotation.enumClass().getEnumConstants()[0].getClass().getMethod(methodName);
        } catch (Exception e) {
            throw new RuntimeException("해당 Enum에 이런 메서드는 없습니다", e);
        }

        validEnumList = Arrays.stream(constraintAnnotation.enumClass().getEnumConstants())
            .map(enumConstant -> {
                try {
                    return (String) method.invoke(enumConstant);
                } catch (Exception e) {
                    throw new RuntimeException("invoke 실패", e);
                }
            })
            .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        return validEnumList.contains(name);
    }
}
