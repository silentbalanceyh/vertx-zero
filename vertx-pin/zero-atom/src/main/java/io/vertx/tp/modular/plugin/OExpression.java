package io.vertx.tp.modular.plugin;

import io.horizon.atom.common.Kv;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface OExpression {

    Object after(Kv<String, Object> kv);
}
