package io.vertx.mod.rbac.logged;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.atom.ScConfig;
import io.vertx.mod.rbac.cv.AuthKey;
import io.vertx.mod.rbac.init.ScPin;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/*
 * Single authority for role -> permissions
 * 1) priority
 * 2) permissions
 */
public class ProfileRole implements Serializable {

    private static final ScConfig CONFIG = ScPin.getConfig();
    private transient final Integer priority;
    private final transient ScRole role;
    /* GroupId Process */
    private transient ProfileGroup reference;

    public ProfileRole(final JsonObject data) {
        /* Role Id */
        final String roleId = data.getString(AuthKey.F_ROLE_ID);
        this.role = ScRole.login(roleId);
        /* Priority */
        this.priority = data.getInteger(AuthKey.PRIORITY);
    }

    Future<ProfileRole> initAsync() {
        /* Fetch permission */
        final boolean isSecondary = CONFIG.getSupportSecondary();
        return isSecondary ?
            /* Enabled secondary permission */
            this.role.fetchWithCache().compose(ids -> Future.succeededFuture(this)) :
            /* No secondary */
            this.role.fetch().compose(ids -> Future.succeededFuture(this));
    }

    public ProfileRole init() {
        /* Fetch permission ( Without Cache in Sync mode ) */
        this.role.refresh();
        // Sc.infoAuth(LOGGER, "Extract Permissions: {0}", permissions.encode());
        return this;
    }

    public Integer getPriority() {
        return this.priority;
    }

    public String getKey() {
        return this.role.key();
    }

    public Set<String> getAuthorities() {
        return this.role.authorities();
    }

    /*
     * For uniform processing
     */
    public ProfileGroup getGroup() {
        return this.reference;
    }

    public ProfileRole setGroup(final ProfileGroup reference) {
        this.reference = reference;
        return this;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ProfileRole that = (ProfileRole) o;
        return this.role.equals(that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.role);
    }

    @Override
    public String toString() {
        return "ProfileRole{" +
            "priority=" + this.priority +
            ", role=" + this.role +
            ", reference=" + this.reference +
            '}';
    }
}
