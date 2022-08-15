package io.vertx.tp.rbac.atom;

import io.vertx.tp.rbac.cv.em.OwnerType;
import io.vertx.up.atom.secure.Vis;
import io.vertx.up.eon.KValue;

import java.io.Serializable;
import java.util.Objects;

public class ScOwner implements Serializable {
    private final String owner;
    private final OwnerType type;
    private String view = KValue.View.VIEW_DEFAULT;
    private String position = KValue.View.POSITION_DEFAULT;

    public ScOwner(final String owner, final OwnerType type) {
        this.owner = owner;
        this.type = type;
    }

    public ScOwner(final String owner) {
        this(owner, OwnerType.ROLE);
    }

    public ScOwner bind(final Vis vis) {
        if (Objects.nonNull(vis)) {
            this.view = vis.view();
            this.position = vis.position();
        }
        return this;
    }

    public ScOwner bind(final String view, final String position) {
        this.view = view;
        this.position = position;
        return this;
    }

    public OwnerType type() {
        return this.type;
    }

    public String owner() {
        return this.owner;
    }

    public String view() {
        return this.view;
    }

    public String position() {
        return this.position;
    }
}
