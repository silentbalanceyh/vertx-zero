package io.vertx.tp.workflow.uca.central;

import io.vertx.tp.workflow.atom.configuration.MetaInstance;
import io.vertx.tp.workflow.uca.component.MoveOn;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractMoveOn extends BehaviourStandard implements MoveOn {
    protected transient AidTodo todoKit;
    protected transient AidLinkage linkageKit;

    @Override
    public Behaviour bind(final MetaInstance metadata) {
        Objects.requireNonNull(metadata);
        this.todoKit = new AidTodo(metadata);
        this.linkageKit = new AidLinkage(metadata);
        return super.bind(metadata);
    }
}
