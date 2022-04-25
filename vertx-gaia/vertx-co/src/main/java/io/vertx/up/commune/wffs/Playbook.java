package io.vertx.up.commune.wffs;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.io.Serializable;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class Playbook implements Serializable {

    private final String expression;
    private final transient JsonObject tpl = new JsonObject();

    private Playbook(final String expression) {
        this.expression = expression;
    }

    public static Playbook open(final String expression) {
        return new Playbook(expression);
    }

    public Playbook bind(final JsonObject tpl) {
        final JsonObject tplJ = Ut.valueJObject(tpl);
        this.tpl.mergeIn(tplJ, true);
        return this;
    }

    public Future<Boolean> isSatisfy(final JsonObject params) {
        System.out.println(this.expression);
        System.out.println(params);
        return Future.succeededFuture(Boolean.TRUE);
    }
}
