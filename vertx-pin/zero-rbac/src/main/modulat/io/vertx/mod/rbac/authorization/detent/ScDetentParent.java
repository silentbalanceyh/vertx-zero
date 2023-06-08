package io.vertx.mod.rbac.authorization.detent;

import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.authorization.ScDetent;
import io.vertx.mod.rbac.logged.ProfileGroup;
import io.vertx.mod.rbac.logged.ProfileRole;

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
