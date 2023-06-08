package io.vertx.mod.workflow.uca.central;

import io.vertx.mod.workflow.atom.configuration.MetaInstance;
import io.vertx.mod.workflow.uca.component.MoveOn;
import io.vertx.mod.workflow.uca.toolkit.ULinkage;
import io.vertx.mod.workflow.uca.toolkit.UTicket;

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
