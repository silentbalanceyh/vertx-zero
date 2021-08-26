package io.vertx.tp.rbac.atom;

import cn.vertxup.rbac.domain.tables.daos.RRolePermDao;
import cn.vertxup.rbac.domain.tables.pojos.RRolePerm;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.cv.AuthKey;
import io.vertx.tp.rbac.init.ScPin;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.unity.Ux;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/*
 * Single authority for role -> permissions
 * 1) priority
 * 2) permissions
 */
public class ProfileRole implements Serializable {

    private static final ScConfig CONFIG = ScPin.getConfig();
    private transient final String roleId;
    private transient final Integer priority;
    private transient final Set<String> authorities = new HashSet<>();
    /* GroupId Process */
    private transient ProfileGroup reference;

    public ProfileRole(final JsonObject data) {
        /* Role Id */
        this.roleId = data.getString(AuthKey.F_ROLE_ID);
        /* Priority */
        this.priority = data.getInteger(AuthKey.PRIORITY);
    }

    Future<ProfileRole> initAsync() {
        /* Fetch permission */
        final boolean isSecondary = CONFIG.getSupportSecondary();
        return isSecondary ?
            /* Enabled secondary permission */
            this.fetchAuthoritiesAsyncWithCache().compose(ids -> Future.succeededFuture(this)) :
            /* No secondary */
            this.fetchAuthoritiesAsync().compose(ids -> Future.succeededFuture(this));
    }

    public ProfileRole init() {
        /* Fetch permission ( Without Cache in Sync mode ) */
        this.refreshAuthorities(Ux.Jooq.on(RRolePermDao.class)
            .fetch(AuthKey.F_ROLE_ID, this.roleId));
        // Sc.infoAuth(LOGGER, "Extract Permissions: {0}", permissions.encode());
        return this;
    }

    public Integer getPriority() {
        return this.priority;
    }

    public String getKey() {
        return this.roleId;
    }

    public Set<String> getAuthorities() {
        return this.authorities;
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

    /*
     * Secondary cache enabled here, fetch authorities
     * 1) Fetch data from cache with roleId = this.roleId
     * 2.1) If null: Fetch authorities from database
     * 2.2) If not null: Return authorities directly ( pick up from cache )
     */
    private Future<JsonArray> fetchAuthoritiesAsyncWithCache() {
        /*
         * This workflow will execute when each user login
         * It means that the cache pool could be refreshed when role
         * permissions
         */
        return Sc.<JsonArray>cachePermission(this.roleId).compose(array -> {
            if (Objects.isNull(array)) {
                return this.fetchAuthoritiesAsync()
                    .compose(data -> Sc.cachePermission(this.roleId, data));
            } else {
                /* Authorities fill from cache ( Sync the authorities ) */
                array.stream().map(item -> (String) item)
                    .forEach(this.authorities::add);
                return Future.succeededFuture(array);
            }
        });
    }

    /*
     * Single authorities fetching
     * 1) Fetch data from database with roleId = this.roleId
     * 2) Extract data to JsonArray ( permission Ids )
     */
    private Future<JsonArray> fetchAuthoritiesAsync() {
        return Ux.Jooq.on(RRolePermDao.class)
            /* Fetch permission ids based on roleId */
            .<RRolePerm>fetchAsync(AuthKey.F_ROLE_ID, this.roleId)
            /* Refresh authorities in current profile */
            .compose(this::refreshAuthoritiesAsync);
    }

    /*
     * Extract the latest relations: role - permissions
     * 1) Clear current profile authorities
     * 2) Refresh current profile authorities by input permissions
     * 3) Returned ( JsonArray )
     */
    private Future<JsonArray> refreshAuthoritiesAsync(final List<RRolePerm> permissions) {
        return Future.succeededFuture(this.refreshAuthorities(permissions));
    }

    private JsonArray refreshAuthorities(final List<RRolePerm> permissions) {
        final List<String> permissionIds = permissions.stream()
            .filter(Objects::nonNull)
            .map(RRolePerm::getPermId)
            .collect(Collectors.toList());
        this.authorities.clear();
        this.authorities.addAll(permissionIds);
        return new JsonArray(permissionIds);
    }

    @Override
    public String toString() {
        return "ProfileRole{" +
            "roleId='" + this.roleId + '\'' +
            ", priority=" + this.priority +
            ", authorities=" + this.authorities +
            ", reference=" + this.reference +
            '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfileRole)) {
            return false;
        }
        final ProfileRole that = (ProfileRole) o;
        return this.roleId.equals(that.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.roleId);
    }
}
