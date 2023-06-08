package io.vertx.mod.rbac.acl.region;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.Envelop;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class CommonCosmo implements Cosmo {
    @Override
    public Future<Envelop> before(final Envelop request, final JsonObject matrix) {
        /* Projection Modification */
        DataIn.visitProjection(request, matrix);
        /* Criteria Modification */
        DataIn.visitCriteria(request, matrix);
        return Ux.future(request);
    }

    @Override
    public Future<Envelop> after(final Envelop response, final JsonObject matrix) {
        /* Projection */
        DataOut.dwarfRecord(response, matrix);
        /* Rows */
        DataOut.dwarfRows(response, matrix);
        /* Projection For Array */
        DataOut.dwarfCollection(response, matrix);


        /* Infusion for */
        DataOut.dwarfAddon(response, matrix);
        return Ux.future(response);
    }
}
