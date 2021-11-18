package io.vertx.tp.workflow.uca.component;

import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.ConfigTodo;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractTodo extends AbstractTransfer implements Transfer {

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

    protected Future<WTodo> todoGenerate(final WTodo todo, final Task task) {
        return this.todoKit.nextAsync(todo, task);
    }
}
