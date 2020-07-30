package io.vertx.tp.rbac.region;

import cn.vertxup.rbac.domain.tables.daos.SVisitantDao;
import cn.vertxup.rbac.domain.tables.pojos.SVisitant;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.tp.rbac.acl.AclData;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.Values;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 * In processing for analyzing `syntax` for calculation
 */
class DataIn {
    private static final Annal LOGGER = Annal.get(DataIn.class);

    /*
     * Calculated `syntax` to generate visitant condition
     * Then the system should pick up unique visitant object here
     */
    static Future<AclData> visitCond(final Envelop envelop, final JsonObject matrix) {
        /*
         * Read configuration of `seeker` here
         ***/
        final JsonObject seeker = matrix.getJsonObject("seeker");
        final JsonObject input = new JsonObject();
        {
            /*
             * Build input data as parameters to build condition
             */
            final JsonObject viewData = matrix.getJsonObject(KeField.VIEW);
            final JsonObject data = envelop.body();
            input.mergeIn(data, true);
            input.put(KeField.RESOURCE_ID, viewData.getString(KeField.RESOURCE_ID));
            input.put(KeField.SIGMA, viewData.getString(KeField.SIGMA));
            input.put(KeField.LANGUAGE, viewData.getString(KeField.LANGUAGE));
            input.put("viewId", viewData.getString(KeField.KEY));
        }
        final JsonObject condition = new JsonObject();
        {
            final JsonObject syntax = seeker.getJsonObject("syntax");
            Ut.<String>itJObject(syntax, (expr, field) -> {
                final String literal;
                if (expr.contains("`")) {
                    literal = Ut.fromExpression(expr, input);
                } else {
                    literal = expr;
                }
                condition.put(field, literal);
            });
        }
        LOGGER.info("Visitant unique query condition: {0}", condition);
        if (Ut.notNil(condition)) {
            return Ux.Jooq.on(SVisitantDao.class).<SVisitant>fetchAndAsync(condition).compose(visitant -> {
                final SVisitant ret;
                if (0 < visitant.size()) {
                    ret = visitant.get(Values.IDX);
                } else {
                    ret = null;
                }
                return Ux.future(ret);
            }).compose(visitant -> Ux.future(new AclData(visitant)));
        } else return Ux.future();
    }
}
