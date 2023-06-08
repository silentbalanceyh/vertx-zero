package io.vertx.mod.rbac.authorization.detent;

import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.authorization.ScDetent;
import io.vertx.mod.rbac.logged.ProfileGroup;
import io.vertx.mod.rbac.logged.ProfileRole;

import java.util.List;

public class ScDetentChild implements ScDetent {

    private transient final JsonObject input;
    private transient final List<ProfileGroup> original;

    public ScDetentChild(final JsonObject input,
                         final List<ProfileGroup> original) {
        this.input = input;
        this.original = original;
    }

    @Override
    public JsonObject proc(final List<ProfileRole> profiles) {
        final JsonObject children = new JsonObject();
        /* SeekGroup -> Parent Horizon */

        children.mergeIn(ScDetent.Group.Child.horizon().proc(profiles));
        /* SeekGroup -> Parent Critical */
        children.mergeIn(ScDetent.Group.Child.critical(this.original).proc(profiles));
        /* SeekGroup -> Parent Priority */
        children.mergeIn(ScDetent.Group.Child.overlook(this.original).proc(profiles));
        return this.input.mergeIn(children);
    }
}
