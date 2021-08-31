package io.vertx.up.uca.rs.argument;

import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.exception.web._415PointDefineException;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.rs.Filler;

import java.util.List;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class PointFiller implements Filler {
    @Override
    public Object apply(final String name, final Class<?> paramType, final RoutingContext datum) {
        /*
         * First check the `paramType`, it must be following
         * 1. JsonArray
         * 2. List<T>
         */
        final boolean valid = Objects.nonNull(paramType)
            && (paramType.isAssignableFrom(List.class)
            || JsonArray.class == paramType
        );
        Fn.out(!valid, _415PointDefineException.class, this.getClass(), paramType);

        final String literal = datum.request().getParam(name);
        return null;
    }
}
