package io.vertx.mod.rbac.logged;

import io.horizon.eon.VString;
import io.vertx.mod.rbac.cv.em.SeekGroup;
import io.vertx.mod.rbac.cv.em.SeekRole;

import java.io.Serializable;
import java.util.Objects;

/*
 * Profile Type for role/group seeking
 * 1) SeekRole:  role seeking
 * 2) SeekGroup: group seeking
 */
public class ProfileType implements Serializable {
    /* Role Profile */
    public static ProfileType UNION = new ProfileType(SeekRole.UNION);          // U
    public static ProfileType EAGER = new ProfileType(SeekRole.EAGER);          // E
    public static ProfileType LAZY = new ProfileType(SeekRole.LAZY);            // L
    public static ProfileType INTERSECT = new ProfileType(SeekRole.INTERSECT);  // I

    // ---------- DIRECT Mode -----------
    /* Group : HORIZON ->  Role ( U, E, L, I ) */
    public static ProfileType HORIZON_UNION = new ProfileType(SeekRole.UNION, SeekGroup.HORIZON);
    public static ProfileType HORIZON_EAGER = new ProfileType(SeekRole.EAGER, SeekGroup.HORIZON);
    public static ProfileType HORIZON_LAZY = new ProfileType(SeekRole.LAZY, SeekGroup.HORIZON);
    public static ProfileType HORIZON_INTERSECT = new ProfileType(SeekRole.INTERSECT, SeekGroup.HORIZON);
    /* Group : CRITICAL -> Role ( U, E, L, I ) */
    public static ProfileType CRITICAL_UNION = new ProfileType(SeekRole.UNION, SeekGroup.CRITICAL);
    public static ProfileType CRITICAL_EAGER = new ProfileType(SeekRole.EAGER, SeekGroup.CRITICAL);
    public static ProfileType CRITICAL_LAZY = new ProfileType(SeekRole.LAZY, SeekGroup.CRITICAL);
    public static ProfileType CRITICAL_INTERSECT = new ProfileType(SeekRole.INTERSECT, SeekGroup.CRITICAL);
    /* Group : OVERLOOK -> Role ( U, E, L, I ) */
    public static ProfileType OVERLOOK_UNION = new ProfileType(SeekRole.UNION, SeekGroup.OVERLOOK);
    public static ProfileType OVERLOOK_EAGER = new ProfileType(SeekRole.EAGER, SeekGroup.OVERLOOK);
    public static ProfileType OVERLOOK_LAZY = new ProfileType(SeekRole.LAZY, SeekGroup.OVERLOOK);
    public static ProfileType OVERLOOK_INTERSECT = new ProfileType(SeekRole.INTERSECT, SeekGroup.OVERLOOK);

    // ----------- PARENT Mode -----------
    /* Group : PARENT_HORIZON -> Role ( U, E, L, I ) */
    public static ProfileType PARENT_HORIZON_UNION = new ProfileType(SeekRole.UNION, SeekGroup.PARENT_HORIZON);
    public static ProfileType PARENT_HORIZON_EAGER = new ProfileType(SeekRole.EAGER, SeekGroup.PARENT_HORIZON);
    public static ProfileType PARENT_HORIZON_LAZY = new ProfileType(SeekRole.LAZY, SeekGroup.PARENT_HORIZON);
    public static ProfileType PARENT_HORIZON_INTERSECT = new ProfileType(SeekRole.INTERSECT, SeekGroup.PARENT_HORIZON);
    /* Group : PARENT_CRITICAL -> Role ( U, E, L, I ) */
    public static ProfileType PARENT_CRITICAL_UNION = new ProfileType(SeekRole.UNION, SeekGroup.PARENT_CRITICAL);
    public static ProfileType PARENT_CRITICAL_EAGER = new ProfileType(SeekRole.EAGER, SeekGroup.PARENT_CRITICAL);
    public static ProfileType PARENT_CRITICAL_LAZY = new ProfileType(SeekRole.LAZY, SeekGroup.PARENT_CRITICAL);
    public static ProfileType PARENT_CRITICAL_INTERSECT = new ProfileType(SeekRole.INTERSECT, SeekGroup.PARENT_CRITICAL);
    /* Group : PARENT_OVERLOOK -> Role ( U, E, L, I ) */
    public static ProfileType PARENT_OVERLOOK_UNION = new ProfileType(SeekRole.UNION, SeekGroup.PARENT_OVERLOOK);
    public static ProfileType PARENT_OVERLOOK_EAGER = new ProfileType(SeekRole.EAGER, SeekGroup.PARENT_OVERLOOK);
    public static ProfileType PARENT_OVERLOOK_LAZY = new ProfileType(SeekRole.LAZY, SeekGroup.PARENT_OVERLOOK);
    public static ProfileType PARENT_OVERLOOK_INTERSECT = new ProfileType(SeekRole.INTERSECT, SeekGroup.PARENT_OVERLOOK);
    // ----------- CHILD Mode -----------

    /* Group : CHILD_HORIZON -> Role ( U, E, L, I ) */
    public static ProfileType CHILD_HORIZON_UNION = new ProfileType(SeekRole.UNION, SeekGroup.CHILD_HORIZON);
    public static ProfileType CHILD_HORIZON_EAGER = new ProfileType(SeekRole.EAGER, SeekGroup.CHILD_HORIZON);
    public static ProfileType CHILD_HORIZON_LAZY = new ProfileType(SeekRole.LAZY, SeekGroup.CHILD_HORIZON);
    public static ProfileType CHILD_HORIZON_INTERSECT = new ProfileType(SeekRole.INTERSECT, SeekGroup.CHILD_HORIZON);
    /* Group : CHILD_CRITICAL -> Role ( U, E, L, I ) */
    public static ProfileType CHILD_CRITICAL_UNION = new ProfileType(SeekRole.UNION, SeekGroup.CHILD_CRITICAL);
    public static ProfileType CHILD_CRITICAL_EAGER = new ProfileType(SeekRole.EAGER, SeekGroup.CHILD_CRITICAL);
    public static ProfileType CHILD_CRITICAL_LAZY = new ProfileType(SeekRole.LAZY, SeekGroup.CHILD_CRITICAL);
    public static ProfileType CHILD_CRITICAL_INTERSECT = new ProfileType(SeekRole.INTERSECT, SeekGroup.CHILD_CRITICAL);
    /* Group : CHILD_OVERLOOK -> Role ( U, E, L, I ) */
    public static ProfileType CHILD_OVERLOOK_UNION = new ProfileType(SeekRole.UNION, SeekGroup.CHILD_OVERLOOK);
    public static ProfileType CHILD_OVERLOOK_EAGER = new ProfileType(SeekRole.EAGER, SeekGroup.CHILD_OVERLOOK);
    public static ProfileType CHILD_OVERLOOK_LAZY = new ProfileType(SeekRole.LAZY, SeekGroup.CHILD_OVERLOOK);
    public static ProfileType CHILD_OVERLOOK_INTERSECT = new ProfileType(SeekRole.INTERSECT, SeekGroup.CHILD_OVERLOOK);

    // ------------ INHERIT Mode ---------

    /* Group : INHERIT_HORIZON -> Role ( U, E, L, I ) */
    public static ProfileType INHERIT_HORIZON_UNION = new ProfileType(SeekRole.UNION, SeekGroup.INHERIT_HORIZON);
    public static ProfileType INHERIT_HORIZON_EAGER = new ProfileType(SeekRole.EAGER, SeekGroup.INHERIT_HORIZON);
    public static ProfileType INHERIT_HORIZON_LAZY = new ProfileType(SeekRole.LAZY, SeekGroup.INHERIT_HORIZON);
    public static ProfileType INHERIT_HORIZON_INTERSECT = new ProfileType(SeekRole.INTERSECT, SeekGroup.INHERIT_HORIZON);
    /* Group : INHERIT_CRITICAL -> Role ( U, E, L, I ) */
    public static ProfileType INHERIT_CRITICAL_UNION = new ProfileType(SeekRole.UNION, SeekGroup.INHERIT_CRITICAL);
    public static ProfileType INHERIT_CRITICAL_EAGER = new ProfileType(SeekRole.EAGER, SeekGroup.INHERIT_CRITICAL);
    public static ProfileType INHERIT_CRITICAL_LAZY = new ProfileType(SeekRole.LAZY, SeekGroup.INHERIT_CRITICAL);
    public static ProfileType INHERIT_CRITICAL_INTERSECT = new ProfileType(SeekRole.INTERSECT, SeekGroup.INHERIT_CRITICAL);
    /* Group : INHERIT_OVERLOOK -> Role ( U, E, L, I ) */
    public static ProfileType INHERIT_OVERLOOK_UNION = new ProfileType(SeekRole.UNION, SeekGroup.INHERIT_OVERLOOK);
    public static ProfileType INHERIT_OVERLOOK_EAGER = new ProfileType(SeekRole.EAGER, SeekGroup.INHERIT_OVERLOOK);
    public static ProfileType INHERIT_OVERLOOK_LAZY = new ProfileType(SeekRole.LAZY, SeekGroup.INHERIT_OVERLOOK);
    public static ProfileType INHERIT_OVERLOOK_INTERSECT = new ProfileType(SeekRole.INTERSECT, SeekGroup.INHERIT_OVERLOOK);
    // ------------ EXTEND Mode ----------

    /* Group : EXTEND_HORIZON -> Role ( U, E, L, I ) */
    public static ProfileType EXTEND_HORIZON_UNION = new ProfileType(SeekRole.UNION, SeekGroup.EXTEND_HORIZON);
    public static ProfileType EXTEND_HORIZON_EAGER = new ProfileType(SeekRole.EAGER, SeekGroup.EXTEND_HORIZON);
    public static ProfileType EXTEND_HORIZON_LAZY = new ProfileType(SeekRole.LAZY, SeekGroup.EXTEND_HORIZON);
    public static ProfileType EXTEND_HORIZON_INTERSECT = new ProfileType(SeekRole.INTERSECT, SeekGroup.EXTEND_HORIZON);
    /* Group : EXTEND_CRITICAL -> Role ( U, E, L, I ) */
    public static ProfileType EXTEND_CRITICAL_UNION = new ProfileType(SeekRole.UNION, SeekGroup.EXTEND_CRITICAL);
    public static ProfileType EXTEND_CRITICAL_EAGER = new ProfileType(SeekRole.EAGER, SeekGroup.EXTEND_CRITICAL);
    public static ProfileType EXTEND_CRITICAL_LAZY = new ProfileType(SeekRole.LAZY, SeekGroup.EXTEND_CRITICAL);
    public static ProfileType EXTEND_CRITICAL_INTERSECT = new ProfileType(SeekRole.INTERSECT, SeekGroup.EXTEND_CRITICAL);
    /* Group : EXTEND_OVERLOOK -> Role ( U, E, L, I ) */
    public static ProfileType EXTEND_OVERLOOK_UNION = new ProfileType(SeekRole.UNION, SeekGroup.EXTEND_OVERLOOK);
    public static ProfileType EXTEND_OVERLOOK_EAGER = new ProfileType(SeekRole.EAGER, SeekGroup.EXTEND_OVERLOOK);
    public static ProfileType EXTEND_OVERLOOK_LAZY = new ProfileType(SeekRole.LAZY, SeekGroup.EXTEND_OVERLOOK);
    public static ProfileType EXTEND_OVERLOOK_INTERSECT = new ProfileType(SeekRole.INTERSECT, SeekGroup.EXTEND_OVERLOOK);

    /* Private Variable */
    private final SeekRole role;
    private final SeekGroup group;

    private ProfileType(final SeekRole role) {
        this(role, null);
    }

    private ProfileType(final SeekRole role, final SeekGroup group) {
        this.role = role;
        this.group = group;
    }

    public String getKey() {
        /* Group,User - Role */
        if (null == this.group) {
            return "USER" + VString.UNDERLINE + this.role.name();
        } else {
            return this.group.name() + VString.UNDERLINE + this.role.name();
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfileType)) {
            return false;
        }
        final ProfileType that = (ProfileType) o;
        return this.role == that.role &&
            this.group == that.group;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.role, this.group);
    }
}