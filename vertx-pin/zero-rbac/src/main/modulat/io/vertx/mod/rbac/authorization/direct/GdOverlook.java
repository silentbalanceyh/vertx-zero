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
 * Only one group of low priority
 */
public class GdOverlook implements ScDetent {

    @Override
    public JsonObject proc(final List<ProfileRole> profiles) {
        /* Group Search */
        final JsonObject group = new JsonObject();
        final List<ProfileRole> source = Amalgam.lazy(profiles);
        Amalgam.logGroup(this.getClass(), source);
        /*
         * group = OVERLOOK, role = UNION
         * Low Priority of Group, then role union
         *
         * !!!Finished
         * */
        Assembler.union(ProfileType.OVERLOOK_UNION, source).accept(group);
        /*
         * group = OVERLOOK, role = EAGER
         * Low Priority of Group, then role eager
         *
         * !!!Finished
         */
        Assembler.eager(ProfileType.OVERLOOK_EAGER, source).accept(group);
        /*
         * group = OVERLOOK, role = LAZY
         * Low Priority of Group, then role lazy
         *
         * !!!Finished
         */
        Assembler.lazy(ProfileType.OVERLOOK_LAZY, source).accept(group);
        /*
         * group = OVERLOOK, role = INTERSECT
         * Low Priority of Group, then role intersect
         *
         * !!!Finished
         */
        Assembler.intersect(ProfileType.OVERLOOK_INTERSECT, source).accept(group);
        return group;
    }
}
