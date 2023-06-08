package io.vertx.up.secure.validation;

import io.aeon.runtime.CRunning;
import io.horizon.eon.VString;
import io.horizon.exception.WebException;
import io.reactivex.rxjava3.core.Observable;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.Rule;
import io.vertx.up.atom.agent.Depot;
import io.vertx.up.atom.agent.Event;
import io.vertx.up.eon.KWeb;
import io.vertx.up.exception.web._400ValidationException;
import io.vertx.up.util.Ut;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.executable.ExecutableValidator;
import jakarta.ws.rs.BodyParam;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Validator {

    private static final jakarta.validation.Validator VALIDATOR
        = Validation.buildDefaultValidatorFactory().usingContext().messageInterpolator(
        new ValidatorInterpolator()
    ).getValidator();

    private static final ConcurrentMap<String, Map<String, List<Rule>>>
        RULERS = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, JsonObject> STORED = CRunning.CC_CODEX.store();

    /**
     * Validate the method parameters based on javax.validation: Hibernate Validator.
     *
     * @param proxy  The checked target object.
     * @param method The checked target method.
     * @param args   The checked target method's parameters.
     * @param <T>    The target object type: Generic types.
     */
    public <T> void verifyMethod(
        final T proxy,
        final Method method,
        final Object[] args) {
        // 1. Get method validator
        final ExecutableValidator validatorParam
            = VALIDATOR.forExecutables();
        // 2. Create new params that wait for validation
        final Set<ConstraintViolation<T>> constraints
            = validatorParam.validateParameters(proxy, method, args);
        // 3. Throw out exception
        if (!constraints.isEmpty()) {
            final ConstraintViolation<T> item = constraints.iterator().next();
            this.replyError(proxy, method, item);
        }
    }

    private <T> void replyError(final T proxy, final Method method,
                                final ConstraintViolation<T> item) {
        if (null != item) {
            final WebException error
                = new _400ValidationException(this.getClass(),
                proxy.getClass(), method, item.getMessage());
            error.readable(item.getMessage());
            throw error;
        }
    }

    /**
     * Advanced ruler building for Body content validation based on yml configuration.
     *
     * @param depot The container to contains event, configuration, ruler.
     *
     * @return The configured rulers.
     */
    public Map<String, List<Rule>> buildRulers(
        final Depot depot) {
        final Map<String, List<Rule>> rulers
            = new LinkedHashMap<>();
        final ConcurrentMap<String, Class<? extends Annotation>>
            annotions = depot.getAnnotations();
        Observable.fromIterable(annotions.keySet())
            .filter(KWeb.ARGS.MIME_DIRECT::equals)
            .map(annotions::get)
            // 1. Check whether contains @BodyParam
            .any(item -> BodyParam.class == item)
            // 2. Build rulers
            .map(item -> this.buildKey(depot.getEvent()))
            .map(this::buildRulers)
            .subscribe(rulers::putAll).dispose();
        return rulers;
    }

    private Map<String, List<Rule>> buildRulers(final String key) {
        if (RULERS.containsKey(key)) {
            return RULERS.get(key);
        } else {
            final JsonObject rule = STORED.get(key); // ZeroCodex.getCodex(key);
            final Map<String, List<Rule>> ruler
                = new LinkedHashMap<>();
            if (null != rule) {
                Ut.itJObject(rule, (value, field) -> {
                    // Checked valid rule config
                    final List<Rule> rulers = this.buildRulers(value);
                    if (!rulers.isEmpty()) {
                        ruler.put(field, rulers);
                    }
                });
                if (!ruler.isEmpty()) {
                    RULERS.put(key, ruler);
                }
            }
            return ruler;
        }
    }

    private List<Rule> buildRulers(final Object config) {
        final List<Rule> rulers = new ArrayList<>();
        if (config instanceof final JsonArray configData) {
            Ut.itJArray(configData, JsonObject.class, (item, index) -> {
                final Rule ruler = Rule.create(item);
                if (null != ruler) {
                    rulers.add(ruler);
                }
            });
        }
        return rulers;
    }

    private String buildKey(final Event event) {
        String prefix = event.getPath().trim().substring(1);
        prefix = prefix.replace(VString.SLASH, VString.DOT);
        prefix = prefix.replace(VString.COLON, VString.DOLLAR);
        final String suffix = event.getMethod().name().toLowerCase(Locale.getDefault());
        return prefix + VString.DOT + suffix;
    }
}
