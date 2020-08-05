package io.vertx.tp.rbac.acl.region;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.cv.em.AclTime;
import io.vertx.up.commune.Envelop;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class SeekCosmo implements Cosmo {
    /*
     *
     * Here are two critical concept for
     * 1) AclTime: When to fetch acl information from database ?
     *    This workflow is in `DataAcl.visitAcl` method.
     *      BEFORE: before(Envelop, matrix) will call BEFORE time here, it means fetch acl before do action on
     *        database and the acl could be picked up from database if it's ok
     *        When the parameters are match `syntax` such as provide all parameters, this workflow is
     *        ok because all the required condition will be matched.
     *      AFTER: after(Envelop, matrix) will call AFTER time here, it means fetch acl after do action on
     *        database and the acl could be picked up from database if it's ok
     *        When the parameters are often `key` only, after database that you could get all record here
     *
     * 2) AclPhase: How to use acl
     *      EAGER: The acl information should be used in current request
     *      DELAY: The acl information should be returned only for future usage in front and it does not
     *        impact current request
     */
    @Override
    public Future<Envelop> before(final Envelop request, final JsonObject matrix) {
        final JsonObject seeker = matrix.getJsonObject("seeker");
        final String component = seeker.getString("component");
        if (Ut.notNil(component)) {
            final Cosmo external = Ut.singleton(component);
            return external.before(request, matrix);
        } else {
            if (Ut.isNil(seeker)) {

                /* Projection Modification */
                DataIn.visitProjection(request, matrix);

                /* Criteria Modification */
                DataIn.visitCriteria(request, matrix);

                return Ux.future(request);
            } else {
                /* Before calling and will capture `BEFORE` syntax */
                return DataAcl.visitAcl(request, matrix, AclTime.BEFORE).compose(acl -> {
                    request.acl(acl);

                    /* Projection Modification */
                    DataIn.visitProjection(request, matrix);

                    /* Criteria Modification */
                    DataIn.visitCriteria(request, matrix);

                    return Ux.future(request);
                });
            }
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
            /* After calling and will capture `AFTER` syntax */
            return DataAcl.visitAcl(response, matrix, AclTime.AFTER).compose(acl -> {
                response.acl(acl);

                /* Projection */
                DataOut.dwarfRecord(response, matrix);

                /* Rows */
                DataOut.dwarfRows(response, matrix);

                /* Projection For Array */
                DataOut.dwarfCollection(response, matrix);

                /*
                 * Append data of `acl` into description for future usage
                 * This feature is ok when AclPhase = DELAY because the EAGER
                 * will impact our current request response directly.
                 *
                 * But this node should returned all critical data
                 * 1) access, The fields that you could visit
                 * 2) edition, The fields that you could edit
                 * 3) record, The fields of all current record
                 */
                if (Objects.nonNull(acl)) response.attach("acl", acl.acl());
                return Ux.future(response);
            }).otherwise(Ux.otherwise());
        }
    }
}
