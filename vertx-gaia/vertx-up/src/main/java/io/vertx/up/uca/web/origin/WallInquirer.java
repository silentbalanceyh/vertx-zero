package io.vertx.up.uca.web.origin;

import io.reactivex.Observable;
import io.vertx.tp.error.WallDuplicatedException;
import io.vertx.tp.error.WallKeyMissingException;
import io.vertx.up.annotations.Wall;
import io.vertx.up.atom.secure.Aegis;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.secure.component.WallSpec;
import io.vertx.up.secure.config.AuthConfig;
import io.vertx.up.util.Ut;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * This class is for @Wall of security here.
 */
public class WallInquirer implements Inquirer<Set<Aegis>> {

    private static final Annal LOGGER = Annal.get(WallInquirer.class);

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
            final ConcurrentMap<String, AuthConfig> config = this.verify(wallClass, keys);
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
        WallSpec.create(clazz).verify().mount(aegis);
        return aegis;
    }

    /**
     * @param wallClses Security @Wall class
     * @param keysRef   critical class reference for security
     *
     * @return valid configuration that will be used in @Wall class.
     */
    private ConcurrentMap<String, AuthConfig> verify(final Set<Class<?>> wallClses,
                                                     final ConcurrentMap<String, Class<?>> keysRef) {
        /* Wall duplicated **/
        final Set<String> hashs = new HashSet<>();
        Observable.fromIterable(wallClses)
            .map(item -> {
                final Annotation annotation = item.getAnnotation(Wall.class);
                // Add configuration key into keys;
                keysRef.put(Ut.invoke(annotation, "value"), item);
                return this.hashPath(annotation);
            }).subscribe(hashs::add).dispose();

        // Duplicated adding.
        Fn.outUp(hashs.size() != wallClses.size(), LOGGER,
            WallDuplicatedException.class, this.getClass(),
            wallClses.stream().map(Class::getName).collect(Collectors.toSet()));

        // AuthConfig
        final ConcurrentMap<String, AuthConfig> map = AuthConfig.configMap();

        /* Wall key missing **/
        for (final String key : keysRef.keySet()) {
            final AuthConfig hitted = map.get(key);
            Fn.outUp(null == hitted, LOGGER, WallKeyMissingException.class, this.getClass(), key, keysRef.get(key));
        }
        return map;
    }

    /**
     * Path or Order must be not the same or duplicated.
     *
     * @param annotation annotation that contains `order` and `path`
     *
     * @return Each @Wall should contain unique hash key here.
     */
    private String hashPath(final Annotation annotation) {
        final Integer order = Ut.invoke(annotation, "order");
        final String path = Ut.invoke(annotation, "path");
        return Ut.encryptSHA256(order + path);
    }
}
