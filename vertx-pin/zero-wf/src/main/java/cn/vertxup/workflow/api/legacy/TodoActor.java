package cn.vertxup.workflow.api.legacy;

import cn.vertxup.workflow.cv.HighWay;
import cn.vertxup.workflow.service.TodoStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import jakarta.inject.Inject;

@Queue
public class TodoActor {
    @Inject
    private transient TodoStub todoStub;

    @Address(HighWay.Todo.BY_ID)
    public Future<JsonObject> fetchTodo(final String key) {
        return this.todoStub.fetchTodo(key);
    }
}
