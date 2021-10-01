package io.vertx.up.secure.component;

import io.reactivex.Observable;
import io.vertx.tp.error.WallDuplicatedException;
import io.vertx.tp.error.WallKeyMissingException;
import io.vertx.tp.error.WallMethodMultiException;
import io.vertx.up.annotations.Authenticate;
import io.vertx.up.annotations.Authorization;
import io.vertx.up.annotations.Wall;
import io.vertx.up.atom.secure.Aegis;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.secure.config.AuthConfig;
import io.vertx.up.uca.di.DiPlugin;
import io.vertx.up.uca.rs.config.EventExtractor;
import io.vertx.up.util.Ut;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class WallSpec {

    private static final DiPlugin PLUGIN = DiPlugin.create(EventExtractor.class);
    private final transient Class<?> clazz;
    private final transient Annal logger;

    private WallSpec(final Class<?> clazz) {
        this.clazz = clazz;
        this.logger = Annal.get(clazz);
    }

    public static WallSpec create(final Class<?> clazz) {
        return new WallSpec(clazz);
    }

    public ConcurrentMap<String, AuthConfig> verify(final Set<Class<?>> wallClses,
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
        Fn.outUp(hashs.size() != wallClses.size(), this.logger,
            WallDuplicatedException.class, this.getClass(),
            wallClses.stream().map(Class::getName).collect(Collectors.toSet()));

        // AuthConfig
        final ConcurrentMap<String, AuthConfig> map = AuthConfig.configMap();

        /* Wall key missing **/
        for (final String key : keysRef.keySet()) {
            final AuthConfig hitted = map.get(key);
            Fn.outUp(null == hitted, this.logger, WallKeyMissingException.class, this.getClass(), key, keysRef.get(key));
        }
        return map;
    }

    public void mount(final Class<?> clazz, final Aegis reference) {
        final Method[] methods = clazz.getDeclaredMethods();
        // Duplicated Method checking
        Fn.outUp(this.verifyMethod(methods, Authenticate.class), this.logger,
            WallMethodMultiException.class, this.getClass(),
            Authenticate.class.getSimpleName(), this.clazz.getName());
        Fn.outUp(this.verifyMethod(methods, Authorization.class), this.logger,
            WallMethodMultiException.class, this.getClass(),
            Authorization.class.getSimpleName(), this.clazz.getName());
        /* Proxy **/
        reference.setProxy(PLUGIN.createComponent(clazz));
        // Find the first: Authenticate
        final Optional<Method> authenticateMethod
            = Arrays.stream(methods).filter(
                item -> item.isAnnotationPresent(Authenticate.class))
            .findFirst();
        reference.getAuthorizer().setAuthenticate(authenticateMethod.orElse(null));
        // Find the second: Authorize
        final Optional<Method> authorizeMethod
            = Arrays.stream(methods).filter(
                item -> item.isAnnotationPresent(Authorization.class))
            .findFirst();
        reference.getAuthorizer().setAuthorize(authorizeMethod.orElse(null));
    }

    private boolean verifyMethod(final Method[] methods,
                                 final Class<? extends Annotation> clazz) {

        final long found = Arrays.stream(methods)
            .filter(method -> method.isAnnotationPresent(clazz))
            .count();
        // If found = 0, 1, OK
        // If > 1, duplicated
        return 1 < found;
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
