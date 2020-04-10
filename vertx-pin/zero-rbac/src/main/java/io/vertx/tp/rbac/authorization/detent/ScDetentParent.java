package io.vertx.tp.rbac.authorization.detent;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.atom.ProfileGroup;
import io.vertx.tp.rbac.atom.ProfileRole;
import io.vertx.tp.rbac.authorization.ScDetent;

import java.util.List;

public class ScDetentParent implements ScDetent {

    private transient final JsonObject input;
    private transient final List<ProfileGroup> original;

    public ScDetentParent(final JsonObject input,
                          final List<ProfileGroup> original) {
        this.input = input;
        this.original = original;
    }

    @Override
    public JsonObject proc(final List<ProfileRole> profiles) {
        final JsonObject parent = new JsonObject();
        /* SeekGroup -> Parent Horizon */
        parent.mergeIn(ScDetent.Group.Parent.horizon().proc(profiles));
        /* SeekGroup -> Parent Critical */
        parent.mergeIn(ScDetent.Group.Parent.critical(this.original).proc(profiles));
        /* SeekGroup -> Parent Priority */
        parent.mergeIn(ScDetent.Group.Parent.overlook(this.original).proc(profiles));
        return this.input.mergeIn(parent);
    }
}
