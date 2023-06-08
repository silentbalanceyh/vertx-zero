package io.vertx.up.boot.origin;

import io.horizon.eon.VMessage;
import io.horizon.uca.log.Annal;
import io.vertx.up.annotations.EndPoint;

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
        LOGGER.info(VMessage.Inquirer.ENDPOINT, endpoints.size());
        return endpoints;
    }
}
