package io.vertx.up.uca.rs.config;

import com.google.inject.Injector;
import io.reactivex.Observable;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.impl.ConcurrentHashSet;
import io.vertx.tp.error.EventCodexMultiException;
import io.vertx.up.annotations.Adjust;
import io.vertx.up.annotations.Codex;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.atom.agent.Event;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.runtime.ZeroAnno;
import io.vertx.up.runtime.ZeroHelper;
import io.vertx.up.uca.container.VInstance;
import io.vertx.up.uca.rs.Extractor;
import io.vertx.up.util.Ut;
import io.vertx.zero.exception.EventSourceException;

import javax.ws.rs.Path;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Scanned @EndPoint clazz to build Event metadata
 */
public class EventExtractor implements Extractor<Set<Event>> {

    private static final Annal LOGGER = Annal.get(EventExtractor.class);

    @Override
    public Set<Event> extract(final Class<?> clazz) {
        return Fn.getNull(new ConcurrentHashSet<>(), () -> {
            // 1. Class verify
            this.verify(clazz);
            // 2. Check whether clazz annotated with @PATH
            final Set<Event> result = new ConcurrentHashSet<>();
            Fn.safeSemi(clazz.isAnnotationPresent(Path.class), LOGGER,
                () -> {
                    // 3.1. Append Root Path
                    final Path path = ZeroHelper.getPath(clazz);
                    assert null != path : "Path should not be null.";
                    result.addAll(this.extract(clazz, PathResolver.resolve(path)));
                },
                () -> {
                    // 3.2. Use method Path directly
                    result.addAll(this.extract(clazz, null));
                });
            return result;
        }, clazz);
    }

    private void verify(final Class<?> clazz) {
        // Check basic specification: No Arg Constructor
        if (!clazz.isInterface()) {
            // Class direct.
            Verifier.noArg(clazz, this.getClass());
        }
        Verifier.modifier(clazz, this.getClass());
        // Event Source Checking
        Fn.outUp(!clazz.isAnnotationPresent(EndPoint.class),
            LOGGER, EventSourceException.class,
            this.getClass(), clazz.getName());
    }

    @SuppressWarnings("all")
    private Set<Event> extract(final Class<?> clazz, final String root) {
        final Set<Event> events = new ConcurrentHashSet<>();
        // 0.Preparing
        final Method[] methods = clazz.getDeclaredMethods();
        // 1.Validate Codex annotation appears
        final Long counter = Observable.fromArray(methods)
            .map(Method::getParameterAnnotations)
            .flatMap(Observable::fromArray)
            .map(Arrays::asList)
            .map(item -> item.stream().map(Annotation::annotationType).collect(Collectors.toList()))
            .filter(item -> item.contains(Codex.class))
            .count().blockingGet();
        Fn.outUp(methods.length < counter, LOGGER,
            EventCodexMultiException.class,
            this.getClass(), clazz);
        // 2.Build Set
        events.addAll(Arrays.stream(methods).filter(MethodResolver::isValid)
            .map(item -> this.extract(item, root))
            .filter(Objects::nonNull)
            .collect(Collectors.toSet()));
        return events;
    }

    /**
     * Scan for single
     *
     * @param method single method that will be scanned.
     * @param root   root path calculation
     *
     * @return Standard Event object
     */
    private Event extract(final Method method, final String root) {
        // 1.Method path
        final Event event = new Event();
        // 2.Method resolve
        final HttpMethod httpMethod = MethodResolver.resolve(method);
        if (null == httpMethod) {
            // Ignored the method could not be annotated.
            return null;
        } else {
            event.setMethod(httpMethod);
        }
        {
            // 3.1. Get path from method
            final Path path = ZeroHelper.getPath(method);
            if (null == path) {
                // 3.2. Check root double check
                if (!Ut.isNil(root)) {
                    // Use root directly.
                    event.setPath(root);
                }
            } else {
                final String result = PathResolver.resolve(
                    path, root);
                event.setPath(result);
            }
        }
        // 4.Action
        event.setAction(method);
        // 6.Mime resolve
        event.setConsumes(MediaResolver.consumes(method));
        event.setProduces(MediaResolver.produces(method));
        // 7. Instance clazz for proxy
        final Class<?> clazz = method.getDeclaringClass();
        final Object proxy;
        final Injector di = ZeroAnno.getDi();
        if (clazz.isInterface()) {
            final Class<?> implClass = Ut.childUnique(clazz);
            if (null != implClass) {
                proxy = Ut.singleton(implClass, () -> di.getInstance(implClass)); // Ut.singleton(implClass);
            } else {
                /*
                 * SPEC5: Interface only, direct api, in this situation,
                 * The proxy is null and the agent do nothing. The request will
                 * send to event bus direct. It's not needed to set
                 * implementation class.
                 */
                proxy = VInstance.create(clazz);
            }
        } else {
            final Class<?> implClass = method.getDeclaringClass();
            proxy = Ut.singleton(implClass, () -> di.getInstance(implClass)); // Ut.singleton(method.getDeclaringClass());
        }
        event.setProxy(proxy);
        // 8. Order
        if (method.isAnnotationPresent(Adjust.class)) {
            final Annotation adjust = method.getDeclaredAnnotation(Adjust.class);
            final Integer order = Ut.invoke(adjust, "value");
            if (Objects.nonNull(order)) {
                /*
                 * Routing order modification.
                 */
                event.setOrder(order);
            }
        }
        return event;
    }
}
