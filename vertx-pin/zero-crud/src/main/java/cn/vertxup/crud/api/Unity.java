package cn.vertxup.crud.api;

import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.actor.IxActor;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.ke.atom.KField;
import io.vertx.tp.ke.atom.KModule;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.Apeak;
import io.vertx.tp.optic.Pocket;
import io.vertx.tp.optic.Seeker;
import io.vertx.tp.optic.component.Dictionary;
import io.vertx.up.commune.Envelop;
import io.vertx.up.commune.exchange.DictConfig;
import io.vertx.up.commune.exchange.DictEpsilon;
import io.vertx.up.commune.exchange.DictFabric;
import io.vertx.up.commune.exchange.DictSource;
import io.vertx.up.eon.KName;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class Unity {

    private static final Annal LOGGER = Annal.get(Unity.class);

    static DictFabric fetchFabric(final ConcurrentMap<String, JsonArray> dictData, final KModule config) {
        return DictFabric.create().dictionary(dictData).epsilon(config.getEpsilon());
    }

    /*
     * Seeker for lookup target resource
     * 1. Provide uri, method to find target resource of personal
     * 2. Sigma from header
     * 3. Find impact resourcedId that will be related to view.
     */
    static Future<JsonObject> fetchView(final UxJooq dao, final Envelop request, final KModule config) {
        /* init parameters */
        final JsonObject params = Unity.initMy(request);
        return Ke.channel(Seeker.class, JsonObject::new, seeker -> Ux.future(params)
                /* Header */
                .compose(input -> IxActor.header().bind(request).procAsync(input, config))
                /* Fetch Impact */
                .compose(seeker.on(dao)::fetchImpact));
    }

    static Future<JsonArray> fetchFull(final UxJooq dao, final Envelop request, final KModule config) {
        /* Get Stub */
        return Ke.channel(Apeak.class, JsonArray::new, stub -> IxActor.start()
                /* Apeak column definition here */
                .compose(input -> IxActor.apeak().bind(request).procAsync(input, config))
                /* Header */
                .compose(input -> IxActor.header().bind(request).procAsync(input, config))
                /* Fetch Full Columns */
                .compose(stub.on(dao)::fetchFull));
    }

    static Future<ConcurrentMap<String, JsonArray>> fetchDict(final Envelop request, final KModule config) {
        /* Epsilon */
        final ConcurrentMap<String, DictEpsilon> epsilonMap = config.getEpsilon();
        /* Channel Plugin */
        final Dictionary plugin = Pocket.lookup(Dictionary.class);
        /* Dict */
        final DictConfig dict = config.getSource();
        if (epsilonMap.isEmpty() || Objects.isNull(plugin) || !dict.validSource()) {
            /*
             * Direct returned
             */
            Ix.infoRest(LOGGER, "Plugin condition failure, {0}, {1}, {2}",
                    epsilonMap.isEmpty(), Objects.isNull(plugin), !dict.validSource());
            return Ux.future(new ConcurrentHashMap<>());
        } else {
            final List<DictSource> sources = dict.getSource();
            final MultiMap paramMap = MultiMap.caseInsensitiveMultiMap();
            final JsonObject headers = request.headersX();
            paramMap.add(KName.SIGMA, headers.getString(KName.SIGMA));
            /*
             * To avoid final in lambda expression
             */
            return plugin.fetchAsync(paramMap, sources);
        }
    }

    static boolean isMatch(final JsonObject record, final KModule module) {
        /*
         * Get unique rule of current module
         */
        final KField fieldConfig = module.getField();
        final JsonArray matrix = fieldConfig.getUnique();
        /*
         * Matrix may be multi group
         */
        final int size = matrix.size();
        for (int idx = 0; idx < size; idx++) {
            final JsonArray group = matrix.getJsonArray(idx);
            if (Ut.notNil(group)) {
                final Set<String> fields = new HashSet<>();
                group.stream().filter(Objects::nonNull)
                        .map(item -> (String) item)
                        .filter(Ut::notNil)
                        .forEach(fields::add);
                final boolean match = fields.stream().allMatch(field -> Objects.nonNull(record.getValue(field)));
                if (!match) {
                    Ix.warnRest(LOGGER, "Unique checking failure, check fields: `{0}`, data = {1}",
                            Ut.fromJoin(fields), record.encode());
                    return false;
                }
            }
        }
        return true;
    }

    /*
     * This method is for uniform safeCall for Future<JsonArray> returned
     * It's shared by
     * /api/columns/{actor}/full
     * /api/columns/{actor}/my
     * Because all of above api returned JsonArray of columns on model
     */
/*    static <T> Future<Envelop> safeCall(final T stub, final Supplier<Future<Envelop>> executor) {
        if (Objects.isNull(stub)) {
            return Ux.future(new JsonArray()).compose(IxHttp::success200);
        } else {
            return executor.get();
        }
    }*/

    /*
     * Uri, Method instead
     * This method is only for save my columns, it provided fixed impact uri for clean cache
     * 1) Save my columns
     * 2) Clean up impact uri about cache flush
     */
    static JsonObject initMy(final Envelop envelop) {
        final String pattern = "/api/{0}/search";
        final String actor = Ux.getString(envelop);
        return new JsonObject()
                .put(KName.URI, MessageFormat.format(pattern, actor))
                .put(KName.METHOD, HttpMethod.POST.name());
    }
}
