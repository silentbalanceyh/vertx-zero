package io.vertx.mod.rbac.authorization.detent;

import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.authorization.ScDetent;
import io.vertx.mod.rbac.logged.ProfileGroup;
import io.vertx.mod.rbac.logged.ProfileRole;

import java.util.List;

public class ScDetentExtend implements ScDetent {

    private transient final JsonObject input;
    private transient final List<ProfileGroup> original;

    public ScDetentExtend(final JsonObject input,
                          final List<ProfileGroup> original) {
        this.input = input;
        this.original = original;
    }

    @Override
    public JsonObject proc(final List<ProfileRole> profiles) {
        final JsonObject extend = new JsonObject();
        /* SeekGroup = EXTEND_HORIZON */
        extend.mergeIn(ScDetent.Group.Extend.horizon(this.original).proc(profiles));
        /* SeekGroup = EXTEND_CRITICAL */
        extend.mergeIn(ScDetent.Group.Extend.critical(this.original).proc(profiles));
        /* SeekGroup = EXTEND_OVERLOOK */
        extend.mergeIn(ScDetent.Group.Extend.overlook(this.original).proc(profiles));
        return this.input.mergeIn(extend);
    }
}
