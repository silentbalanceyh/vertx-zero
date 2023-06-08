package cn.vertxup.erp.api;

import cn.vertxup.erp.service.EmployeeStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.erp.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import jakarta.inject.Inject;

@Queue
public class EmployeeActor {

    @Inject
    private transient EmployeeStub stub;

    @Address(Addr.Employee.ADD)
    public Future<JsonObject> create(final JsonObject body) {
        return this.stub.createAsync(body);
    }

    @Address(Addr.Employee.EDIT)
    public Future<JsonObject> update(final String key, final JsonObject params) {
        return this.stub.updateAsync(key, params);
    }

    @Address(Addr.Employee.BY_ID)
    public Future<JsonObject> fetchById(final String key) {
        return this.stub.fetchAsync(key);
    }

    @Address(Addr.Employee.DELETE)
    public Future<Boolean> remove(final String key) {
        return this.stub.deleteAsync(key);
    }
}
