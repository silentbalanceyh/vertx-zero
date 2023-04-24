package io.vertx.tp.jet.refine;

import cn.vertxup.jet.domain.tables.pojos.IApi;
import cn.vertxup.jet.domain.tables.pojos.IJob;
import cn.vertxup.jet.domain.tables.pojos.IService;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.jet.atom.JtApp;
import io.vertx.tp.jet.atom.JtConfig;
import io.vertx.tp.jet.atom.JtUri;
import io.vertx.tp.jet.atom.JtWorker;
import io.vertx.up.commune.config.Database;
import io.vertx.up.commune.config.Identity;
import io.vertx.up.commune.config.Integration;
import io.vertx.up.commune.exchange.BTree;
import io.vertx.up.commune.exchange.DSetting;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.em.ChannelType;
import io.aeon.experiment.rule.RuleUnique;
import io.vertx.up.log.Annal;
import jakarta.ws.rs.core.MediaType;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Jt {

    public static void infoInit(final Annal logger, final String pattern, final Object... args) {
        JtLog.info(logger, "Init", pattern, args);
    }

    public static void infoRoute(final Annal logger, final String pattern, final Object... args) {
        JtLog.info(logger, "Route", pattern, args);
    }

    public static void infoWorker(final Annal logger, final String pattern, final Object... args) {
        JtLog.info(logger, "Worker", pattern, args);
    }

    public static void infoWeb(final Annal logger, final String pattern, final Object... args) {
        JtLog.info(logger, "Wet", pattern, args);
    }

    public static void warnApp(final Annal logger, final String pattern, final Object... args) {
        JtLog.warn(logger, "Ambient", pattern, args);
    }

    public static void warnInfo(final Annal logger, final String pattern, final Object... args) {
        JtLog.warn(logger, "Init", pattern, args);
    }

    public static String jobCode(final IJob job) {
        return job.getNamespace() + Strings.DASH + job.getCode();
    }

    /*
     * Extraction for some specification data
     */
    public static String toPath(final Supplier<String> routeSupplier, final Supplier<String> uriSupplier,
                                final boolean secure, final JtConfig external) {
        return JtRoute.toPath(routeSupplier, uriSupplier, secure, external);
    }

    public static String toPath(final Supplier<String> routeSupplier, final Supplier<String> uriSupplier,
                                final boolean secure) {
        return JtRoute.toPath(routeSupplier, uriSupplier, secure);
    }

    public static Set<MediaType> toMime(final Supplier<String> supplier) {
        return JtRoute.toMime(supplier);
    }

    public static Set<String> toMimeString(final Supplier<String> supplier) {
        return toMime(supplier).stream()
            .map(type -> type.getType() + Strings.SLASH + type.getSubtype())
            .collect(Collectors.toSet());
    }

    /*
     * IService -> Dict
     */
    public static DSetting toDict(final IService service) {
        return JtBusiness.toDict(service);
    }

    /*
     * IService -> DualMapping
     */
    public static BTree toMapping(final IService service) {
        return JtBusiness.toMapping(service);
    }

    /*
     * IService -> Identify
     */
    public static Identity toIdentity(final IService service) {
        return JtBusiness.toIdentify(service);
    }

    public static Future<ConcurrentMap<String, JsonArray>> toDictionary(final String key, final String cacheKey, final String identifier, final DSetting dict) {
        return JtBusiness.toDictionary(key, cacheKey, identifier, dict);
    }

    public static Set<String> toSet(final Supplier<String> supplier) {
        return JtRoute.toSet(supplier);
    }

    /*
     * Type extraction
     */
    public static JtWorker toWorker(final IApi api) {
        return JtType.toWorker(api);
    }

    public static Class<?> toChannel(final Supplier<String> supplier, final ChannelType type) {
        return JtType.toChannel(supplier, type);
    }

    public static void initApi(final IApi api) {
        JtDataObject.initApi(api);
    }

    public static JsonObject toOptions(final JtApp app, final IApi api, final IService service) {
        return JtDataObject.toOptions(app, api, service);
    }

    public static JsonObject toOptions(final JtApp app, final IJob job, final IService service) {
        return JtDataObject.toOptions(app, job, service);
    }

    public static JsonObject toOptions(final JtApp app, final IService service) {
        return JtDataObject.toOptions(app, service);
    }

    /*
     * Ask configuration, before deployVerticle here
     * 1. JtUri -> JsonObject
     * 2. Set -> Map ( key -> JtUri -> JsonObject )
     * 3. Before deployment of Verticle
     */
    public static ConcurrentMap<String, JsonObject> ask(final Set<JtUri> uriSet) {
        return JtDelivery.ask(uriSet);
    }

    /*
     * Answer configuration, after deployVerticle here
     * 1. JsonObject -> JtUri
     * 2. Map ( key -> apiKey -> JsonObject -> JtUri )
     * 3. After deployment of Verticle ( Consume )
     */
    public static ConcurrentMap<String, JtUri> answer(final JsonObject config) {
        return JtDelivery.answer(config);
    }

    /*
     * App, Database, Integration
     */
    public static Database toDatabase(final Supplier<String> supplier, final Database defaultDatabase) {
        return JtDataObject.toDatabase(supplier, defaultDatabase);
    }

    public static Database toDatabase(final IService service) {
        return JtDataObject.toDatabase(service);
    }

    public static RuleUnique toRule(final IService service) {
        return JtDataObject.toRule(service);
    }

    public static Integration toIntegration(final IService service) {
        return JtDataObject.toIntegration(service);
    }
}
