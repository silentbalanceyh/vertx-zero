package io.vertx.up.uca.options;

import io.vertx.core.json.JsonObject;
import io.vertx.up.exception.ZeroException;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.uca.yaml.ZeroVertx;
import io.vertx.up.util.Ut;

public class JObjectOpts implements Visitor<JsonObject> {

    private static final Node<JsonObject> NODE
        = Ut.singleton(ZeroVertx.class);

    @Override
    public JsonObject visit(final String... nodes)
        throws ZeroException {
        Fn.inLenMin(this.getClass(), 0, nodes);
        // Tree Data
        final JsonObject tree = NODE.read();
        return Ut.visitJObject(tree, nodes);
    }
}
