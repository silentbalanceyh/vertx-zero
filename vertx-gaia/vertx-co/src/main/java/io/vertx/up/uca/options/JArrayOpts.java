package io.vertx.up.uca.options;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.uca.yaml.ZeroVertx;
import io.vertx.up.util.Ut;

public class JArrayOpts implements Visitor<JsonArray> {

    private static final Node<JsonObject> NODE
            = Ut.singleton(ZeroVertx.class);

    @Override
    public JsonArray visit(final String... nodes) {
        Fn.inLenMin(this.getClass(), 0, nodes);
        final JsonObject tree = NODE.read();
        return Ut.visitJArray(tree, nodes);
    }
}
