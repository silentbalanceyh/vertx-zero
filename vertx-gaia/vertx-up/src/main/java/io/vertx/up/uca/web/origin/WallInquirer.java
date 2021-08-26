package io.vertx.up.uca.web.origin;

import io.reactivex.Observable;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.error.WallDuplicatedException;
import io.vertx.tp.error.WallKeyMissingException;
import io.vertx.up.annotations.Wall;
import io.vertx.up.atom.secure.Cliff;
import io.vertx.up.exception.zero.DynamicKeyMissingException;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.secure.Rampart;
import io.vertx.up.secure.inquire.OstiumAuth;
import io.vertx.up.secure.inquire.PhylumAuth;
import io.vertx.up.uca.marshal.Transformer;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.uca.yaml.ZeroUniform;
import io.vertx.up.util.Ut;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * This class is for @Wall of security here.
 */
public class WallInquirer implements Inquirer<Set<Cliff>> {

    private static final Annal LOGGER = Annal.get(WallInquirer.class);

    private static final Node<JsonObject> NODE =
        Ut.singleton(ZeroUniform.class);

    private static final String KEY = "secure";

    private transient final Transformer<Cliff> transformer =
        Ut.singleton(Rampart.class);

    @Override
    public Set<Cliff> scan(final Set<Class<?>> walls) {
        /* 1. Build result **/
        final Set<Cliff> wallSet = new TreeSet<>();
        final Set<Class<?>> wallClses = walls.stream()
            .filter((item) -> item.isAnnotationPresent(Wall.class))
            .collect(Collectors.toSet());
        if (!wallClses.isEmpty()) {
            /*
             * It means that you have set Wall and enable security configuration
             * wallClses verification
             */
            final ConcurrentMap<String, Class<?>> keys = new ConcurrentHashMap<>();
            final JsonObject config = verify(wallClses, keys);
            for (final String field : config.fieldNames()) {
                // Difference key setting
                final Class<?> cls = keys.get(field);
                final Cliff cliff = transformer.transform(config.getJsonObject(field));
                // Set Information from class
                mountData(cliff, cls);
                wallSet.add(cliff);
            }
        }
        /* 3. Transfer **/
        return wallSet;
    }

    private void mountData(final Cliff cliff, final Class<?> clazz) {
        /* Extract basic data **/
        mountAnno(cliff, clazz);
        /* Proxy **/
        if (cliff.isDefined()) {
            // Custom Workflow
            OstiumAuth.create(clazz)
                .verify().mount(cliff);
        } else {
            // Standard Workflow
            PhylumAuth.create(clazz)
                .verify().mount(cliff);
        }
    }

    private void mountAnno(final Cliff cliff, final Class<?> clazz) {
        final Annotation annotation = clazz.getAnnotation(Wall.class);
        cliff.setOrder(Ut.invoke(annotation, "order"));
        cliff.setPath(Ut.invoke(annotation, "path"));
        cliff.setDefined(Ut.invoke(annotation, "define"));
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
            .filter(Objects::nonNull)
            .map(item -> {
                final Annotation annotation = item.getAnnotation(Wall.class);
                // Add configuration key into keys;
                keysRef.put(Ut.invoke(annotation, "value"), item);
                return hashPath(annotation);
            }).subscribe(hashs::add).dispose();

        // Duplicated adding.
        Fn.outUp(hashs.size() != wallClses.size(), LOGGER,
            WallDuplicatedException.class, getClass(),
            wallClses.stream().map(Class::getName).collect(Collectors.toSet()));

        /* Shared key does not existing **/
        final JsonObject config = NODE.read();
        Fn.outUp(!config.containsKey(KEY), LOGGER,
            DynamicKeyMissingException.class, getClass(),
            KEY, config);

        /* Wall key missing **/
        final JsonObject hitted = config.getJsonObject(KEY);
        for (final String key : keysRef.keySet()) {
            Fn.outUp(null == hitted || !hitted.containsKey(key), LOGGER,
                WallKeyMissingException.class, getClass(),
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
