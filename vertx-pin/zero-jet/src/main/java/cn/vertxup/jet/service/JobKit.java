package cn.vertxup.jet.service;

import cn.vertxup.jet.domain.tables.pojos.IService;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.worker.Mission;
import io.vertx.up.eon.KName;
import io.vertx.up.uca.job.plugin.JobClient;
import io.vertx.up.uca.job.plugin.JobInfix;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Set;

/*
 *  Job kit here for configuration
 */
public class JobKit {
    private static final JobClient CLIENT = JobInfix.getClient();

    /*
     * Could not use old code here
     *
     * private static final List<Mission> MISSION_LIST = JobPool.get();
     */
    static Future<JsonArray> fetchMission(final Set<String> codes) {
        if (Objects.isNull(CLIENT)) {
            return Ux.futureA();
        } else {
            return CLIENT.fetchAsync(codes).compose(missionList -> {
                final JsonArray response = new JsonArray();
                missionList.forEach(item -> response.add(JobKit.toJson(item)));
                return Ux.future(response);
            });
        }
    }

    static Future<JsonObject> fetchMission(final String code) {
        if (Objects.isNull(CLIENT)) {
            return Ux.futureJ();
        } else {
            return CLIENT.fetchAsync(code).compose(found -> {
                if (Objects.isNull(found)) {
                    return Ux.future(new JsonObject());
                } else {
                    return Ux.future(toJson(found));
                }
            });
        }
    }

    public static IService fromJson(final JsonObject serviceJson) {
        //        Ke.mountString(serviceJson, KName.METADATA);
        //        Ke.mountString(serviceJson, KName.RULE_UNIQUE);
        //
        //        Ke.mountString(serviceJson, KName.Api.CONFIG_INTEGRATION);
        //        Ke.mountString(serviceJson, KName.Api.CONFIG_DATABASE);
        //
        //        Ke.mountString(serviceJson, KName.Api.CHANNEL_CONFIG);
        //        Ke.mountString(serviceJson, KName.Api.SERVICE_CONFIG);
        //        Ke.mountString(serviceJson, KName.Api.MAPPING_CONFIG);
        //        Ke.mountString(serviceJson, KName.Api.DICT_EPSILON);
        //        Ke.mountString(serviceJson, KName.Api.DICT_CONFIG);
        Ut.valueToString(serviceJson,
            KName.METADATA,
            KName.RULE_UNIQUE,
            /*
             * Zero standard configuration
             * 1) Integration
             * 2) Database
             * Here should be configuration for `Database` & `Integration`
             */
            KName.Api.CONFIG_INTEGRATION,
            KName.Api.CONFIG_DATABASE,
            /*
             * 1) channelConfig - Channel Component configuration
             * 2) serviceConfig - Service Component configuration
             * 3) dictConfig = Dict Component configuration
             * 4) mappingConfig = Mapping Component configuration
             */
            KName.Api.CHANNEL_CONFIG,
            KName.Api.SERVICE_CONFIG,
            KName.Api.MAPPING_CONFIG,
            KName.Api.DICT_EPSILON,
            KName.Api.DICT_CONFIG
        );
        return Ux.fromJson(serviceJson, IService.class);
    }

    public static JsonObject toJson(final Mission mission) {
        final JsonObject serialized = Ut.serializeJson(mission);
        final JsonObject metadata = serialized.getJsonObject(KName.METADATA);
        if (Ut.isNotNil(metadata)) {
            final JsonObject service = metadata.getJsonObject(KName.SERVICE);
            if (Ut.isNotNil(service)) {
                Ut.valueToJObject(service,
                    KName.METADATA,
                    KName.RULE_UNIQUE,
                    /*
                     * Zero standard configuration
                     * 1) Integration
                     * 2) Database
                     * Here should be configuration for `Database` & `Integration`
                     */
                    KName.Api.CONFIG_INTEGRATION,
                    KName.Api.CONFIG_DATABASE,
                    /*
                     * 1) channelConfig - Channel Component configuration
                     * 2) serviceConfig - Service Component configuration
                     * 3) dictConfig = Dict Component configuration
                     * 4) mappingConfig = Mapping Component configuration
                     */
                    KName.Api.CHANNEL_CONFIG,
                    KName.Api.SERVICE_CONFIG,
                    KName.Api.MAPPING_CONFIG,
                    KName.Api.DICT_EPSILON,
                    KName.Api.DICT_CONFIG
                );
                /*
                Ke.mount(service, KName.METADATA);
                Ke.mount(service, KName.RULE_UNIQUE);


                Ke.mount(service, KName.Api.CONFIG_INTEGRATION);
                Ke.mount(service, KName.Api.CONFIG_DATABASE);

                Ke.mount(service, KName.Api.CHANNEL_CONFIG);
                Ke.mount(service, KName.Api.SERVICE_CONFIG);
                Ke.mount(service, KName.Api.MAPPING_CONFIG);
                Ke.mount(service, KName.Api.DICT_EPSILON);
                Ke.mountArray(service, KName.Api.DICT_CONFIG);
                */
            }
        }
        return serialized;
    }
}
