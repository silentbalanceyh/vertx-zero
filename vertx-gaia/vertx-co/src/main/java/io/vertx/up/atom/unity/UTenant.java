package io.vertx.up.atom.unity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonArrayDeserializer;
import com.fasterxml.jackson.databind.JsonArraySerializer;
import com.fasterxml.jackson.databind.JsonObjectDeserializer;
import com.fasterxml.jackson.databind.JsonObjectSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.Copyable;
import io.vertx.up.commune.config.Integration;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class UTenant implements Serializable, Copyable<UTenant> {
    @JsonIgnore
    private final transient ConcurrentMap<String, Integration> integrationMap = new ConcurrentHashMap<>();
    @JsonIgnore
    private final transient ConcurrentMap<String, String> vendorMap = new ConcurrentHashMap<>();
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private transient JsonObject global;
    @JsonSerialize(using = JsonArraySerializer.class)
    @JsonDeserialize(using = JsonArrayDeserializer.class)
    private transient JsonArray source;
    private transient ConcurrentMap<String, JsonObject> mapping = new ConcurrentHashMap<>();
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private transient JsonObject application;

    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private transient JsonObject integration;

    public JsonObject getIntegration() {
        return this.integration;
    }

    public void setIntegration(final JsonObject integration) {
        this.integration = integration;
        if (Ut.notNil(integration)) {
            // Integration Configuration
            Ut.<JsonObject>itJObject(integration, (vendor, name) -> {
                final String configFile = vendor.getString(KName.CONFIG, null);
                Objects.requireNonNull(configFile);
                // Basic Information of
                final Integration config = new Integration();
                config.fromFile(configFile);
                // Vendor Name
                final String vendorName = vendor.getString(KName.NAME);
                config.setVendorConfig(vendorName);
                config.setVendor(name);
                this.integrationMap.put(name, config);
            });
        }
    }

    public Integration integration(final String key) {
        return this.integrationMap.getOrDefault(key, null);
    }

    public Set<String> vendors() {
        return this.integrationMap.keySet();
    }

    public JsonObject getGlobal() {
        return Objects.isNull(this.global) ? new JsonObject() : this.global.copy();
    }

    public void setGlobal(final JsonObject global) {
        this.global = global;
    }

    public JsonArray getSource() {
        return this.source;
    }

    public void setSource(final JsonArray source) {
        this.source = source;
    }

    public ConcurrentMap<String, JsonObject> getMapping() {
        return this.mapping;
    }

    public void setMapping(final ConcurrentMap<String, JsonObject> mapping) {
        this.mapping = mapping;
    }

    public JsonObject getApplication() {
        final JsonObject application = Objects.isNull(this.application) ? new JsonObject() : this.application.copy();
        if (Objects.isNull(this.global)) {
            return application;
        } else {
            return Ut.ifJCopy(this.global,
                KName.APP_ID, KName.SIGMA, KName.APP_KEY
            ).apply(this.application);
        }
    }

    public void setApplication(final JsonObject application) {
        this.application = application;
    }

    @Override
    public UTenant copy() {
        final UTenant tenant = new UTenant();
        tenant.application = this.application.copy();
        tenant.global = this.global.copy();
        tenant.integration = this.integration.copy();
        tenant.source = this.source.copy();
        // Vendor
        tenant.vendorMap.clear();
        tenant.vendorMap.putAll(this.vendorMap);
        // Mapping
        tenant.mapping.clear();
        this.mapping.forEach((key, item) -> tenant.mapping.put(key, item.copy()));
        // Integration
        tenant.integrationMap.clear();
        this.integrationMap.forEach((key, integration) -> tenant.integrationMap.put(key, integration.copy()));
        return tenant;
    }

    @Override
    public String toString() {
        return "UTenant{" +
            "vendorMap=" + this.vendorMap +
            ", global=" + this.global +
            ", source=" + this.source +
            ", mapping=" + this.mapping +
            ", application=" + this.application +
            ", integration=" + this.integration +
            '}';
    }
}
