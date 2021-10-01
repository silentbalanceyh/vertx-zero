package io.vertx.up.uca.web.origin;

import io.vertx.tp.error.WallDuplicatedException;
import io.vertx.tp.error.WallKeyMissingException;
import io.vertx.tp.error.WallMethodMultiException;
import io.vertx.tp.error.WallTypeWrongException;
import io.vertx.up.annotations.Authenticate;
import io.vertx.up.annotations.Authorization;
import io.vertx.up.annotations.Wall;
import io.vertx.up.atom.secure.Aegis;
import io.vertx.up.atom.secure.AegisItem;
import io.vertx.up.eon.em.AuthWall;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.di.DiPlugin;
import io.vertx.up.uca.rs.config.EventExtractor;
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
        /* Verify */
        return aegis;
    }

    private void verifyConfig(final Class<?> clazz, final Aegis reference, final String typeKey) {
        final AuthWall wall = AuthWall.from(typeKey);
        /* Wall Type Wrong */
        Fn.outUp(Objects.isNull(wall), LOGGER, WallTypeWrongException.class, typeKey, clazz);
        reference.setType(wall);
        final ConcurrentMap<String, AegisItem> configMap = AegisItem.configMap();
        if (AuthWall.EXTENSION == wall) {
            /* Extension */
            reference.setDefined(Boolean.TRUE);
            configMap.forEach(reference::addItem);
        } else {
            /* Standard */
            reference.setDefined(Boolean.FALSE);
            final AegisItem found = configMap.getOrDefault(wall.key(), null);
            Fn.outUp(Objects.isNull(found), LOGGER, WallKeyMissingException.class, wall.key(), clazz);
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
        Fn.outUp(this.verifyMethod(methods, Authenticate.class), LOGGER,
            WallMethodMultiException.class, this.getClass(),
            Authenticate.class.getSimpleName(), clazz.getName());
        Fn.outUp(this.verifyMethod(methods, Authorization.class), LOGGER,
            WallMethodMultiException.class, this.getClass(),
            Authorization.class.getSimpleName(), clazz.getName());
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
        Fn.outUp(dupSet.size() != wallClses.size(), LOGGER,
            WallDuplicatedException.class, this.getClass(),
            wallClses.stream().map(Class::getName).collect(Collectors.toSet()));
    }
}
