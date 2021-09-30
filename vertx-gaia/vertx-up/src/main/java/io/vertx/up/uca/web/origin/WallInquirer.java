package io.vertx.up.uca.web.origin;

import io.reactivex.Observable;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.error.WallDuplicatedException;
import io.vertx.tp.error.WallKeyMissingException;
import io.vertx.up.annotations.Aas;
import io.vertx.up.annotations.Wall;
import io.vertx.up.atom.secure.Aegis;
import io.vertx.up.exception.zero.DynamicKeyMissingException;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.secure.Rampart;
import io.vertx.up.secure.proof.AuthDefined;
import io.vertx.up.secure.proof.AuthStandard;
import io.vertx.up.uca.marshal.Transformer;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.uca.yaml.ZeroUniform;
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

    private static final Node<JsonObject> NODE =
        Ut.singleton(ZeroUniform.class);

    private static final String KEY = "secure";

    private transient final Transformer<Aegis> transformer =
        Ut.singleton(Rampart.class);

    @Override
    public Set<Aegis> scan(final Set<Class<?>> walls) {
        /* 1. Build result **/
        final Set<Aegis> wallSet = new TreeSet<>();
        final Set<Class<?>> wallClses = walls.stream()
            .filter((item) -> item.isAnnotationPresent(Wall.class))
            .collect(Collectors.toSet());
        if (!wallClses.isEmpty()) {
            /*
             * It means that you have set Wall and enable security configuration
             * wall Class verification
             */
            final ConcurrentMap<String, Class<?>> keys = new ConcurrentHashMap<>();
            final JsonObject config = this.verify(wallClses, keys);
            for (final String field : config.fieldNames()) {
                // Difference key setting
                final Class<?> cls = keys.get(field);
                final Aegis aegis = this.transformer.transform(config.getJsonObject(field));
                // Set Information from class
                this.mountData(aegis, cls);
                wallSet.add(aegis);
            }
        }
        /* 3. Transfer **/
        return wallSet;
    }

    private void mountData(final Aegis aegis, final Class<?> clazz) {
        /* Extract basic data **/
        final Annotation annotation = clazz.getAnnotation(Wall.class);
        aegis.setOrder(Ut.invoke(annotation, "order"));
        aegis.setPath(Ut.invoke(annotation, "path"));
        aegis.setDefined(Ut.invoke(annotation, "define"));
        /* Proxy **/
        if (aegis.isDefined()) {
            // Custom Workflow
            AuthDefined.create(clazz)
                .verify().mount(aegis);
        } else {
            // Standard Workflow
            AuthStandard.create(clazz)
                .verify().mount(aegis);
        }
        /* Executor for you own code logical */
        final Class<?> executorCls = Ut.invoke(annotation, "executor");
        if (Void.class != executorCls && executorCls.isAnnotationPresent(Aas.class)) {
            aegis.setExecutor(executorCls);
        }
    }

    /**
     * @param wallClses Security @Wall class
     * @param keysRef   critical class reference for security
     *
     * @return valid configuration that will be used in @Wall class.
     */
    private JsonObject verify(final Set<Class<?>> wallClses,
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

        /* Shared key does not existing **/
        final JsonObject config = NODE.read();
        Fn.outUp(!config.containsKey(KEY), LOGGER,
            DynamicKeyMissingException.class, this.getClass(),
            KEY, config);

        /* Wall key missing **/
        final JsonObject hitted = config.getJsonObject(KEY);
        for (final String key : keysRef.keySet()) {
            Fn.outUp(null == hitted || !hitted.containsKey(key), LOGGER,
                WallKeyMissingException.class, this.getClass(),
                key, keysRef.get(key));
        }
        return hitted;
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
