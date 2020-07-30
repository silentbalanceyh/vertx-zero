package io.vertx.tp.rbac.acl.region;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.Envelop;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class SeekCosmo implements Cosmo {
    @Override
    public Future<Envelop> before(final Envelop request, final JsonObject matrix) {
        final JsonObject seeker = matrix.getJsonObject("seeker");
        final String component = seeker.getString("component");
        if (Ut.notNil(component)) {
            final Cosmo external = Ut.singleton(component);
            return external.before(request, matrix);
        } else {
            return DataIn.visitAcl(request, matrix).compose(acl -> {
                request.acl(acl);
                /* Projection Modification */
                DataIn.visitProjection(request, matrix, acl);
                /* Criteria Modification */
                DataIn.visitCriteria(request, matrix);
                return Ux.future(request);
            });
        }
    }

    @Override
    public Future<Envelop> after(final Envelop response, final JsonObject matrix) {
        final JsonObject seeker = matrix.getJsonObject("seeker");
        final String component = seeker.getString("component");
        if (Ut.notNil(component)) {
            final Cosmo external = Ut.singleton(component);
            return external.after(response, matrix);
        } else {
            /* Projection */
            DataOut.dwarfRecord(response, matrix);
            /* Rows */
            DataOut.dwarfRows(response, matrix);
            /* Projection For Array */
            DataOut.dwarfCollection(response, matrix);
            return Ux.future(response);
        }
    }
}
