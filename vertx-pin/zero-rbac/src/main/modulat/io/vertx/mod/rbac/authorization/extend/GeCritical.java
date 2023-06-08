package io.vertx.mod.rbac.authorization.extend;

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
 * Child
 */
public class GeCritical implements ScDetent {

    private transient final List<ProfileGroup> original;

    public GeCritical(final List<ProfileGroup> original) {
        this.original = original;
    }

    private List<ProfileRole> before(final List<ProfileRole> profiles) {
        /* Find eager group in Critical */
        final ProfileGroup eager = Align.eager(this.original);
        /* Filter by group key */
        final List<ProfileRole> source = Amalgam.children(profiles, eager);
        /* Then filter by priority */
        final List<ProfileRole> processed = Amalgam.eager(source);

        return Assembler.connect(processed, eager);
    }

    @Override
    public JsonObject proc(final List<ProfileRole> profiles) {
        /* Group Search */
        final JsonObject group = new JsonObject();
        final List<ProfileRole> source = this.before(profiles);
        Amalgam.logGroup(this.getClass(), source);
        /*
         * group = EXTEND_CRITICAL, role = UNION
         * No priority of ( group, role )
         *
         * !!!Finished
         */
        Assembler.union(ProfileType.EXTEND_CRITICAL_UNION, source).accept(group);
        /*
         * group = EXTEND_CRITICAL, role = EAGER
         * No priority of ( group ),  pickup the highest of each group out
         * ( Pick Up the role that group has only one )
         *
         * !!!Finished
         */
        Assembler.eager(ProfileType.EXTEND_CRITICAL_EAGER, source).accept(group);
        /*
         * group = EXTEND_CRITICAL, role = LAZY
         * No priority of ( group ), pickup the lowest of each group out
         * ( Exclude the role that group has only one )
         *
         * !!!Finished
         */
        Assembler.lazy(ProfileType.EXTEND_CRITICAL_LAZY, source).accept(group);
        /*
         * group = EXTEND_CRITICAL, role = INTERSECT
         * No priority of ( group ), pickup all the role's intersect
         * All group must contain the role or it's no access.
         *
         * !!!Finished
         */
        Assembler.intersect(ProfileType.EXTEND_CRITICAL_INTERSECT, source).accept(group);
        return group;
    }
}
