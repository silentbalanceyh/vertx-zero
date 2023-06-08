package io.vertx.mod.rbac.authorization.detent;

import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.authorization.ScDetent;
import io.vertx.mod.rbac.logged.ProfileGroup;
import io.vertx.mod.rbac.logged.ProfileRole;

import java.util.List;

public class ScDetentInherit implements ScDetent {

    private transient final JsonObject input;
    private transient final List<ProfileGroup> original;

    public ScDetentInherit(final JsonObject input,
                           final List<ProfileGroup> original) {
        this.input = input;
        this.original = original;
    }

    @Override
    public JsonObject proc(final List<ProfileRole> profiles) {
        final JsonObject inherit = new JsonObject();
        /* SeekGroup = INHERIT_HORIZON */
        inherit.mergeIn(ScDetent.Group.Inherit.horizon(this.original).proc(profiles));
        /* SeekGroup = INHERIT_CRITICAL */
        inherit.mergeIn(ScDetent.Group.Inherit.critical(this.original).proc(profiles));
        /* SeekGroup = INHERIT_OVERLOOK */
        inherit.mergeIn(ScDetent.Group.Inherit.overlook(this.original).proc(profiles));
        return this.input.mergeIn(inherit);
    }
}
