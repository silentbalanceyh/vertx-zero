package io.vertx.mod.rbac.authorization;

import cn.vertxup.rbac.domain.tables.pojos.SGroup;
import cn.vertxup.rbac.service.business.GroupService;
import cn.vertxup.rbac.service.business.GroupStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.cv.AuthKey;
import io.vertx.mod.rbac.logged.ProfileGroup;
import io.vertx.mod.rbac.logged.ProfileRole;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/*
 * Group dynamic searching
 */
public class Align {

    private static final GroupStub STUB = Ut.singleton(GroupService.class);

    public static ProfileGroup eager(final List<ProfileGroup> groups) {
        return groups.stream() /* High priority */
            .min(Comparator.comparing(ProfileGroup::getPriority))
            .orElse(null);
    }

    public static ProfileGroup lazy(final List<ProfileGroup> groups) {
        return groups.stream() /* Low priority */
            .max(Comparator.comparing(ProfileGroup::getPriority))
            .orElse(null);
    }

    /*
     * Search parent ProfileRole based on roles.
     * 1 Level Only ( Find Parent Only )
     */
    public static Future<List<ProfileRole>> flat(final List<ProfileGroup> profiles) {
        return Ux.future(profiles.stream()
            .flatMap(group -> group.getRoles().stream())
            .collect(Collectors.toList()));
    }

    public static Future<List<ProfileRole>> parent(final List<ProfileGroup> profiles) {
        return flat(profiles.stream().map(Align::findParent)
            .filter(Objects::nonNull)
            .collect(Collectors.toList()));
    }

    public static Future<List<ProfileRole>> children(final List<ProfileGroup> profiles) {
        return flat(profiles.stream().map(Align::findChildren)
            .flatMap(List<ProfileGroup>::stream)
            .filter(Objects::nonNull)
            .collect(Collectors.toList()));
    }

    private static List<ProfileGroup> findChildren(final ProfileGroup current) {
        /* Group Id */
        final String groupId = current.getKey();
        final List<SGroup> groups = STUB.fetchChildren(groupId);
        return groups.stream().filter(Objects::nonNull)
            .map(group -> toProfile(group, current))
            .filter(Objects::nonNull)
            /*
             * 「Connect」
             * Modification for result
             */
            .map(children -> children.setReference(current.getKey()))
            .collect(Collectors.toList());
    }

    private static ProfileGroup findParent(final ProfileGroup current) {
        /* Group Id */
        final String groupId = current.getKey();
        /* Group Object */
        final SGroup group = STUB.fetchParent(groupId);
        final ProfileGroup parent = toProfile(group, current);

        /*
         * 「Connect」
         * Modification for input reference
         * */
        if (Objects.nonNull(parent)) {
            current.setReference(parent.getKey());
        }
        return parent;
    }

    private static ProfileGroup toProfile(final SGroup group, final ProfileGroup current) {
        if (null == group) {
            return null;
        } else {
            final JsonObject groupData = new JsonObject();
            groupData.put(AuthKey.F_GROUP_ID, group.getKey());
            groupData.put(AuthKey.PRIORITY, current.getPriority());
            /* GroupId */
            final JsonArray roles = STUB.fetchRoleIds(group.getKey());
            groupData.put("role", roles);
            /* Don't forget to call init() method to set role related permissions. */
            return new ProfileGroup(groupData)
                .init();
        }
    }
}
