package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import io.vertx.up.eon.Values;

import java.util.Locale;

/**
 * #「Tp」Jackson Naming Strategy
 *
 * This component is `plugin` to resolving java bean specification in jackson here.
 *
 * zero framework support jooq engine as default, when the user want to do serialization for Jooq generated code, this
 * component could detect `getX` and `isX` to uniform identifying to replaced different java bean method here.
 *
 * 1. Situation 1: The type of boolean has been generated to `isX` as get bean method.
 * 2. Situation 2: The type of boolean has been kept in `getX` as get bean method.
 *
 * To uniform this kind of java bean get method specification, here zero provide small fix of naming resolution.
 *
 * @author lang
 */
public class OriginalNamingStrategy extends PropertyNamingStrategy {

    public static PropertyNamingStrategy JOOQ_NAME = new OriginalNamingStrategy();

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
        final String firstLetter = String.valueOf(fieldName.charAt(Values.IDX));
        return firstLetter.toLowerCase(Locale.getDefault()) + fieldName.substring(1);
    }
}
