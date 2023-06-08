package io.vertx.mod.rbac.logged;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.cv.AuthKey;
import io.vertx.up.fn.Fn;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/*
 * Single middle fetchProfile for group
 */
public class ProfileGroup implements Serializable {

    private transient final String groupId;
    private transient final Integer priority;
    private transient final JsonArray role;
    private transient final List<ProfileRole> roles = new ArrayList<>();
    private transient String reference;

    public ProfileGroup(final JsonObject data) {
        /* Group Id */
        this.groupId = data.getString(AuthKey.F_GROUP_ID);
        /* Priority */
        this.priority = data.getInteger(AuthKey.PRIORITY);
        /* Role */
        this.role = null == data.getJsonArray("role")
            ? new JsonArray() : data.getJsonArray("role");
    }

    Future<ProfileGroup> initAsync() {
        /* No determine */
        return this.fetchProfilesAsync().compose(profiles -> {
            /* Clear and add */
            this.setRoles(profiles);
            return Future.succeededFuture(this);
        });
    }

    public ProfileGroup init() {
        this.setRoles(this.fetchProfiles());
        return this;
    }

    public Integer getPriority() {
        return this.priority;
    }

    public String getKey() {
        return this.groupId;
    }

    public List<ProfileRole> getRoles() {
        return this.roles;
    }

    private void setRoles(final List<ProfileRole> profiles) {
        this.roles.clear();
        this.roles.addAll(profiles);
    }

    /*
     * Parent Reference for current fetchProfile group
     * */
    public String getReference() {
        return this.reference;
    }

    public ProfileGroup setReference(final String reference) {
        this.reference = reference;
        return this;
    }

    /*
     * Extract the latest relations: initAsync role for each group fetchProfile
     */
    @SuppressWarnings("all")
    private Future<List<ProfileRole>> fetchProfilesAsync() {
        final List futures = new ArrayList();
        this.role.stream().filter(Objects::nonNull)
            .map(item -> (JsonObject) item)
            .map(ProfileRole::new)
            .map(ProfileRole::initAsync)
            .forEach(futures::add);
        return CompositeFuture.join(futures)
            /* Composite Result */
            .compose(Fn::<ProfileRole>combineT)
            .compose(profiles -> {
                /* Bind each fetchProfile to group Id */
                profiles.forEach(profile -> profile.setGroup(this));
                return Future.succeededFuture(profiles);
            });
    }

    private List<ProfileRole> fetchProfiles() {
        return this.role.stream().filter(Objects::nonNull)
            .map(item -> (JsonObject) item)
            .map(ProfileRole::new)
            .map(ProfileRole::init)
            .map(role -> role.setGroup(this))
            .collect(Collectors.toList());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfileGroup)) {
            return false;
        }
        final ProfileGroup that = (ProfileGroup) o;
        return this.groupId.equals(that.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.groupId);
    }

    @Override
    public String toString() {
        return "ProfileGroup{" +
            "groupId='" + this.groupId + '\'' +
            ", priority=" + this.priority +
            ", role=" + this.role +
            ", reference='" + this.reference + '\'' +
            '}';
    }
}
