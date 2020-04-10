package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import io.vertx.up.eon.Values;

import java.util.Locale;

public class OrignialNamingStrategy extends PropertyNamingStrategy {

    public static PropertyNamingStrategy JOOQ_NAME = new OrignialNamingStrategy();

    // 序列化时调用
    @Override
    public String nameForGetterMethod(final MapperConfig<?> config,
                                      final AnnotatedMethod method, final String defaultName) {
        final String methodName = method.getName();
        String fieldName = "";
        if (methodName.startsWith("get")) {
            fieldName = methodName.substring(3);
        } else if (methodName.startsWith("is")) {
            fieldName = methodName.substring(2);
        }
        return this.lowerFirst(fieldName);
    }

    private String lowerFirst(final String name) {
        final String firstLetter = String.valueOf(name.charAt(Values.IDX));
        return firstLetter.toLowerCase(Locale.getDefault()) + name.substring(1);
    }
}
