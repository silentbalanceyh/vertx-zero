package io.vertx.tp.jet.refine;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.jet.atom.JtConfig;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.uca.yaml.ZeroUniform;
import io.vertx.up.util.Ut;

import javax.ws.rs.core.MediaType;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

/*
 * Function for resolution null pointer in conversion
 */
class JtRoute {

    private static final Node<JsonObject> UNIFORM = Ut.singleton(ZeroUniform.class);

    static Set<String> toSet(final Supplier<String> supplier) {
        final String inputRequired = supplier.get();
        final Set<String> result = new HashSet<>();
        if (Ut.notNil(inputRequired) && Ut.isJArray(inputRequired)) {
            final JsonArray mimeArr = new JsonArray(inputRequired);
            mimeArr.stream().map(item -> (String) item).forEach(result::add);
        }
        return result;
    }

    static String toPath(final Supplier<String> routeSupplier, final Supplier<String> uriSupplier,
                         final boolean secure) {
        final JsonObject config = UNIFORM.read();
        final JsonObject configRouter = Ut.valueJObject(config, KName.ROUTER);
        final JtConfig configuration = Ut.deserialize(configRouter, JtConfig.class);
        return toPath(routeSupplier, uriSupplier, secure, configuration);
    }

    static String toPath(final Supplier<String> routeSupplier,
                         final Supplier<String> uriSupplier,
                         final boolean secure,      // Null Pointer if use Boolean
                         final JtConfig config) {
        /* Whether current api is secure */
        final StringBuilder uri = new StringBuilder();
        /* Get secure path here */
        if (secure) {
            String wall = config.getWall();
            if (Ut.isNil(wall)) {
                wall = Strings.EMPTY;
            }
            if (wall.startsWith("/")) {
                uri.append(wall);
            } else {
                uri.append(wall);
            }
        }
        /* Read root of current route */
        final String root = routeSupplier.get();
        if (Ut.notNil(root)) {
            uri.append(root).append(root.endsWith("/") ? "" : "/");
        }
        /* Read real Api */
        final String path = uriSupplier.get();
        if (Ut.notNil(path)) {
            uri.append(path);
        }
        /* replace duplicated // -> /, normalized  */
        return uri.toString().replace("//", "/");
    }

    static Set<MediaType> toMime(final Supplier<String> supplier) {
        /* Convert to MediaType of Rs */
        final String mime = supplier.get();
        final Set<MediaType> mimeSet = new HashSet<>();
        if (Ut.notNil(mime) && Ut.isJArray(mime)) {
            final JsonArray mimeArr = new JsonArray(mime);
            mimeArr.stream().map(item -> (String) item)
                .map(MediaType::valueOf).forEach(mimeSet::add);
        }
        /* application/json */
        if (mimeSet.isEmpty()) {
            mimeSet.add(MediaType.APPLICATION_JSON_TYPE);
        }
        return mimeSet;
    }
}
