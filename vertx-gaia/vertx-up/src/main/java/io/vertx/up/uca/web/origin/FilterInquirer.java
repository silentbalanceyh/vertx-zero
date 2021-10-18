package io.vertx.up.uca.web.origin;

import io.reactivex.Observable;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.up.annotations.Ordered;
import io.vertx.up.atom.agent.Event;
import io.vertx.up.eon.Orders;
import io.vertx.up.eon.Values;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.web.filter.Filter;
import io.vertx.up.util.Ut;
import io.vertx.zero.exception.FilterInitialException;
import io.vertx.zero.exception.FilterOrderException;
import io.vertx.zero.exception.FilterSpecificationException;

import javax.servlet.annotation.WebFilter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Filter processing
 * path = Event Chain
 */
public class FilterInquirer implements Inquirer<ConcurrentMap<String, Set<Event>>> {

    private static final Annal LOGGER = Annal.get(FilterInquirer.class);

    @Override
    public ConcurrentMap<String, Set<Event>> scan(final Set<Class<?>> clazzes) {
        // Scan all classess that are annotated with @WebFilter
        final ConcurrentMap<String, Set<Event>> filters = new ConcurrentHashMap<>();
        Observable.fromIterable(clazzes)
            .filter(item -> item.isAnnotationPresent(WebFilter.class))
            .map(this::ensure)
            .subscribe(item -> this.extract(filters, item))
            .dispose();
        return filters;
    }

    private Class<?> ensure(final Class<?> clazz) {
        Fn.outUp(!Filter.class.isAssignableFrom(clazz), LOGGER,
            FilterSpecificationException.class, this.getClass(), clazz);
        return clazz;
    }

    private void extract(final ConcurrentMap<String, Set<Event>> map,
                         final Class<?> clazz) {
        final Annotation annotation = clazz.getAnnotation(WebFilter.class);
        final String[] pathes = Ut.invoke(annotation, "value");
        // Multi pathes supported
        for (final String path : pathes) {
            final Event event = this.extract(path, clazz);
            if (null != event) {
                // Set<Event> initialized.
                Set<Event> events = map.get(path);
                if (null == events) {
                    events = new HashSet<>();
                }
                // Add new event to set
                events.add(event);
                map.put(path, events);
            }
        }
    }

    private Event extract(final String path, final Class<?> clazz) {
        final Event event = new Event();
        event.setPath(path);
        final Annotation annotation = clazz.getAnnotation(Ordered.class);
        int order = Orders.FILTER;
        if (null != annotation) {
            final Integer setted = Ut.invoke(annotation, "value");
            // Order specification
            Fn.outUp(setted < 0, LOGGER,
                FilterOrderException.class, this.getClass(), clazz);
            order = order + setted;
        }
        event.setOrder(order);
        // Proxy
        final Object proxy = Ut.singleton(clazz);
        Fn.outUp(null == proxy, LOGGER,
            FilterInitialException.class, this.getClass(), clazz);
        event.setProxy(proxy);
        // Action
        final Method action = this.findMethod(clazz);
        event.setAction(action);
        event.setConsumes(new HashSet<>());
        event.setProduces(new HashSet<>());
        return event;
    }

    private Method findMethod(final Class<?> clazz) {
        final List<Method> methods = new ArrayList<>();
        // One method only
        final Method[] scanned = clazz.getDeclaredMethods();
        Observable.fromArray(scanned)
            .filter(item -> "doFilter".equals(item.getName()))
            .subscribe(methods::add)
            .dispose();
        // No overwritting
        if (Values.ONE == methods.size()) {
            return methods.get(Values.IDX);
        } else {
            // Search for correct signature
            return Observable.fromIterable(methods)
                .filter(this::isValidFilter)
                .blockingFirst();
        }
    }

    private boolean isValidFilter(final Method method) {
        final Class<?>[] parameters = method.getParameterTypes();
        boolean valid = false;
        if (Values.TWO == parameters.length) {
            final Class<?> requestCls = parameters[Values.IDX];
            final Class<?> responseCls = parameters[Values.ONE];
            if (HttpServerRequest.class == requestCls && HttpServerResponse.class == responseCls) {
                valid = true;
            }
        }
        return valid;
    }
}
