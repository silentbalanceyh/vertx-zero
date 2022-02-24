package io.vertx.tp.workflow.uca.component;

import cn.vertxup.workflow.domain.tables.pojos.WTicket;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.feature.Linkage;
import io.vertx.tp.workflow.atom.MetaInstance;
import io.vertx.tp.workflow.atom.WRecord;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class HelperLinkage {

    private final transient MetaInstance metadata;

    HelperLinkage(final MetaInstance metadata) {
        this.metadata = metadata;
    }

    Future<WRecord> syncAsync(final JsonObject params, final WRecord record) {
        /*
         * Linkage Sync based on configuration
         */
        final WTicket ticket = record.ticket();
        if (Objects.isNull(ticket) || this.metadata.linkSkip()) {
            return Ux.future(record);
        }
        final ConcurrentMap<String, Future<JsonArray>> futures = new ConcurrentHashMap<>();
        final Set<String> fields = this.metadata.linkFields();
        Wf.Log.infoWeb(this.getClass(), "Linkage Definition Size: {0}", fields.size());
        fields.forEach(field -> {
            /*
             * Data Array extract from `params` based on `field`
             */
            final JsonArray linkageData = this.buildData(params, field, ticket);
            if (Ut.notNil(linkageData)) {
                /*
                 * Build condition based on
                 * DEFAULT
                 * - sourceKey = key
                 *
                 * CONFIGURATION
                 * - targetType
                 * - sourceType
                 */
                final String sourceKey = ticket.getKey();
                final JsonObject condition = this.metadata.linkQuery(field);
                condition.put(KName.SOURCE_KEY, sourceKey);
                futures.put(field, this.buildEach(condition, linkageData));
            }
        });
        return Ux.thenCombine(futures).compose(dataMap -> {
            dataMap.forEach(record::linkage);
            return Ux.future(record);
        });
    }

    @SuppressWarnings("all")
    private void buildName(final JsonObject json, final String field, final JsonObject sourceData) {
        // name parsing on ADD / UPDATE
        final String literal = json.getString(field);
        if (Ut.notNil(literal) && literal.contains("`")) {
            final JsonObject targetData = json.getJsonObject(KName.TARGET_DATA);
            // parameters building
            final JsonObject parameters = new JsonObject();
            sourceData.fieldNames()
                .forEach(k -> parameters.put("source." + k, sourceData.getValue(k)));
            targetData.fieldNames()
                .forEach(k -> parameters.put("target." + k, sourceData.getValue(k)));
            json.put(field, Ut.fromExpression(literal, parameters));
        }
    }

    private JsonArray buildData(final JsonObject params, final String field, final WTicket ticket) {
        final JsonArray linkageData = Ut.sureJArray(params, field);
        /*
         * When add new linkage for saving, here provide additional
         * - sourceKey
         * - sourceData
         *
         * Metadata Fields
         */
        final JsonObject sourceData = Ux.toJson(ticket);
        Ut.itJArray(linkageData).forEach(json -> {
            // If not `sourceKey`, here put sourceKey
            json.put(KName.SOURCE_KEY, ticket.getKey());

            // name parsing on ADD / UPDATE
            this.buildName(json, KName.NAME, sourceData);

            if (!json.containsKey(KName.CREATED_BY)) {
                // Created new linkage
                // - createdAt, createdBy
                json.put(KName.CREATED_BY, params.getValue(KName.UPDATED_BY));
                json.put(KName.CREATED_AT, params.getValue(KName.UPDATED_AT));
            }

            // All information came from
            Ut.jsonCopy(json, params,
                KName.UPDATED_BY,
                KName.UPDATED_AT,
                KName.ACTIVE,
                KName.LANGUAGE,
                KName.SIGMA
            );

            // Updated source data
            json.put(KName.SOURCE_DATA, sourceData);
        });
        return linkageData;
    }

    private Future<JsonArray> buildEach(final JsonObject condition, final JsonArray data) {
        /*
         * Exclude the data stored in database here
         */
        final JsonArray keys = Ut.toJArray(Ut.itJArray(data)
            .map(json -> json.getString(KName.KEY))
            .filter(Ut::notNil)
            .collect(Collectors.toSet()));
        condition.put("key,!i", keys);
        return Ke.channelAsync(Linkage.class, Ux::futureA, (link) ->
            // Linkage Processing build for Each
            link.unlink(condition).compose(deleted -> link.link(data, false))
        );
    }
}
