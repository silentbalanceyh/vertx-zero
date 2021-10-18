package io.vertx.tp.rbac.logged;

import cn.vertxup.rbac.domain.tables.daos.RRolePermDao;
import cn.vertxup.rbac.domain.tables.pojos.RRolePerm;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.tp.rbac.atom.ScConfig;
import io.vertx.tp.rbac.cv.AuthKey;
import io.vertx.tp.rbac.init.ScPin;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.cache.Rapid;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * Data in Global Shared Data for current role
 * Connect to Pool: permissionPool
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ScRole {
    private static final ConcurrentMap<String, ScRole> ROLES = new ConcurrentHashMap<>();
    private static final Annal LOGGER = Annal.get(ScRole.class);
    private static final ScConfig CONFIG = ScPin.getConfig();
    private final transient Rapid<String, JsonArray> cache;
    private final transient String roleId;
    private final transient Set<String> authorities = new HashSet<>();

    private ScRole(final String roleId) {
        this.roleId = roleId;
        this.cache = Rapid.t(CONFIG.getPermissionPool());
    }

    public static ScRole login(final String roleId) {
        return Fn.pool(ROLES, roleId, () -> new ScRole(roleId));
    }

    public String key() {
        return this.roleId;
    }

    public Set<String> authorities() {
        return this.authorities;
    }

    // ------------------------- Initialized Method ------------------------
    public Future<JsonArray> clear() {
        ROLES.remove(this.roleId);
        return this.cache.clear(this.roleId);
    }

    /*
     * Secondary cache enabled here, fetch authorities
     * 1) Fetch data from cache with roleId = this.roleId
     * 2.1) If null: Fetch authorities from database
     * 2.2) If not null: Return authorities directly ( pick up from cache )
     */
    public Future<JsonArray> fetchWithCache() {
        /*
         * This workflow will execute when each user login
         * It means that the cache pool could be refreshed when role
         * permissions
         */
        return this.permission().compose(permissions -> {
            if (Objects.isNull(permissions)) {
                return this.fetch().compose(this::permission);
            } else {
                Sc.infoAuth(LOGGER, "ScRole \u001b[0;37m----> Cache key = {0}\u001b[m.", this.roleId);
                /* Authorities fill from cache ( Sync the authorities ) */
                permissions.stream().map(item -> (String) item)
                    .forEach(this.authorities::add);
                return Ux.future(permissions);
            }
        });
    }

    /*
     * Single authorities fetching
     * 1) Fetch data from database with roleId = this.roleId
     * 2) Extract data to JsonArray ( permission Ids )
     */
    public Future<JsonArray> fetch() {
        return Ux.Jooq.on(RRolePermDao.class)
            /* Fetch permission ids based on roleId */
            .<RRolePerm>fetchAsync(AuthKey.F_ROLE_ID, this.roleId)
            /*
             * Extract the latest relations: role - permissions
             * 1) Clear current profile authorities
             * 2) Refresh current profile authorities by input permissions
             * 3) Returned ( JsonArray )
             */
            .compose(permissions -> Ux.future(this.authorities(permissions)));
    }

    public void refresh() {
        /* Fetch permission ( Without Cache in Sync mode ) */
        final List<RRolePerm> queried = Ux.Jooq.on(RRolePermDao.class)
            .fetch(AuthKey.F_ROLE_ID, this.roleId);
        this.authorities(queried);
    }

    public Future<JsonArray> refresh(final JsonArray permissions) {
        return this.permission(permissions);
    }

    // ------------------------- Permission Method ------------------------
    private JsonArray authorities(final List<RRolePerm> permissions) {
        final List<String> permissionIds = permissions.stream()
            .filter(Objects::nonNull)
            .map(RRolePerm::getPermId)
            .collect(Collectors.toList());
        this.authorities.clear();
        this.authorities.addAll(permissionIds);
        return Ut.toJArray(permissionIds);
    }

    /*
     * Pool configured default parameters
     * - permissionPool
     * This pool is for permission of role:
     * key = role id
     * - S_ROLE ( key )
     * value = permissions ( JsonArray )
     */
    private Future<JsonArray> permission() {
        return this.cache.read(this.roleId);
    }

    private Future<JsonArray> permission(final JsonArray permission) {
        return this.cache.write(this.roleId, permission);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ScRole scRole = (ScRole) o;
        return this.roleId.equals(scRole.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.roleId);
    }

    @Override
    public String toString() {
        return "ScRole{" +
            "roleId='" + this.roleId + '\'' +
            ", authorities=" + this.authorities +
            '}';
    }
}
