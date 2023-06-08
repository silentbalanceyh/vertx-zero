package io.vertx.mod.rbac.authorization;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.cv.AuthKey;
import io.vertx.mod.rbac.logged.ProfileGroup;
import io.vertx.mod.rbac.logged.ProfileRole;
import io.vertx.mod.rbac.logged.ProfileType;
import io.vertx.up.util.Ut;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
 * Role Calculation
 * -- No filter for ProfileRole calculation, all the calculation will apply to
 * all input profiles.
 */
public class Assembler {

    public static List<ProfileRole> connect(final List<ProfileRole> profiles, final List<ProfileGroup> original) {
        final Set<ProfileRole> originalSet = original
            .stream().flatMap(group -> group.getRoles().stream())
            .collect(Collectors.toSet());
        originalSet.addAll(profiles);
        return new ArrayList<>(originalSet);
    }

    public static List<ProfileRole> connect(final List<ProfileRole> profiles, final ProfileGroup original) {
        final List<ProfileRole> eagerRoles = Objects.isNull(original) ? new ArrayList<>() : original.getRoles();
        final Set<ProfileRole> originalSet = new HashSet<>(eagerRoles);
        originalSet.addAll(profiles);
        return new ArrayList<>(originalSet);
    }

    public static Consumer<JsonObject> union(final ProfileType type, final List<ProfileRole> profiles) {
        return bind(type, profiles, Ut::elementUnion);
    }

    public static Consumer<JsonObject> intersect(final ProfileType type, final List<ProfileRole> profiles) {
        return bind(type, profiles, Ut::elementIntersect);
    }

    public static Consumer<JsonObject> eager(final ProfileType type, final List<ProfileRole> profiles) {
        return bind(type, profiles, true);
    }

    public static Consumer<JsonObject> lazy(final ProfileType type, final List<ProfileRole> profiles) {
        return bind(type, profiles, false);
    }

    private static Consumer<JsonObject> bind(final ProfileType type,
                                             final List<ProfileRole> profiles,
                                             final BinaryOperator<Set<String>> fnReduce) {
        return input -> {
            if (Objects.nonNull(input) && !profiles.isEmpty()) {
                /*
                 * For default permissions
                 * When the profile size is 1, it means that the default permission should be
                 * the only one role permissions, in this situation, the authorities set should
                 * not be new HashSet<>() here, but first.getAuthorities() instead.
                 * Refer below line:  DEFAULT AUTHORITIES
                 */
                final ProfileRole first = profiles.iterator().next();
                /* 1. permissions = [] */
                /* 2. roles = [] */
                final JsonArray roles = new JsonArray();
                final JsonArray permissions = Ut.toJArray(profiles.stream()
                    .filter(Objects::nonNull)
                    .map(bindRole(roles))
                    .map(ProfileRole::getAuthorities)
                    /* DEFAULT AUTHORITIES */
                    .reduce(first.getAuthorities(), fnReduce));

                input.put(type.getKey(), bindResult(permissions, roles));
            } else {
                input.put(type.getKey(), bindResult(null, null));
            }
        };
    }

    private static JsonObject bindResult(final JsonArray permissions,
                                         final JsonArray roles) {
        final JsonObject profile = new JsonObject();
        profile.put(AuthKey.PROFILE_PERM, Objects.isNull(permissions) ? new JsonArray() : permissions);
        profile.put(AuthKey.PROFILE_ROLE, Objects.isNull(roles) ? new JsonArray() : roles);
        return profile;
    }

    private static Function<ProfileRole, ProfileRole> bindRole(final JsonArray roles) {
        return profile -> {
            roles.add(profile.getKey());
            return profile;
        };
    }

    private static Consumer<JsonObject> bind(final ProfileType type,
                                             final List<ProfileRole> profiles,
                                             final boolean highPriority) {
        return input -> {
            if (Objects.nonNull(input) && !profiles.isEmpty()) {
                /* 1. permissions = [] */
                final JsonArray permissions;
                /* 2. roles = [] */
                final JsonArray roles = new JsonArray();
                if (highPriority) {
                    permissions = Ut.toJArray(profiles.stream()
                        .min(Comparator.comparing(ProfileRole::getPriority))
                        .map(bindRole(roles))
                        .map(ProfileRole::getAuthorities)
                        .orElse(new HashSet<>()));
                } else {
                    permissions = Ut.toJArray(profiles.stream()
                        .max(Comparator.comparing(ProfileRole::getPriority))
                        .map(bindRole(roles))
                        .map(ProfileRole::getAuthorities)
                        .orElse(new HashSet<>()));
                }
                input.put(type.getKey(), bindResult(permissions, roles));
            } else {
                input.put(type.getKey(), bindResult(null, null));
            }
        };
    }
}
