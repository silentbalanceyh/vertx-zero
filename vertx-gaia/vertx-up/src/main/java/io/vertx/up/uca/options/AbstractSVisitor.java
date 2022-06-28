package io.vertx.up.uca.options;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Plugins;
import io.vertx.up.exception.ZeroException;
import io.vertx.up.exception.demon.ServerConfigException;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractSVisitor {
    private transient final Node<JsonObject> NODE = Node.infix(Plugins.SERVER);

    protected JsonArray serverPre(final int expected, final String... key)
        throws ZeroException {
        // 1. Must be the first line, fixed position.
        Fn.inLenEq(this.getClass(), expected, (Object[]) key);
        // 2. Visit the node for server, http
        final JsonObject data = this.NODE.read();

        Fn.outZero(null == data || !data.containsKey(KName.SERVER), this.logger(),
            ServerConfigException.class,
            this.getClass(), null == data ? null : data.encode());

        return Ut.valueJArray(data, KName.SERVER);
    }

    public Annal logger() {
        return Annal.get(this.getClass());
    }
}
