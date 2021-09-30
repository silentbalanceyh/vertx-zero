package io.vertx.up.secure.proof;

import io.vertx.tp.error.WallMethodMultiException;
import io.vertx.up.annotations.Authenticate;
import io.vertx.up.annotations.Authorization;
import io.vertx.up.atom.secure.Aegis;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.di.DiPlugin;
import io.vertx.up.uca.rs.config.EventExtractor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

public class AuthStandard {

    private static final DiPlugin PLUGIN = DiPlugin.create(EventExtractor.class);
    private final transient Class<?> clazz;
    private final transient Annal logger;
    private final transient Method[] methods;

    private AuthStandard(final Class<?> clazz) {
        this.clazz = clazz;
        this.logger = Annal.get(clazz);
        this.methods = clazz.getDeclaredMethods();
    }

    public static AuthStandard create(final Class<?> clazz) {
        return new AuthStandard(clazz);
    }

    public AuthStandard verify() {
        // Duplicated Method checking
        Fn.outUp(this.verifyMethod(this.methods, Authenticate.class), this.logger,
            WallMethodMultiException.class, this.getClass(),
            Authenticate.class.getSimpleName(), this.clazz.getName());
        Fn.outUp(this.verifyMethod(this.methods, Authorization.class), this.logger,
            WallMethodMultiException.class, this.getClass(),
            Authorization.class.getSimpleName(), this.clazz.getName());
        return this;
    }

    public void mount(final Aegis reference) {
        /* Proxy **/
        reference.setProxy(PLUGIN.createComponent(this.clazz));
        // Find the first: Authenticate
        final Optional<Method> authenticateMethod
            = Arrays.stream(this.methods).filter(
                item -> item.isAnnotationPresent(Authenticate.class))
            .findFirst();
        reference.getAuthorizer().setAuthenticate(authenticateMethod.orElse(null));
        // Find the second: Authorize
        final Optional<Method> authorizeMethod
            = Arrays.stream(this.methods).filter(
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
}
