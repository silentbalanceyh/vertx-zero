package cn.vertxup.ambient.api;

import cn.vertxup.ambient.service.TodoStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;

import javax.inject.Inject;

@Queue
public class TodoActor {
    @Inject
    private transient TodoStub todoStub;

    @Address(Addr.Todo.BY_ID)
    public Future<JsonObject> fetchTodo(final String key) {
        return this.todoStub.fetchTodo(key);
    }
}
