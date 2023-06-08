package io.vertx.mod.rbac.atom;

import com.fasterxml.jackson.databind.JsonArrayDeserializer;
import com.fasterxml.jackson.databind.JsonArraySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.core.json.JsonArray;
import io.vertx.up.eon.KName;

import java.io.Serializable;

public class ScCondition implements Serializable {
    @JsonSerialize(using = JsonArraySerializer.class)
    @JsonDeserialize(using = JsonArrayDeserializer.class)
    private JsonArray user = new JsonArray().add(KName.SIGMA);

    @JsonSerialize(using = JsonArraySerializer.class)
    @JsonDeserialize(using = JsonArrayDeserializer.class)
    private JsonArray role = new JsonArray().add(KName.SIGMA);

    @JsonSerialize(using = JsonArraySerializer.class)
    @JsonDeserialize(using = JsonArrayDeserializer.class)
    private JsonArray group = new JsonArray().add(KName.SIGMA);
    @JsonSerialize(using = JsonArraySerializer.class)
    @JsonDeserialize(using = JsonArrayDeserializer.class)
    private JsonArray action = new JsonArray().add(KName.SIGMA);
    @JsonSerialize(using = JsonArraySerializer.class)
    @JsonDeserialize(using = JsonArrayDeserializer.class)
    private JsonArray permission = new JsonArray().add(KName.SIGMA);
    @JsonSerialize(using = JsonArraySerializer.class)
    @JsonDeserialize(using = JsonArrayDeserializer.class)
    private JsonArray resource = new JsonArray().add(KName.SIGMA);

    public JsonArray getUser() {
        return this.user;
    }

    public void setUser(final JsonArray user) {
        this.user = user;
    }

    public JsonArray getRole() {
        return this.role;
    }

    public void setRole(final JsonArray role) {
        this.role = role;
    }

    public JsonArray getGroup() {
        return this.group;
    }

    public void setGroup(final JsonArray group) {
        this.group = group;
    }

    public JsonArray getAction() {
        return this.action;
    }

    public void setAction(final JsonArray action) {
        this.action = action;
    }

    public JsonArray getPermission() {
        return this.permission;
    }

    public void setPermission(final JsonArray permission) {
        this.permission = permission;
    }

    public JsonArray getResource() {
        return this.resource;
    }

    public void setResource(final JsonArray resource) {
        this.resource = resource;
    }

    @Override
    public String toString() {
        return "ScCondition{" +
            "user=" + this.user +
            ", role=" + this.role +
            ", group=" + this.group +
            ", action=" + this.action +
            ", permission=" + this.permission +
            ", resource=" + this.resource +
            '}';
    }
}
