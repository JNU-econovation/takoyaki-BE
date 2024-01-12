package com.bestbenefits.takoyaki.util;

import org.springframework.jdbc.support.JdbcUtils;

public class StringModer {
    private StringModer() {};

    public static String toPascal(String target) {
        if (target == null || target.isEmpty()) {
            return target;
        }

        char[] chars = target.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);

        return new String(chars);
    }

    public static String toSnakeCase(String target) {
        return JdbcUtils.convertPropertyNameToUnderscoreName(target);
    }
}
