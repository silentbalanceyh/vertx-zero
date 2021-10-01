package io.vertx.up.uca.web.origin;

import io.vertx.up.annotations.Wall;
import io.vertx.up.atom.secure.Aegis;
import io.vertx.up.secure.component.WallSpec;
import io.vertx.up.secure.config.AuthConfig;
import io.vertx.up.util.Ut;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * This class is for @Wall of security here.
 */
public class WallInquirer implements Inquirer<Set<Aegis>> {

    private final transient WallSpec specification = WallSpec.create(WallInquirer.class);

    @Override
    public Set<Aegis> scan(final Set<Class<?>> walls) {
        /* 1. Build result **/
        final Set<Aegis> wallSet = new TreeSet<>();
        final Set<Class<?>> wallClass = walls.stream()
            .filter((item) -> item.isAnnotationPresent(Wall.class))
            .collect(Collectors.toSet());
        if (!wallClass.isEmpty()) {
            /*
             * It means that you have set Wall and enable security configuration
             * wall Class verification
             */
            final ConcurrentMap<String, Class<?>> keys = new ConcurrentHashMap<>();
            final ConcurrentMap<String, AuthConfig> config = this.specification.verify(wallClass, keys);
            config.forEach((key, authConfig) -> {
                // Difference key setting
                final Class<?> cls = keys.get(key);
                // Set Information from class
                wallSet.add(this.create(authConfig, cls));
            });
        }
        /* 3. Transfer **/
        return wallSet;
    }

    private Aegis create(final AuthConfig input, final Class<?> clazz) {
        final Aegis aegis = new Aegis();
        aegis.setConfig(input.config());
        aegis.setType(input.wall());
        /* Extract basic data **/
        final Annotation annotation = clazz.getAnnotation(Wall.class);
        aegis.setOrder(Ut.invoke(annotation, "order"));
        aegis.setPath(Ut.invoke(annotation, "path"));
        aegis.setDefined(Ut.invoke(annotation, "define"));
        /* Proxy Creation with Wall Specification **/
        this.specification.mount(clazz, aegis);
        return aegis;
    }
}
