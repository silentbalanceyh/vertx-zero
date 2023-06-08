package io.vertx.mod.rbac.authorization.parent;

import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.authorization.Align;
import io.vertx.mod.rbac.authorization.Amalgam;
import io.vertx.mod.rbac.authorization.Assembler;
import io.vertx.mod.rbac.authorization.ScDetent;
import io.vertx.mod.rbac.logged.ProfileGroup;
import io.vertx.mod.rbac.logged.ProfileRole;
import io.vertx.mod.rbac.logged.ProfileType;

import java.util.List;

/*
 * Group calculation
 * Parent
 */
public class GpCritical implements ScDetent {

    private transient final List<ProfileGroup> original;

    public GpCritical(final List<ProfileGroup> original) {
        this.original = original;
    }

    private List<ProfileRole> before(final List<ProfileRole> profiles) {
        /* Find eager group in Critical */
        final ProfileGroup eager = Align.eager(this.original);
        /* Filter by group key */
        final List<ProfileRole> source = Amalgam.parent(profiles, eager);
        /* Then filter by priority */
        return Amalgam.eager(source);
    }

    @Override
    public JsonObject proc(final List<ProfileRole> profiles) {
        /* Group Search */
        final JsonObject group = new JsonObject();
        final List<ProfileRole> source = this.before(profiles);
        Amalgam.logGroup(this.getClass(), source);
        /*
         * group = PARENT_CRITICAL, role = UNION
         * No priority of ( group, role )
         *
         * !!!Finished
         */
        Assembler.union(ProfileType.PARENT_CRITICAL_UNION, source).accept(group);
        /*
         * group = PARENT_CRITICAL, role = EAGER
         * No priority of ( group ),  pickup the highest of each group out
         * ( Pick Up the role that group has only one )
         *
         * !!!Finished
         */
        Assembler.eager(ProfileType.PARENT_CRITICAL_EAGER, source).accept(group);
        /*
         * group = PARENT_CRITICAL, role = LAZY
         * No priority of ( group ), pickup the lowest of each group out
         * ( Exclude the role that group has only one )
         *
         * !!!Finished
         */
        Assembler.lazy(ProfileType.PARENT_CRITICAL_LAZY, source).accept(group);
        /*
         * group = PARENT_CRITICAL, role = INTERSECT
         * No priority of ( group ), pickup all the role's intersect
         * All group must contain the role or it's no access.
         *
         * !!!Finished
         */
        Assembler.intersect(ProfileType.PARENT_CRITICAL_INTERSECT, source).accept(group);
        return group;
    }
}
