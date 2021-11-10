package cn.zeroup.macrocosm.api.legacy;

import cn.zeroup.macrocosm.cv.HighWay;
import cn.zeroup.macrocosm.service.TodoStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;

import javax.inject.Inject;

@Queue
public class TodoActor {
    @Inject
    private transient TodoStub todoStub;

    @Address(HighWay.Todo.BY_ID)
    public Future<JsonObject> fetchTodo(final String key) {
        return this.todoStub.fetchTodo(key);
    }
}
