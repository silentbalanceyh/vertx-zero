package io.vertx.tp.workflow.uca.component;

import cn.vertxup.workflow.domain.tables.daos.WTodoDao;
import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.ConfigTodo;
import io.vertx.tp.workflow.atom.WInstance;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.runtime.ProcessInstance;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractTodo extends AbstractTransfer {

    private transient final KitTodo todoKit;

    public AbstractTodo() {
        super();
        this.todoKit = new KitTodo();
    }

    protected Future<WTodo> todoInsert(final JsonObject params, final ConfigTodo config, final ProcessInstance instance) {
        return this.todoKit.insertAsync(params, config, instance);
    }

    protected Future<WTodo> todoUpdate(final JsonObject params) {
        return this.todoKit.updateAsync(params);
    }

    protected Future<WTodo> todoGenerate(final WTodo todo, final WInstance instance) {
        final WTodo generated = KitTodo.inputNext(todo, instance);
        return Ux.Jooq.on(WTodoDao.class).insertAsync(generated);
    }

    protected ConfigTodo todoConfig(final JsonObject params) {
        final JsonObject request = params.copy();
        request.put(KName.Flow.TODO, this.config.getJsonObject(KName.Flow.TODO, new JsonObject()));
        return new ConfigTodo(request);
    }
}
