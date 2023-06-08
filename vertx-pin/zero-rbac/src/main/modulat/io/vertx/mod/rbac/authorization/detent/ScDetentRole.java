package io.vertx.mod.rbac.authorization.detent;

import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.authorization.Assembler;
import io.vertx.mod.rbac.authorization.ScDetent;
import io.vertx.mod.rbac.logged.ProfileRole;
import io.vertx.mod.rbac.logged.ProfileType;

import java.util.List;

public class ScDetentRole implements ScDetent {

    private transient final JsonObject input;

    public ScDetentRole(final JsonObject input) {
        this.input = input;
    }

    @Override
    public JsonObject proc(final List<ProfileRole> profile) {
        final JsonObject data = new JsonObject();
        /*
         * role = UNION
         *
         * !!!Finished
         * */
        Assembler.union(ProfileType.UNION, profile).accept(data);
        /*
         * role = INTERSECT
         *
         * !!!Finished
         * */
        Assembler.intersect(ProfileType.INTERSECT, profile).accept(data);
        /*
         * role = EAGER
         *
         * !!!Finished
         * */
        Assembler.eager(ProfileType.EAGER, profile).accept(data);
        /*
         * role = LAZY
         *
         * !!!Finished
         * */
        Assembler.lazy(ProfileType.LAZY, profile).accept(data);

        return this.input.mergeIn(data);
    }
}
