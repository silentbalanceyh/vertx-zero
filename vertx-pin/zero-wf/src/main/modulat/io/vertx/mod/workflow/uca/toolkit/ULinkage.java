package io.vertx.mod.workflow.uca.toolkit;

import cn.vertxup.workflow.domain.tables.pojos.WTicket;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.workflow.atom.EngineOn;
import io.vertx.mod.workflow.atom.configuration.MetaInstance;
import io.vertx.mod.workflow.atom.runtime.WRecord;
import io.vertx.mod.workflow.uca.modeling.Respect;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static io.vertx.mod.workflow.refine.Wf.LOG;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ULinkage {

    private final transient MetaInstance metadata;

    public ULinkage(final MetaInstance metadata) {
        this.metadata = metadata;
    }

    private ULinkage(final WRecord record) {
        final WTicket ticket = record.ticket();
        Objects.requireNonNull(ticket);

        // Connect to Workflow Engine
        final EngineOn engine = EngineOn.connect(ticket.getFlowDefinitionKey());
        this.metadata = engine.metadata();
    }

    public static Future<WRecord> readLinkage(final WRecord record) {
        final ULinkage helper = new ULinkage(record);
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
        LOG.Web.info(this.getClass(), "( Fetch ) Linkage Definition Size: {0}", fields.size());
        fields.forEach(field -> {
            final Respect respect = this.metadata.linkRespect(field);
            futures.put(field, respect.fetchAsync(record));
        });
        return Fn.combineM(futures).compose(dataMap -> {
            dataMap.forEach(record::linkage);
            return Ux.future(record);
        });
    }

    public Future<WRecord> syncAsync(final JsonObject params, final WRecord record) {
        /*
         * Old Processing
         */
        final WRecord prev = record.prev();
        if (Objects.nonNull(prev) && prev.data().size() > 0) {
            return this.fetchAsync(prev).compose(prevRecord -> {
                record.prev(prevRecord);
                return this.syncAsyncInternal(params, record);
            });
        } else {
            return this.syncAsyncInternal(params, record);
        }
    }

    private Future<WRecord> syncAsyncInternal(final JsonObject params, final WRecord record) {
        /*
         * Linkage Sync based on configuration
         */
        final WTicket ticket = record.ticket();
        if (Objects.isNull(ticket) || this.metadata.linkSkip()) {
            return Ux.future(record);
        }
        final ConcurrentMap<String, Future<JsonArray>> futures = new ConcurrentHashMap<>();
        final Set<String> fields = this.metadata.linkFields();
        LOG.Web.info(this.getClass(), "( Sync ) Linkage Definition Size: {0}", fields.size());
        fields.forEach(field -> {
            /*
             * Data Array extract from `params` based on `field`
             */
            final JsonArray linkageData = Ut.valueJArray(params, field);
            final Respect respect = this.metadata.linkRespect(field);
            futures.put(field, respect.syncAsync(linkageData, params, record));
        });
        return Fn.combineM(futures).compose(dataMap -> {
            dataMap.forEach(record::linkage);
            return Ux.future(record);
        });
    }
}
