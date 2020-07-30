package io.vertx.tp.rbac.acl.region;

import cn.vertxup.rbac.domain.tables.daos.SVisitantDao;
import cn.vertxup.rbac.domain.tables.pojos.SVisitant;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.tp.rbac.atom.acl.AclData;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.atom.query.Inquiry;
import io.vertx.up.commune.Envelop;
import io.vertx.up.commune.secure.Acl;
import io.vertx.up.eon.Values;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 * In processing for analyzing `syntax` for calculation
 */
class DataIn {
    private static final Annal LOGGER = Annal.get(DataIn.class);

    static void visitProjection(final Envelop envelop, final JsonObject matrix, final Acl acl) {
        final JsonArray projection = matrix.getJsonArray(Inquiry.KEY_PROJECTION);
        if (Objects.nonNull(projection) && !projection.isEmpty()) {
            final JsonArray replaced = Sc.aclProjection(projection, acl);
            envelop.onProjection(replaced);
        }
    }

    static void visitCriteria(final Envelop envelop, final JsonObject matrix) {
        /* Criteria Modification */
        final JsonObject criteria = matrix.getJsonObject(Inquiry.KEY_CRITERIA);
        if (Objects.nonNull(criteria) && !criteria.isEmpty()) {
            envelop.onCriteria(criteria);
        }
    }

    /*
     * Calculated `syntax` to generate visitant condition
     * Then the system should pick up unique visitant object here
     */
    static Future<AclData> visitAcl(final Envelop envelop, final JsonObject matrix) {
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
                if (Objects.isNull(ret)) {
                    return Ux.future();
                } else {
                    return Ux.future(new AclData(ret));
                }
            });
        } else return Ux.future();
    }
}
