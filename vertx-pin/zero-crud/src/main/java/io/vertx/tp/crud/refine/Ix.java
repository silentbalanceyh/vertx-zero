package io.vertx.tp.crud.refine;

import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.uca.desk.IxMod;
import io.vertx.tp.ke.atom.KField;
import io.vertx.tp.ke.atom.KModule;
import io.vertx.up.atom.Kv;
import io.vertx.up.commune.element.TypeAtom;
import io.vertx.up.commune.exchange.DictFabric;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class Ix {
    // --------------------------------- New Version
    public static void onAuditor(final JsonObject auditor, final JsonObject config, final String userId) {
        IxData.audit(auditor, config, userId);
    }

    public static Kv<String, String> onColumn(final Object value) {
        return IxData.field(value);
    }

    public static Kv<String, HttpMethod> onFlush(final IxMod in) {
        return IxData.flush(in);
    }

    public static Future<DictFabric> onFabric(final IxMod in) {
        return IxData.fabric(in);
    }

    public static JsonArray onMatrix(final KField field) {
        return IxData.matrix(field);
    }

    public static TypeAtom onAtom(final IxMod active, final JsonArray columns) {
        return IxType.atom(active, columns);
    }

    public static Function<JsonObject, Future<JsonObject>> searchFn(final IxMod in) {
        return IxQr.searchFn(in);
    }

    public static Function<JsonObject, Future<Long>> countFn(final IxMod in) {
        return IxQr.countFn(in);
    }

    public static <T> BiFunction<Supplier<T>, BiFunction<UxJooq, JsonObject, Future<T>>, Future<T>> seekFn(final IxMod in, final Object json) {
        return IxQr.seekFn(in, json);
    }

    public static Function<JsonObject, Future<JsonArray>> fetchFn(final IxMod in) {
        return IxQr.fetchFn(in);
    }

    // JqTool
    @SafeVarargs
    public static <T> Future<T> passion(final T input, final IxMod in, final BiFunction<T, IxMod, Future<T>>... executors) {
        return IxData.passion(input, in, executors);
    }

    /*
     * Deserialize to T
     */
    public static <T> Future<T> deserializeT(final JsonObject data, final KModule config) {
        final T reference = IxSerialize.deserializeT(data, config);
        return Ux.future(reference);
    }

    public static <T> Future<List<T>> deserializeT(final JsonArray data, final KModule config) {
        return Ux.future(IxSerialize.deserializeT(data, config));
    }

    public static void serializeJ(final JsonObject data, final KModule config) {
        Ux.future(IxSerialize.serializeJ(data, config));
    }

    public static class Log {

        public static void filters(final Class<?> clazz, final String pattern, final Object... args) {
            final Annal logger = Annal.get(clazz);
            IxLog.infoFilters(logger, pattern, args);
        }

        public static void init(final Class<?> clazz, final String pattern, final Object... args) {
            final Annal logger = Annal.get(clazz);
            IxLog.infoInit(logger, pattern, args);
        }

        public static void rest(final Class<?> clazz, final String pattern, final Object... args) {
            final Annal logger = Annal.get(clazz);
            IxLog.infoRest(logger, pattern, args);
        }

        public static void restW(final Class<?> clazz, final String pattern, final Object... args) {
            final Annal logger = Annal.get(clazz);
            IxLog.warnRest(logger, pattern, args);
        }

        public static void dao(final Class<?> clazz, final String pattern, final Object... args) {
            final Annal logger = Annal.get(clazz);
            IxLog.infoDao(logger, pattern, args);
        }

        public static void verify(final Class<?> clazz, final String pattern, final Object... args) {
            final Annal logger = Annal.get(clazz);
            IxLog.infoVerify(logger, pattern, args);
        }
    }
}
