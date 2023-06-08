package io.vertx.mod.rbac.authorization.parent;

import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.authorization.Amalgam;
import io.vertx.mod.rbac.authorization.Assembler;
import io.vertx.mod.rbac.authorization.ScDetent;
import io.vertx.mod.rbac.logged.ProfileRole;
import io.vertx.mod.rbac.logged.ProfileType;

import java.util.List;

/*
 * Group calculation
 * Parent
 */
public class GpHorizon implements ScDetent {

    @Override
    public JsonObject proc(final List<ProfileRole> profiles) {
        /* Group Search */
        final JsonObject group = new JsonObject();
        Amalgam.logGroup(this.getClass(), profiles);
        /*
         * group = PARENT_HORIZON, role = UNION
         * No priority of ( group, role )
         *
         * !!!Finished
         */
        Assembler.union(ProfileType.PARENT_HORIZON_UNION, profiles).accept(group);
        /*
         * group = PARENT_HORIZON, role = EAGER
         * No priority of ( group ),  pickup the highest of each group out
         * ( Pick Up the role that group has only one )
         *
         * !!!Finished
         */
        Assembler.union(ProfileType.PARENT_HORIZON_EAGER, Amalgam.eagerEach(profiles)).accept(group);
        /*
         * group = PARENT_HORIZON, role = LAZY
         * No priority of ( group ), pickup the lowest of each group out
         * ( Exclude the role that group has only one )
         *
         * !!!Finished
         */
        Assembler.union(ProfileType.PARENT_HORIZON_LAZY, Amalgam.lazyEach(profiles)).accept(group);
        /*
         * group = PARENT_HORIZON, role = INTERSECT
         * No priority of ( group ), pickup all the role's intersect
         * All group must contain the role or it's no access.
         *
         * !!!Finished
         */
        Assembler.intersect(ProfileType.PARENT_HORIZON_INTERSECT, profiles).accept(group);
        return group;
    }
}
