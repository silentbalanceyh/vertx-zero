package io.vertx.up.uca.rs.hunt.adaptor;

import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.Envelop;
import io.vertx.up.log.Annal;
import io.vertx.up.runtime.ZeroYml;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.uca.yaml.ZeroVertx;
import io.vertx.up.util.Ut;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractWings implements Wings {
    private static final Node<JsonObject> NODE = Ut.singleton(ZeroVertx.class);
    private static final JsonObject environment = new JsonObject();

    static {
        final JsonObject initialized = NODE.read();
        if (Ut.isNotNil(initialized)) {
            environment.mergeIn(initialized, true);
        }
    }

    protected boolean isFreedom() {
        final JsonObject options = NODE.read();
        if (options.containsKey(ZeroYml.freedom)) {
            return options.getBoolean(ZeroYml.freedom);
        } else {
            return false;        // Default is non freedom
        }
    }

    protected String toFreedom(final Envelop envelop) {
        final JsonObject input = envelop.outJson();
        if (Ut.isNil(input)) {
            return null;
        } else {
            if (input.containsKey("data")) {
                final Object value = input.getValue("data");
                return Objects.isNull(value) ? null : value.toString();
            } else {
                return input.encode();
            }
        }
    }

    protected Annal logger() {
        return Annal.get(this.getClass());
    }
}
