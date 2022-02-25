package io.vertx.tp.workflow.uca.component;

import cn.vertxup.workflow.domain.tables.pojos.WTicket;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.EngineOn;
import io.vertx.tp.workflow.atom.MetaInstance;
import io.vertx.tp.workflow.atom.WRecord;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.tp.workflow.uca.modeling.Respect;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class HelperLinkage {

    private final transient MetaInstance metadata;

    HelperLinkage(final MetaInstance metadata) {
        this.metadata = metadata;
    }

    private HelperLinkage(final WRecord record) {
        final WTicket ticket = record.ticket();
        Objects.requireNonNull(ticket);

        // Connect to Workflow Engine
        final EngineOn engine = EngineOn.connect(ticket.getFlowDefinitionKey());
        this.metadata = engine.metadata();
    }

    public static Future<WRecord> readLinkage(final WRecord record) {
        final HelperLinkage helper = new HelperLinkage(record);
        return helper.fetchAsync(record);
    }

    private Future<WRecord> fetchAsync(final WRecord record) {
        // Linkage Extract
        if (this.metadata.linkSkip()) {
            return Ux.future(record);
        }

        // ConcurrentMap
        final ConcurrentMap<String, Future<JsonArray>> futures = new ConcurrentHashMap<>();
        final Set<String> fields = this.metadata.linkFields();
        Wf.Log.infoWeb(this.getClass(), "( Fetch ) Linkage Definition Size: {0}", fields.size());
        fields.forEach(field -> {
            final Respect respect = this.metadata.linkRespect(field);
            futures.put(field, respect.fetchAsync(record));
        });
        return Ux.thenCombine(futures).compose(dataMap -> {
            dataMap.forEach(record::linkage);
            return Ux.future(record);
        });
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
        Wf.Log.infoWeb(this.getClass(), "( Sync ) Linkage Definition Size: {0}", fields.size());
        fields.forEach(field -> {
            /*
             * Data Array extract from `params` based on `field`
             */
            final JsonArray linkageData = Ut.valueJArray(params, field);
            if (Ut.notNil(linkageData)) {
                final Respect respect = this.metadata.linkRespect(field);
                futures.put(field, respect.syncAsync(linkageData, params, record));
            }
        });
        return Ux.thenCombine(futures).compose(dataMap -> {
            dataMap.forEach(record::linkage);
            return Ux.future(record);
        });
    }
}
