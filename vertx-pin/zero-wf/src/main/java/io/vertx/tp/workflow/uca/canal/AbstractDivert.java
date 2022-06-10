package io.vertx.tp.workflow.uca.canal;

import io.vertx.tp.workflow.atom.MetaInstance;
import io.vertx.tp.workflow.atom.WMove;
import io.vertx.tp.workflow.uca.component.Divert;

import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractDivert extends BehaviourStandard implements Divert {
    protected transient AidTodo todoKit;
    protected transient AidLinkage linkageKit;

    @Override
    public Divert bind(final ConcurrentMap<String, WMove> moveMap) {
        super.rules(moveMap);
        return this;
    }

    @Override
    public Behaviour bind(final MetaInstance metadata) {
        Objects.requireNonNull(metadata);
        this.todoKit = new AidTodo(metadata);
        this.linkageKit = new AidLinkage(metadata);
        return super.bind(metadata);
    }
}
