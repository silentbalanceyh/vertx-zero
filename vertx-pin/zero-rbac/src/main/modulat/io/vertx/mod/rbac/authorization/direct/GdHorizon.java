package io.vertx.mod.rbac.authorization.direct;

import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.authorization.Amalgam;
import io.vertx.mod.rbac.authorization.Assembler;
import io.vertx.mod.rbac.authorization.ScDetent;
import io.vertx.mod.rbac.logged.ProfileRole;
import io.vertx.mod.rbac.logged.ProfileType;

import java.util.List;

/*
 * Group calculation
 * Ignore priority of group
 */
public class GdHorizon implements ScDetent {

    @Override
    public JsonObject proc(final List<ProfileRole> profiles) {
        /* Group Search */
        final JsonObject group = new JsonObject();
        Amalgam.logGroup(this.getClass(), profiles);
        /*
         * group = HORIZON, role = UNION
         * No priority of ( group, role )
         *
         * !!!Finished
         * */
        Assembler.union(ProfileType.HORIZON_UNION, profiles).accept(group);
        /*
         * group = HORIZON, role = EAGER
         * No priority of ( group ),  pickup the highest of each group out
         * ( Pick Up the role that group has only one )
         *
         * !!!Finished
         */
        Assembler.union(ProfileType.HORIZON_EAGER, Amalgam.eagerEach(profiles)).accept(group);
        /*
         * group = HORIZON, role = LAZY
         * No priority of ( group ), pickup the lowest of each group out
         * ( Exclude the role that group has only one )
         *
         * !!!Finished
         */
        Assembler.union(ProfileType.HORIZON_LAZY, Amalgam.lazyEach(profiles)).accept(group);
        /*
         * group = HORIZON, role = INTERSECT
         * No priority of ( group ), pickup all the role's intersect
         * All group must contain the role or it's no access.
         *
         * !!!Finished
         */
        Assembler.intersect(ProfileType.HORIZON_INTERSECT, profiles).accept(group);
        return group;
    }
}
