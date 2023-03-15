package io.vertx.tp.workflow.uca.central;

import io.vertx.tp.workflow.atom.configuration.MetaInstance;
import io.vertx.tp.workflow.uca.component.MoveOn;
import io.vertx.tp.workflow.uca.toolkit.ULinkage;
import io.vertx.tp.workflow.uca.toolkit.UTicket;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractMoveOn extends BehaviourStandard implements MoveOn {
    protected transient UTicket todoKit;
    protected transient ULinkage linkageKit;

    @Override
    public Behaviour bind(final MetaInstance metadata) {
        Objects.requireNonNull(metadata);
        this.todoKit = new UTicket(metadata);
        this.linkageKit = new ULinkage(metadata);
        return super.bind(metadata);
    }
}
