package io.vertx.mod.rbac.authorization.extend;

import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.authorization.Amalgam;
import io.vertx.mod.rbac.authorization.Assembler;
import io.vertx.mod.rbac.authorization.ScDetent;
import io.vertx.mod.rbac.logged.ProfileGroup;
import io.vertx.mod.rbac.logged.ProfileRole;
import io.vertx.mod.rbac.logged.ProfileType;

import java.util.List;

/*
 * Group calculation
 * Child
 */
public class GeHorizon implements ScDetent {

    private transient final List<ProfileGroup> original;

    public GeHorizon(final List<ProfileGroup> original) {
        this.original = original;
    }

    @Override
    public JsonObject proc(final List<ProfileRole> profiles) {
        /* Group Search */
        final JsonObject group = new JsonObject();
        final List<ProfileRole> source = Assembler.connect(profiles, this.original);
        Amalgam.logGroup(this.getClass(), profiles);
        /*
         * group = EXTEND_HORIZON, role = UNION
         * No priority of ( group, role )
         *
         * !!!Finished
         */
        Assembler.union(ProfileType.EXTEND_HORIZON_UNION, source).accept(group);
        /*
         * group = EXTEND_HORIZON, role = EAGER
         * No priority of ( group ),  pickup the highest of each group out
         * ( Pick Up the role that group has only one )
         *
         * !!!Finished
         */
        Assembler.union(ProfileType.EXTEND_HORIZON_EAGER, Amalgam.eagerEach(source)).accept(group);
        /*
         * group = EXTEND_HORIZON, role = LAZY
         * No priority of ( group ), pickup the lowest of each group out
         * ( Exclude the role that group has only one )
         *
         * !!!Finished
         */
        Assembler.union(ProfileType.EXTEND_HORIZON_LAZY, Amalgam.lazyEach(source)).accept(group);
        /*
         * group = EXTEND_HORIZON, role = INTERSECT
         * No priority of ( group ), pickup all the role's intersect
         * All group must contain the role or it's no access.
         *
         * !!!Finished
         */
        Assembler.intersect(ProfileType.EXTEND_HORIZON_INTERSECT, source).accept(group);
        return group;
    }
}
