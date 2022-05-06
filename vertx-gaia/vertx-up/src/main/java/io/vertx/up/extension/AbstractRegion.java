package io.vertx.up.extension;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

public abstract class AbstractRegion implements PlugRegion {

    private transient final JsonObject config = new JsonObject();

    @Override
    public PlugRegion bind(final JsonObject config) {
        this.config.mergeIn(config);
        return this;
    }

    protected JsonObject config() {
        return this.config;
    }

    protected Annal getLogger() {
        return Annal.get(this.getClass());
    }

    protected boolean isEnabled(final RoutingContext context) {
        final String prefix = this.config.getString("prefix");
        if (Ut.isNil(prefix)) {
            this.getLogger().info("Data Region require config `prefix` attribute value, but now is null. Disabled! ");
            return false;
        } else {
            final String requestUri = context.request().path();
            return requestUri.startsWith(prefix);
        }
    }
}
