package io.vertx.mod.jet.refine;

import io.horizon.eon.VString;
import io.macrocosm.specification.app.HApp;
import io.macrocosm.specification.program.HArk;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.jet.atom.JtConfig;
import io.vertx.up.eon.KName;
import io.vertx.up.runtime.ZeroStore;
import io.vertx.up.util.Ut;
import jakarta.ws.rs.core.MediaType;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

/*
 * Function for resolution null dot in conversion
 */
class JtRoute {

    static Set<String> toSet(final Supplier<String> supplier) {
        final String inputRequired = supplier.get();
        final Set<String> result = new HashSet<>();
        if (Ut.isNotNil(inputRequired) && Ut.isJArray(inputRequired)) {
            final JsonArray mimeArr = new JsonArray(inputRequired);
            mimeArr.stream().map(item -> (String) item).forEach(result::add);
        }
        return result;
    }

    static String toPath(final HArk ark, final Supplier<String> uriSupplier,
                         final boolean secure) {
        final JsonObject configRouter = ZeroStore.option(KName.ROUTER);
        final JtConfig configuration = Ut.deserialize(configRouter, JtConfig.class);
        return toPath(ark, uriSupplier, secure, configuration);
    }

    static String toPath(final HArk ark,
                         final Supplier<String> uriSupplier,
                         final boolean secure,      // Null Pointer if use Boolean
                         final JtConfig config) {
        /* Whether current api is secure */
        final StringBuilder uri = new StringBuilder();
        /* Get secure path here */
        if (secure) {
            String wall = config.getWall();
            if (Ut.isNil(wall)) {
                wall = VString.EMPTY;
            }
            if (wall.startsWith("/")) {
                uri.append(wall);
            } else {
                uri.append(wall);
            }
        }
        /* Read root of current route */
        final HApp app = ark.app();
        final String root = app.option(KName.App.ROUTE);
        if (Ut.isNotNil(root)) {
            uri.append(root).append(root.endsWith("/") ? "" : "/");
        }
        /* Read real Api */
        final String path = uriSupplier.get();
        if (Ut.isNotNil(path)) {
            uri.append(path);
        }
        /* replace duplicated // -> /, normalized  */
        return uri.toString().replace("//", "/");
    }

    static Set<MediaType> toMime(final Supplier<String> supplier) {
        /* Convert to MediaType of Rs */
        final String mime = supplier.get();
        final Set<MediaType> mimeSet = new HashSet<>();
        if (Ut.isNotNil(mime) && Ut.isJArray(mime)) {
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
