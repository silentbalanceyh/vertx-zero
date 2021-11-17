package io.vertx.tp.workflow.uca.component;

import cn.vertxup.workflow.domain.tables.daos.WTodoDao;
import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.ConfigTodo;
import io.vertx.up.atom.Refer;
import io.vertx.up.eon.KName;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.runtime.ProcessInstance;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class TransferSave extends AbstractTransfer implements Transfer {
    @Override
    public Future<WTodo> moveAsync(final JsonObject params, final ProcessInstance instance) {
        // Todo Updating
        final UxJooq jooq = Ux.Jooq.on(WTodoDao.class);
        final String key = params.getString(KName.KEY);
        final Refer refer = new Refer();
        return jooq.<WTodo>fetchByIdAsync(key).compose(query -> {
            final WTodo updated = Ux.combineT(query, params);
            return jooq.updateAsync(updated);
        }).compose(refer::future).compose(todo -> {
            final ConfigTodo configTodo = new ConfigTodo(todo);
            return this.updateAsync(params, configTodo);
        }).compose(nil -> Ux.future(refer.get()));
    }
}
