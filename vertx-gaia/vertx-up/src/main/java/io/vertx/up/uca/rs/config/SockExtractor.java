package io.vertx.up.uca.rs.config;

import io.vertx.up.annotations.WebSocket;
import io.vertx.up.atom.worker.Remind;
import io.vertx.up.eon.DefaultClass;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.di.DiPlugin;
import io.vertx.up.uca.rs.Extractor;
import io.vertx.up.util.Ut;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class SockExtractor implements Extractor<Set<Remind>> {
    private static final DiPlugin PLUGIN = DiPlugin.create(SockExtractor.class);

    @Override
    public Set<Remind> extract(final Class<?> clazz) {
        return Fn.getNull(new HashSet<>(), () -> {
            // 1. Class verify
            Verifier.noArg(clazz, this.getClass());
            Verifier.modifier(clazz, this.getClass());
            // 2. Scan method to find @WebSocket
            final Set<Remind> websockets = new HashSet<>();
            final Method[] methods = clazz.getDeclaredMethods();
            Arrays.stream(methods)
                .filter(MethodResolver::isValid)
                .filter(method -> method.isAnnotationPresent(WebSocket.class))
                .map(this::extract)
                .forEach(websockets::add);
            return websockets;
        }, clazz);
    }

    private Remind extract(final Method method) {
        final Class<?> clazz = method.getDeclaringClass();
        // 1. Scan whole Endpoints
        final Annotation annotation = method.getDeclaredAnnotation(WebSocket.class);
        final String address = Ut.invoke(annotation, KName.VALUE);
        // 2. Build Remind
        final Remind remind = new Remind();
        remind.setMethod(method);
        remind.setAddress(address);

        // Fix: Instance class for proxy
        final Object proxy = PLUGIN.createComponent(clazz);
        remind.setProxy(proxy);
        remind.setName(Ut.invoke(annotation, KName.NAME));
        // Input Part: input / inputAddress
        final Class<?> inputCls = Ut.invoke(annotation, "input");
        if (DefaultClass.class != inputCls) {
            remind.setInput(inputCls);
        }
        remind.setInputAddress(Ut.invoke(annotation, "inputAddress"));

        return remind;
    }
}
