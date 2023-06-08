package io.vertx.up.boot.origin;

import io.horizon.uca.log.Annal;
import io.vertx.ext.web.handler.AuthorizationHandler;
import io.vertx.up.annotations.Authenticate;
import io.vertx.up.annotations.Authorized;
import io.vertx.up.annotations.AuthorizedResource;
import io.vertx.up.annotations.Wall;
import io.vertx.up.backbone.config.EventExtractor;
import io.vertx.up.boot.di.DiPlugin;
import io.vertx.up.commune.secure.Aegis;
import io.vertx.up.commune.secure.AegisItem;
import io.vertx.up.eon.em.EmSecure;
import io.vertx.up.exception.boot.WallDuplicatedException;
import io.vertx.up.exception.boot.WallKeyMissingException;
import io.vertx.up.exception.boot.WallMethodMultiException;
import io.vertx.up.exception.boot.WallTypeWrongException;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * This class is for @Wall of security here.
 */
public class WallInquirer implements Inquirer<Set<Aegis>> {

    private final static Annal LOGGER = Annal.get(WallInquirer.class);
    private static final DiPlugin PLUGIN = DiPlugin.create(EventExtractor.class);

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
             * wall Class verification, in this branch it means that the system scanned
             * class that has been annotated with @Wall, you have defined wall
             * of zero framework in your system.
             *
             * Attention: If you enable zero extension ( zero-rbac ), the system will
             * use standard wall class in zero framework, this feature has been upgraded
             * from vertx 4.0
             */
            this.verifyDuplicated(wallClass);
            wallClass.stream().map(this::create).forEach(wallSet::add);
        }

        return wallSet;
    }

    private Aegis create(final Class<?> clazz) {
        final Aegis aegis = new Aegis();
        /*
         * 「Validation」
         * 1 - Proxy Creation with Wall Specification
         * 2 - Wall Type & Aegis Item
         ***/
        this.verifyProxy(clazz, aegis);

        final Annotation annotation = clazz.getAnnotation(Wall.class);
        final String typeKey = Ut.invoke(annotation, "value");
        this.verifyConfig(clazz, aegis, typeKey);
        aegis.setPath(Ut.invoke(annotation, "path"));

        /*
         * AuthorizationHandler class here
         */
        final Class<?> handlerCls = Ut.invoke(annotation, "handler");
        if (AuthorizationHandler.class.isAssignableFrom(handlerCls)) {
            aegis.setHandler(handlerCls);
        }
        /* Verify */
        return aegis;
    }

    private void verifyConfig(final Class<?> clazz, final Aegis reference, final String typeKey) {
        final EmSecure.AuthWall wall = EmSecure.AuthWall.from(typeKey);
        /* Wall Type Wrong */
        Fn.outBoot(Objects.isNull(wall), LOGGER, WallTypeWrongException.class,
            this.getClass(), typeKey, clazz);
        reference.setType(wall);
        final ConcurrentMap<String, AegisItem> configMap = AegisItem.configMap();
        if (EmSecure.AuthWall.EXTENSION == wall) {
            /* Extension */
            reference.setDefined(Boolean.TRUE);
            configMap.forEach(reference::addItem);
        } else {
            /* Standard */
            reference.setDefined(Boolean.FALSE);
            final AegisItem found = configMap.getOrDefault(wall.key(), null);
            Fn.outBoot(Objects.isNull(found), LOGGER, WallKeyMissingException.class,
                this.getClass(), wall.key(), clazz);
            reference.setItem(found);
        }
    }

    /*
     * Wall class specification scanned and verified by zero framework
     * the class must contain method `@Authenticate` and optional method `@Authorization` once
     */
    private void verifyProxy(final Class<?> clazz, final Aegis reference) {
        final Method[] methods = clazz.getDeclaredMethods();
        // Duplicated Method checking
        Fn.outBoot(this.verifyMethod(methods, Authenticate.class), LOGGER,
            WallMethodMultiException.class, this.getClass(),
            Authenticate.class.getSimpleName(), clazz.getName());
        Fn.outBoot(this.verifyMethod(methods, Authorized.class), LOGGER,
            WallMethodMultiException.class, this.getClass(),
            Authorized.class.getSimpleName(), clazz.getName());
        Fn.outBoot(this.verifyMethod(methods, AuthorizedResource.class), LOGGER,
            WallMethodMultiException.class, this.getClass(),
            AuthorizedResource.class.getSimpleName(), clazz.getName());

        /* Proxy **/
        reference.setProxy(PLUGIN.createProxy(clazz, null));
        // Find the first: Authenticate
        Arrays.stream(methods).forEach(method -> {
            if (Objects.nonNull(method)) {
                if (method.isAnnotationPresent(Authenticate.class)) {
                    reference.getAuthorizer().setAuthenticate(method);
                }
                if (method.isAnnotationPresent(Authorized.class)) {
                    reference.getAuthorizer().setAuthorization(method);
                }
                if (method.isAnnotationPresent(AuthorizedResource.class)) {
                    reference.getAuthorizer().setResource(method);
                }
            }
        });
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

    /*
     * Wall duplicated detection
     * Here the unique key is: path + order, you could not define duplicate wall class
     * Path or Order must be not the same or duplicated.
     **/
    private void verifyDuplicated(final Set<Class<?>> wallClses) {
        final Set<String> dupSet = new HashSet<>();
        // type = define
        wallClses.forEach(item -> {
            final Annotation annotation = item.getAnnotation(Wall.class);
            final Integer order = Ut.invoke(annotation, "order");
            final String path = Ut.invoke(annotation, "path");
            final String wallKey = Ut.encryptSHA256(order + path);
            dupSet.add(wallKey);
        });

        // Duplicated adding.
        Fn.outBoot(dupSet.size() != wallClses.size(), LOGGER,
            WallDuplicatedException.class, this.getClass(),
            wallClses.stream().map(Class::getName).collect(Collectors.toSet()));
    }
}
