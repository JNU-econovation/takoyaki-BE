package com.bestbenefits.takoyaki.config.constraint_validator;

import com.bestbenefits.takoyaki.config.annotation.EnumName;
import com.bestbenefits.takoyaki.exception.common.InvalidTypeValueException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
public class EnumNameValidator implements ConstraintValidator<EnumName, String> {
    private final static String methodName = "fromName";
    private Class<?> className;
    private Method fromName;

    @Override
    public void initialize(EnumName constraintAnnotation) {
        try {
            className = constraintAnnotation.enumClass();
            fromName = className.getDeclaredMethod(methodName, String.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(
                    "Enum Class " + constraintAnnotation.enumClass().getName() +
                    ": 해당 Enum 클래스는 " + methodName + " 메서드가 없습니다.");
        }
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        try {
            return className.isInstance(fromName.invoke(null, name));
        } catch (IllegalAccessException | InvocationTargetException e) {
            //TODO: SLF4J 사용해서 로그 남겨보기
            throw new RuntimeException(e.toString());
        } catch (InvalidTypeValueException e) {
            throw e;
        }
    }
}