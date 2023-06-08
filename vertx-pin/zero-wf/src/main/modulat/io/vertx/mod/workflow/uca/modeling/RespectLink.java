package io.vertx.mod.workflow.uca.modeling;

import cn.vertxup.workflow.domain.tables.pojos.WTicket;
import io.horizon.spi.feature.Linkage;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.workflow.atom.runtime.WRecord;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

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
        final JsonArray keys = Ut.valueJArray(dataArray, KName.KEY);
        condition.put("key,!i", keys);
        return Ux.channelA(Linkage.class, Ux::futureA, (link) ->
            // Linkage Removing / Create
            link.unlink(condition).compose(deleted -> link.link(dataArray, false))
        );
    }

    /*
     * XLinkage record fields assignment
     * - sourceKey
     * - sourceData
     * - name: calculation on name based on expression parsing.
     */
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

        final JsonObject parameters = new JsonObject();
        {
            parameters.mergeIn(Ut.fromPrefix(sourceData, KName.SOURCE));
            final JsonObject targetData = data.getJsonObject(KName.TARGET_DATA);
            parameters.mergeIn(Ut.fromPrefix(targetData, KName.TARGET));
        }
        // Parsing Expression ( Create Copy )
        final JsonObject parsed = Ut.fromExpression(data, parameters);
        data.mergeIn(parsed, true);
    }

    @Override
    public Future<JsonArray> fetchAsync(final WRecord record) {
        final WTicket ticket = record.ticket();
        final JsonObject condition = this.queryTpl();
        condition.put(KName.SOURCE_KEY, ticket.getKey());
        return Ux.channelA(Linkage.class, Ux::futureA, link -> link.fetch(condition));
    }
}
