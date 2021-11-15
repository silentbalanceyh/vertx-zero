package io.vertx.tp.workflow.uca.component;

import cn.vertxup.workflow.domain.tables.daos.WTodoDao;
import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.ConfigTodo;
import io.vertx.up.eon.KName;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class TransferStart extends AbstractTodo {
    @Override
    public Future<WTodo> moveAsync(final JsonObject record, final ProcessInstance instance,
                                   final ConfigTodo todo) {
        // Todo Build
        final UxJooq jooq = Ux.Jooq.on(WTodoDao.class);
        return todo.generate(record.getString(KName.KEY)).compose(entity -> {
            // Code Synced with Serial
            if (Objects.isNull(entity.getCode())) {
                entity.setCode(entity.getSerial());
            }
            // Owner is as created todo here.d
            entity.setOwner(entity.getCreatedBy());
            Objects.requireNonNull(entity.getKey());
            return this.traceAsync(entity, instance)
                .compose(jooq::insertAsync);
        });
    }
}
