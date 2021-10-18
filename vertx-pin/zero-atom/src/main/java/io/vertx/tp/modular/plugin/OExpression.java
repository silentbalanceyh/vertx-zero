package io.vertx.tp.modular.plugin;

import io.vertx.up.atom.Kv;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface OExpression {

    Object after(Kv<String, Object> kv);
}
