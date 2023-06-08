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
 * Only one group of high priority
 *
 */
public class GdCritical implements ScDetent {

    @Override
    public JsonObject proc(final List<ProfileRole> profiles) {
        /* Group Search */
        final JsonObject group = new JsonObject();
        final List<ProfileRole> source = Amalgam.eager(profiles);
        Amalgam.logGroup(this.getClass(), source);
        /*
         * group = CRITICAL, role = UNION
         * High Priority of Group, then role union
         *
         * !!!Finished
         * */
        Assembler.union(ProfileType.CRITICAL_UNION, source).accept(group);
        /*
         * group = CRITICAL, role = EAGER
         * High Priority of Group, then role eager
         *
         * !!!Finished
         */
        Assembler.eager(ProfileType.CRITICAL_EAGER, source).accept(group);
        /*
         * group = CRITICAL, role = LAZY
         * High Priority of Group, then role lazy
         *
         * !!!Finished
         */
        Assembler.lazy(ProfileType.CRITICAL_LAZY, source).accept(group);
        /*
         * group = CRITICAL, role = INTERSECT
         * High Priority of Group, then role intersect
         *
         * !!!Finished
         */
        Assembler.intersect(ProfileType.CRITICAL_INTERSECT, source).accept(group);
        return group;
    }
}
