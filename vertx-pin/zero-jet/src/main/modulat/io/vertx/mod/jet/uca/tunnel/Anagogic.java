package io.vertx.mod.jet.uca.tunnel;

import io.horizon.spi.jet.JtComponent;
import io.modello.specification.atom.HRule;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.exchange.BTree;
import io.vertx.up.atom.exchange.DFabric;
import io.vertx.up.commune.Envelop;
import io.vertx.up.commune.config.Database;
import io.vertx.up.commune.config.Identity;
import io.vertx.up.commune.config.XHeader;
import io.vertx.up.specification.action.Commercial;
import io.vertx.up.uca.cache.Rapid;
import io.vertx.up.uca.cache.RapidKey;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.function.Supplier;

/*
 * Tool for different injection
 * 1）Database injection method
 * 2) Dict processing for `Dict` processing here.
 * 3) Diode processing for
 * - BEFORE -->
 * - AFTER  <--
 * - AROUND <->
 * 4) Enable plug-in for processing
 * - Dict plug-in for key = JsonArray processing
 * -- Dict ( Assist ) plugin here
 */
class Anagogic {
    /*
     * Database processing
     */
    static Future<Database> databaseAsync(final Commercial commercial) {
        return Rapid.<String, Database>t(RapidKey.DATABASE_MULTI)
            .cached(commercial.app(), () -> Ux.future(commercial.database()));
    }

    static Future<Boolean> componentAsync(final JtComponent component, final Envelop envelop) {
        final JsonObject headers = envelop.headersX();
        final XHeader header = new XHeader();
        header.fromJson(headers);
        Ut.contract(component, XHeader.class, header);
        return Ux.future(Boolean.TRUE);
    }

    static Future<Boolean> componentAsync(final JtComponent component, final Commercial commercial, final Supplier<Future<DFabric>> supplier) {
        if (Objects.nonNull(commercial)) {
            return supplier.get().compose(fabric -> {
                /*
                 * JsonObject options inject ( without `mapping` node for Diode )
                 */
                final JsonObject options = Ut.valueJObject(commercial.options());

                Ut.contract(component, JsonObject.class, options);                  /* serviceConfig */
                Ut.contract(component, Identity.class, commercial.identity());      /* identifierComponent -> converted to identity */
                Ut.contract(component, BTree.class, commercial.mapping());    /* mappingConfig */
                Ut.contract(component, DFabric.class, fabric);                   /* dictConfig -> converted to fabric */
                Ut.contract(component, HRule.class, commercial.rule());        /* Rule Unique */

                return Future.succeededFuture(Boolean.TRUE);
            });
        } else {
            return Future.succeededFuture(Boolean.TRUE);
        }
    }
}
