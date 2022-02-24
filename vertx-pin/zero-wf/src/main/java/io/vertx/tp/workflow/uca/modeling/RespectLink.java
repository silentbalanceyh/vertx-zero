package io.vertx.tp.workflow.uca.modeling;

import cn.vertxup.workflow.domain.tables.pojos.WTicket;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.feature.Linkage;
import io.vertx.tp.workflow.atom.WRecord;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.stream.Collectors;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class RespectLink extends AbstractRespect {

    public RespectLink(final JsonObject query) {
        super(query);
    }

    @Override
    public Future<JsonArray> syncAsync(final JsonArray data, final JsonObject params, final WRecord record) {
        final JsonArray dataArray = this.syncPre(data, params, record);
        /*
         * Build condition based on
         * DEFAULT
         * - sourceKey = key
         *
         * CONFIGURATION
         * - targetType
         * - sourceType
         */
        final WTicket ticket = record.ticket();
        final JsonObject condition = this.queryTpl();
        condition.put(KName.SOURCE_KEY, ticket.getKey());
        /*
         * Exclude the data stored in database here
         */
        final JsonArray keys = Ut.toJArray(Ut.itJArray(dataArray)
            .map(json -> json.getString(KName.KEY))
            .filter(Ut::notNil)
            .collect(Collectors.toSet()));
        condition.put("key,!i", keys);
        return Ke.channelAsync(Linkage.class, Ux::futureA, (link) ->
            // Linkage Processing build for Each
            link.unlink(condition).compose(deleted -> link.link(dataArray, false))
        );
    }

    @Override
    protected void syncPre(final JsonObject data, final JsonObject params, final WRecord record) {
        /*
         * When add new linkage for saving, here provide additional
         * - sourceKey
         * - sourceData
         *
         * Metadata Fields
         */
        final WTicket ticket = record.ticket();
        final JsonObject sourceData = Ux.toJson(ticket);
        // If not `sourceKey`, here put sourceKey
        data.put(KName.SOURCE_KEY, ticket.getKey());
        data.put(KName.SOURCE_DATA, sourceData);
        /*
         * Name Processing
         */
        // name parsing on ADD / UPDATE
        final String literal = data.getString(KName.NAME);
        if (Ut.notNil(literal) && literal.contains("`")) {
            final JsonObject targetData = data.getJsonObject(KName.TARGET_DATA);
            // parameters building
            final JsonObject parameters = new JsonObject();
            sourceData.fieldNames()
                .forEach(k -> parameters.put("source." + k, sourceData.getValue(k)));
            targetData.fieldNames()
                .forEach(k -> parameters.put("target." + k, sourceData.getValue(k)));
            data.put(KName.NAME, Ut.fromExpression(literal, parameters));
        }
    }

    @Override
    public Future<JsonArray> fetchAsync(final WRecord record) {
        final WTicket ticket = record.ticket();
        final String sourceKey = ticket.getKey();
        final JsonObject condition = this.queryTpl();
        condition.put(KName.SOURCE_KEY, sourceKey);
        return Ke.channelAsync(Linkage.class, Ux::futureA, link -> link.fetch(condition));
    }
}
