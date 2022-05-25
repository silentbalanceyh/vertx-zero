package cn.vertxup.erp.service;

import cn.vertxup.erp.domain.tables.daos.EEmployeeDao;
import cn.vertxup.erp.domain.tables.pojos.EEmployee;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.cv.em.BizInternal;
import io.vertx.tp.optic.business.ExUser;
import io.vertx.tp.optic.environment.Indent;
import io.vertx.tp.optic.feature.Trash;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Set;
import java.util.function.BiFunction;

public class EmployeeService implements EmployeeStub {
    @Override
    public Future<JsonObject> createAsync(final JsonObject data) {
        final EEmployee employee = Ut.deserialize(data, EEmployee.class);
        if (Ut.isNil(employee.getWorkNumber())) {
            return Ux.channelAsync(Indent.class, () -> this.insertAsync(employee, data),
                serial -> serial.indent("NUM.EMPLOYEE", data.getString(KName.SIGMA)).compose(workNum -> {
                    employee.setWorkNumber(workNum);
                    return this.insertAsync(employee, data);
                }));
        } else {
            return this.insertAsync(employee, data);
        }
    }

    private Future<JsonObject> insertAsync(final EEmployee employee, final JsonObject data) {
        return Ux.Jooq.on(EEmployeeDao.class).insertAsync(employee)
            .compose(Ux::futureJ)
            .compose(inserted -> {
                /*
                 * If data contains `userId`, it means that current employee will relate to
                 * an account
                 */
                if (data.containsKey(KName.USER_ID)) {
                    /*
                     * Create new relation here.
                     */
                    final String key = data.getString(KName.USER_ID);
                    return this.updateReference(key, inserted);
                } else {
                    return Ux.future(data);
                }
            });
    }

    @Override
    public Future<JsonObject> fetchAsync(final String key) {
        return Ux.Jooq.on(EEmployeeDao.class).fetchByIdAsync(key)
            .compose(Ux::futureJ)
            .compose(this::fetchRef);
    }

    @Override
    public Future<JsonArray> fetchAsync(final Set<String> keys) {
        return Ux.Jooq.on(EEmployeeDao.class).fetchInAsync(KName.KEY, Ut.toJArray(keys))
            .compose(Ux::futureA)
            .compose(this::fetchRef);
    }

    @Override
    public Future<JsonArray> fetchAsync(final JsonObject condition) {
        return Ux.Jooq.on(EEmployeeDao.class).fetchAsync(condition)
            .compose(Ux::futureA)
            .compose(this::fetchRef);
    }

    @Override
    public Future<JsonObject> updateAsync(final String key, final JsonObject data) {
        return this.fetchAsync(key)
            .compose(Ut.ifNil(JsonObject::new, original -> {
                final String userId = original.getString(KName.USER_ID);
                final String current = data.getString(KName.USER_ID);
                if (Ut.isNil(userId) && Ut.isNil(current)) {
                    /*
                     * Old null, new null
                     * Relation keep
                     */
                    return this.updateEmployee(key, data);
                } else if (Ut.isNil(userId) && Ut.notNil(current)) {
                    /*
                     * Old null, new <value>
                     * Create relation with new
                     */
                    return this.updateEmployee(key, data)
                        .compose(response -> this.updateReference(current, response));
                } else if (Ut.notNil(userId) && Ut.isNil(current)) {
                    /*
                     * Old <value>, new <null>
                     * Clear relation with old
                     */
                    return this.updateEmployee(key, data)
                        .compose(response -> this.updateReference(userId, new JsonObject())
                            .compose(nil -> Ux.future(response))
                        );
                } else {
                    /*
                     * Old <value>, new <value>
                     */
                    if (userId.equals(current)) {
                        /*
                         * Old = New
                         * Relation keep
                         */
                        return this.updateEmployee(key, data);
                    } else {
                        return this.updateEmployee(key, data)
                            /*
                             * Clear first
                             */
                            .compose(response -> this.updateReference(userId, new JsonObject())
                                /*
                                 * Then update
                                 */
                                .compose(nil -> this.updateReference(current, response)));
                    }
                }
            }));
    }

    private Future<JsonObject> updateEmployee(final String key, final JsonObject data) {
        final JsonObject uniques = new JsonObject();
        uniques.put(KName.KEY, key);
        final EEmployee employee = Ut.deserialize(data, EEmployee.class);
        return Ux.Jooq.on(EEmployeeDao.class)
            .upsertAsync(uniques, employee)
            .compose(Ux::futureJ);
    }

    @Override
    public Future<Boolean> deleteAsync(final String key) {
        return this.fetchAsync(key)
            .compose(Ut.ifNil(() -> Boolean.TRUE, item -> Ux.channelAsync(Trash.class,
                () -> this.deleteAsync(key, item),
                tunnel -> tunnel.backupAsync("res.employee", item)
                    .compose(backup -> this.deleteAsync(key, item)))));
    }

    private Future<Boolean> deleteAsync(final String key, final JsonObject item) {
        final String userId = item.getString(KName.USER_ID);
        return this.updateReference(userId, new JsonObject())
            .compose(nil -> Ux.Jooq.on(EEmployeeDao.class)
                .deleteByIdAsync(key));
    }

    private Future<JsonObject> updateReference(final String key, final JsonObject data) {
        return this.switchJ(data, (user, filters) -> user.updateReference(key, filters)
            .compose(Ut.ifJNil(response ->
                Ux.future(data.put(KName.USER_ID, response.getString(KName.KEY))))));
    }

    private Future<JsonObject> fetchRef(final JsonObject input) {
        return this.switchJ(input, ExUser::fetchByReference).compose(Ut.ifJNil(response -> {
            final String userId = response.getString(KName.KEY);
            if (Ut.notNil(userId)) {
                return Ux.future(input.put(KName.USER_ID, userId));
            } else {
                return Ux.future(input);
            }
        }));
    }

    private Future<JsonArray> fetchRef(final JsonArray input) {
        return this.switchA(input, (user, data) -> {
            final Set<String> keys = Ut.valueSetString(data, KName.KEY);
            return user.fetchByReference(keys);
        }).compose(employee -> {
            final JsonArray merged = Ut.elementZip(input, employee, KName.KEY, KName.MODEL_KEY);
            return Ux.future(merged);
        });
    }

    private Future<JsonObject> switchJ(final JsonObject input,
                                       final BiFunction<ExUser, JsonObject, Future<JsonObject>> executor) {
        return Ux.channel(ExUser.class, JsonObject::new, user -> {
            if (Ut.isNil(input)) {
                return Ux.future(new JsonObject());
            } else {
                final JsonObject filters = new JsonObject();
                filters.put(KName.IDENTIFIER, BizInternal.TypeUser.employee.name());
                filters.put(KName.SIGMA, input.getString(KName.SIGMA));
                filters.put(KName.KEY, input.getString(KName.KEY));
                return executor.apply(user, filters);
            }
        });
    }

    private Future<JsonArray> switchA(final JsonArray input,
                                      final BiFunction<ExUser, JsonArray, Future<JsonArray>> executor) {
        return Ux.channel(ExUser.class, JsonArray::new,
            user -> executor.apply(user, input));
    }
}
