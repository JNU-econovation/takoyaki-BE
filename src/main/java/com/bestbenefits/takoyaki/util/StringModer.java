package com.bestbenefits.takoyaki.util;

import org.springframework.stereotype.Component;

@Component
public class StringModer {
    public String toPascal(String target) {
        if (target == null || target.isEmpty()) {
            return target;
        }

        char[] chars = target.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);

        return new String(chars);
    }
}
