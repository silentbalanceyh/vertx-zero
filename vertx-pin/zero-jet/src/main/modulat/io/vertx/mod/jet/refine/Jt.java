package io.vertx.mod.jet.refine;

import cn.vertxup.jet.domain.tables.pojos.IApi;
import cn.vertxup.jet.domain.tables.pojos.IJob;
import cn.vertxup.jet.domain.tables.pojos.IService;
import io.horizon.eon.VString;
import io.horizon.uca.log.Log;
import io.horizon.uca.log.LogModule;
import io.macrocosm.specification.program.HArk;
import io.modello.specification.atom.HRule;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.jet.atom.JtConfig;
import io.vertx.mod.jet.atom.JtUri;
import io.vertx.mod.jet.atom.JtWorker;
import io.vertx.up.atom.exchange.BTree;
import io.vertx.up.atom.exchange.DSetting;
import io.vertx.up.commune.config.Database;
import io.vertx.up.commune.config.Identity;
import io.vertx.up.commune.config.Integration;
import io.vertx.up.eon.em.EmTraffic;
import jakarta.ws.rs.core.MediaType;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Jt {

    public static String jobCode(final IJob job) {
        return job.getNamespace() + VString.DASH + job.getCode();
    }

    /*
     * Extraction for some specification data
     */
    public static String toPath(final HArk ark, final Supplier<String> uriSupplier,
                                final boolean secure, final JtConfig external) {
        return JtRoute.toPath(ark, uriSupplier, secure, external);
    }

    public static String toPath(final HArk ark, final Supplier<String> uriSupplier,
                                final boolean secure) {
        return JtRoute.toPath(ark, uriSupplier, secure);
    }

    public static Set<MediaType> toMime(final Supplier<String> supplier) {
        return JtRoute.toMime(supplier);
    }

    public static Set<String> toMimeString(final Supplier<String> supplier) {
        return toMime(supplier).stream()
            .map(type -> type.getType() + VString.SLASH + type.getSubtype())
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

    public static Class<?> toChannel(final Supplier<String> supplier, final EmTraffic.Channel type) {
        return JtType.toChannel(supplier, type);
    }

    public static void initApi(final IApi api) {
        JtDataObject.initApi(api);
    }

    public static JsonObject toOptions(final IService service, final IApi api) {
        return JtDataObject.toOptions(service, api);
    }

    public static JsonObject toOptions(final IService service, final IJob job) {
        return JtDataObject.toOptions(service, job);
    }

    public static JsonObject toOptions(final IService service) {
        return JtDataObject.toOptions(service);
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

    /**
     * 双通道模式下的数据库
     *
     * @param service 通道服务接口
     *
     * @return 选择的数据库
     */
    public static Database toDatabase(final IService service) {
        return JtDataObject.toDatabase(service);
    }

    public static HRule toRule(final IService service) {
        return JtDataObject.toRule(service);
    }

    public static Integration toIntegration(final IService service) {
        return JtDataObject.toIntegration(service);
    }

    public interface LOG {
        String MODULE = "Πίδακας δρομολογητή";

        LogModule Init = Log.modulat(MODULE).program("Init");
        LogModule Route = Log.modulat(MODULE).program("Route");
        LogModule Worker = Log.modulat(MODULE).program("Worker");
        LogModule Web = Log.modulat(MODULE).program("Web");
        LogModule App = Log.modulat(MODULE).program("App");
    }
}
