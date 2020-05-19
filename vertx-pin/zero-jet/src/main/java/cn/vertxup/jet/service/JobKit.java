package cn.vertxup.jet.service;

import cn.vertxup.jet.domain.tables.pojos.IService;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.plugin.job.JobPool;
import io.vertx.up.atom.worker.Mission;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/*
 *  Job kit here for configuration
 */
public class JobKit {
    /*
     * Could not use old code here
     *
     * private static final List<Mission> MISSION_LIST = JobPool.get();
     */
    static Future<JsonArray> fetchMission(final Set<String> codes) {
        final List<Mission> missionList = JobPool.get();
        if (Objects.isNull(codes) || codes.isEmpty()) {
            return Ux.future(new JsonArray());
        } else {
            final JsonArray response = new JsonArray();
            missionList.stream()
                    .filter(mission -> codes.contains(mission.getCode()))
                    .map(JobKit::toJson)
                    .forEach(response::add);
            return Ux.future(response);
        }
    }

    static Future<JsonObject> fetchMission(final String code) {
        final List<Mission> missionList = JobPool.get();
        final Mission found = missionList.stream()
                .filter(mission -> code.equals(mission.getCode()))
                .findFirst().orElse(null);
        if (Objects.isNull(found)) {
            return Ux.future(new JsonObject());
        } else {
            return Ux.future(toJson(found));
        }
    }

    public static IService fromJson(final JsonObject serviceJson) {
        Ke.mountString(serviceJson, KeField.METADATA);
        Ke.mountString(serviceJson, KeField.RULE_UNIQUE);

        Ke.mountString(serviceJson, KeField.Api.CONFIG_INTEGRATION);
        Ke.mountString(serviceJson, KeField.Api.CONFIG_DATABASE);

        Ke.mountString(serviceJson, KeField.Api.CHANNEL_CONFIG);
        Ke.mountString(serviceJson, KeField.Api.SERVICE_CONFIG);
        Ke.mountString(serviceJson, KeField.Api.MAPPING_CONFIG);
        Ke.mountString(serviceJson, KeField.Api.DICT_EPSILON);
        Ke.mountString(serviceJson, KeField.Api.DICT_CONFIG);
        return Ux.fromJson(serviceJson, IService.class);
    }

    public static JsonObject toJson(final Mission mission) {
        final JsonObject serialized = Ut.serializeJson(mission);
        final JsonObject metadata = serialized.getJsonObject(KeField.METADATA);
        if (Ut.notNil(metadata)) {
            final JsonObject service = metadata.getJsonObject(KeField.SERVICE);
            if (Ut.notNil(service)) {
                Ke.mount(service, KeField.METADATA);
                Ke.mount(service, KeField.RULE_UNIQUE);

                /*
                 * Zero standard configuration
                 * 1) Integration
                 * 2) Database
                 * Here should be configuration for `Database` & `Integration`
                 */
                Ke.mount(service, KeField.Api.CONFIG_INTEGRATION);
                Ke.mount(service, KeField.Api.CONFIG_DATABASE);

                /*
                 * 1) channelConfig - Channel Component configuration
                 * 2) serviceConfig - Service Component configuration
                 * 3) dictConfig = Dict Component configuration
                 * 4) mappingConfig = Mapping Component configuration
                 */
                Ke.mount(service, KeField.Api.CHANNEL_CONFIG);
                Ke.mount(service, KeField.Api.SERVICE_CONFIG);
                Ke.mount(service, KeField.Api.MAPPING_CONFIG);
                Ke.mount(service, KeField.Api.DICT_EPSILON);
                Ke.mountArray(service, KeField.Api.DICT_CONFIG);
            }
        }
        return serialized;
    }
}
