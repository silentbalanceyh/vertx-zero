package io.vertx.up.uca.web.origin;

import io.horizon.eon.info.VMessage;
import io.vertx.up.annotations.EndPoint;
import io.horizon.uca.log.Annal;

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
        LOGGER.info(VMessage.INQUIRER_ENDPOINT, endpoints.size());
        return endpoints;
    }
}
