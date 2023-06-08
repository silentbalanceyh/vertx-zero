package io.vertx.mod.rbac.authorization;

import io.horizon.uca.log.Annal;
import io.vertx.mod.rbac.logged.ProfileGroup;
import io.vertx.mod.rbac.logged.ProfileRole;
import io.vertx.up.util.Ut;

import java.util.*;
import java.util.stream.Collectors;

import static io.vertx.mod.rbac.refine.Sc.LOG;

/*
 * Role Calculation
 * -- The calculation will based on group parameter.
 */
public class Amalgam {

    public static void logGroup(final Class<?> clazz,
                                final List<ProfileRole> roles) {
        final Annal LOGGER = Annal.get(clazz);
        LOG.Auth.debug(LOGGER, "Group Selected: {0}, Size: {1}",
            Ut.fromJoin(getGroups(roles)), String.valueOf(roles.size()));
    }

    public static List<ProfileRole> parent(final List<ProfileRole> roles,
                                           final ProfileGroup group) {
        /* Do not modify input roles */
        return new ArrayList<>(roles).stream().filter(role -> Objects.nonNull(role.getGroup()))
            .filter(role -> group.getReference().equals(role.getGroup().getKey()))
            .collect(Collectors.toList());
    }

    public static List<ProfileRole> children(final List<ProfileRole> roles,
                                             final ProfileGroup group) {
        /* Do not modify input roles */
        return new ArrayList<>(roles).stream().filter(role -> Objects.nonNull(role.getGroup()))
            .filter(role -> group.getKey().equals(role.getGroup().getReference()))
            .collect(Collectors.toList());
    }

    /*
     * Search eager of each group, each group should has only one,
     * Searched ProfileRole size = group size
     */
    public static List<ProfileRole> eagerEach(final List<ProfileRole> roles) {
        /* Find groups */
        return getGroups(roles).stream().map(group -> roles.stream()
                .filter(role -> group.equals(role.getGroup().getKey()))
                /* Pickup high priority of group */
                .min(Comparator.comparing(ProfileRole::getPriority))
                .orElse(null))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    public static List<ProfileRole> lazyEach(final List<ProfileRole> roles) {
        return getGroups(roles).stream().map(group -> roles.stream()
                .filter(role -> group.equals(role.getGroup().getKey()))
                /* Pickup low priority of group */
                .max(Comparator.comparing(ProfileRole::getPriority))
                .orElse(null))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    public static List<ProfileRole> eager(final List<ProfileRole> roles) {
        final Integer groupPriority = findGroupPriority(roles, true);
        return roles.stream()
            .filter(role -> groupPriority.equals(role.getGroup().getPriority()))
            .collect(Collectors.toList());
    }

    public static List<ProfileRole> lazy(final List<ProfileRole> roles) {
        final Integer groupPriority = findGroupPriority(roles, false);
        return roles.stream()
            .filter(role -> groupPriority.equals(role.getGroup().getPriority()))
            .collect(Collectors.toList());
    }

    /*
     * Find Group by priority
     */
    private static Integer findGroupPriority(final List<ProfileRole> roles, final boolean isHigh) {
        return isHigh ?
            roles.stream().map(ProfileRole::getGroup)
                .min(Comparator.comparing(ProfileGroup::getPriority))
                .map(ProfileGroup::getPriority)
                .orElse(0) :
            roles.stream().map(ProfileRole::getGroup)
                .max(Comparator.comparing(ProfileGroup::getPriority))
                .map(ProfileGroup::getPriority)
                .orElse(Integer.MAX_VALUE);
    }

    private static Set<String> getGroups(final List<ProfileRole> roles) {
        return roles.stream()
            .map(ProfileRole::getGroup)
            .map(ProfileGroup::getKey)
            .collect(Collectors.toSet());
    }
}
