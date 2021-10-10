package cn.vertxup.rbac.service.business;

import cn.vertxup.rbac.domain.tables.pojos.SUser;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.business.ExEmployee;
import io.vertx.tp.rbac.cv.AuthMsg;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.atom.unity.UObject;
import io.vertx.up.eon.KName;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;

import java.util.Objects;
import java.util.function.Function;

class UserHelper {

    private static final Annal LOGGER = Annal.get(UserHelper.class);

    static Future<JsonObject> fetchEmployee(final SUser user) {
        /* User model key */
        return applyTunnel(user, executor ->
            /* Read employee information */
            executor.fetchAsync(user.getModelKey()));
    }

    static Future<JsonObject> updateEmployee(final SUser user, final JsonObject params) {
        /* User model key */
        return applyTunnel(user, executor ->
            /* Update employee information */
            executor.updateAsync(params.getString("employeeId"), params));
    }

    private static Future<JsonObject> applyUser(final SUser user) {
        return Ux.future(user).compose(Ux::futureJ);
    }

    private static Future<JsonObject> applyTunnel(final SUser user, final Function<ExEmployee, Future<JsonObject>> fnTunnel) {
        if (Objects.nonNull(user)) {
            if (Objects.nonNull(user.getModelKey())) {
                return Ke.channelAsync(ExEmployee.class, () -> {
                    /*
                     * Here branch means that actual definition is conflict with your expected.
                     * You forget to define executor of `EcEmployee`.
                     */
                    Sc.infoAuth(LOGGER, AuthMsg.EMPLOYEE_EMPTY + " Executor");
                    return applyUser(user);
                }, executor -> {
                    /*
                     * Simple situation for user information get
                     * 1) EcEmployee `only` in standard situation
                     * 2) Zero extension provide employee information get only
                     * 3) You can write another complex `EcEmployee` implementation class
                     *    to extend related information read.
                     */
                    Sc.infoAuth(LOGGER, AuthMsg.EMPLOYEE_BY_USER, user.getModelKey());
                    return fnTunnel.apply(executor)
                        /* Employee information */
                        .compose(employee -> Objects.isNull(employee) ?
                            Ux.future(new JsonObject()) :
                            Ux.future(employee))
                        /* Merged */
                        .compose(employee -> UObject
                            .create(Ux.toJson(user).copy())
                            .append(employee)
                            /* Model Key -> Employee Id */
                            .convert(KName.MODEL_KEY, "employeeId")
                            .toFuture()
                        );
                });
            } else {
                /*
                -* There are two fields in S_USER table: MODEL_ID & MODEL_KEY
                 * This branch means that MODEL_KEY is null, you could not call `EcEmployee`
                 * for continue information extract
                 */
                Sc.infoAuth(LOGGER, AuthMsg.EMPLOYEE_EMPTY + " Model Key");
                return applyUser(user);
            }
        } else {
            /*
             * Input SUser object is null, could not find X_USER record
             * in your database
             */
            Sc.infoAuth(LOGGER, AuthMsg.EMPLOYEE_EMPTY + " Null");
            return Ux.future(new JsonObject());
        }
    }
}
