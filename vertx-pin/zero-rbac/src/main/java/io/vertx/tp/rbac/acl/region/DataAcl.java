package io.vertx.tp.rbac.acl.region;

import cn.vertxup.rbac.domain.tables.daos.SVisitantDao;
import cn.vertxup.rbac.domain.tables.pojos.SVisitant;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.atom.acl.AclData;
import io.vertx.tp.rbac.cv.em.AclTime;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.commune.Envelop;
import io.vertx.up.commune.secure.Acl;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Values;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class DataAcl {
    private static final Annal LOGGER = Annal.get(DataAcl.class);

    /*
     * Calculated `syntax` to generate visitant condition
     * Then the system should pick up unique visitant object here
     */
    static Future<Acl> visitAcl(final Envelop envelop, final JsonObject matrix,
                                final AclTime expected) {
        /*
         * Read configuration of `seeker` here
         * 1) Read syntax `BEFORE/AFTER` to match
         * 2）To avoid missing acl configuration information, the default phase is `BEFORE`
         * 3）Input and Define must be matched
         ***/
        final JsonObject seeker = matrix.getJsonObject(KName.SEEKER);
        final JsonObject syntax = seeker.getJsonObject(KName.SYNTAX);
        final AclTime actual = Ut.toEnum(() -> syntax.getString(KName.PHASE), AclTime.class, AclTime.BEFORE);
        if (expected == actual) {
            final JsonObject input = new JsonObject();
            {
                /*
                 * Build input data as parameters to build condition
                 */
                final JsonObject viewData = matrix.getJsonObject(KName.VIEW);
                final JsonObject data = envelop.body();
                input.mergeIn(data, true);
                input.put(KName.RESOURCE_ID, viewData.getString(KName.RESOURCE_ID));
                input.put(KName.SIGMA, viewData.getString(KName.SIGMA));
                input.put(KName.LANGUAGE, viewData.getString(KName.LANGUAGE));
                input.put(KName.VIEW_ID, viewData.getString(KName.KEY));
            }
            final JsonObject condition = new JsonObject();
            {
                final JsonObject syntaxData = Ut.sureJObject(syntax.getJsonObject(KName.DATA));
                Ut.<String>itJObject(syntaxData, (expr, field) -> {
                    final String literal;
                    if (expr.contains("`")) {
                        literal = Ut.fromExpression(expr, input);
                    } else {
                        literal = expr;
                    }
                    if (Ut.notNil(literal)) {
                        condition.put(field, literal);
                    }
                });
            }
            Sc.infoView(DataAcl.class, "Visitant unique query condition: {0}", condition);
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
                        return Ux.future(new AclData(ret).config(syntax.getJsonObject(KName.CONFIG)));
                    }
                });
            } else return Ux.future();
        } else {
            /*
             * Keep no change here for envelop acl
             */
            return Ux.future(envelop.acl());
        }
    }
}
