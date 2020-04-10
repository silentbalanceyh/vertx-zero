package io.vertx.up.uca.web.origin;

import io.vertx.up.annotations.EndPoint;
import io.vertx.up.eon.Info;
import io.vertx.up.log.Annal;

import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 */
public class EndPointInquirer implements Inquirer<Set<Class<?>>> {

    private static final Annal LOGGER = Annal.get(EndPointInquirer.class);

    @Override
    public Set<Class<?>> scan(final Set<Class<?>> clazzes) {
        final Set<Class<?>> endpoints = clazzes.stream()
                .filter((item) -> item.isAnnotationPresent(EndPoint.class))
                .collect(Collectors.toSet());
        LOGGER.info(Info.SCANED_ENDPOINT, endpoints.size());
        return endpoints;
    }
}
