package io.vertx.tp.modular.plugin;

import io.horizon.atom.Kv;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface INormalizer {

    Object before(Kv<String, Object> kv);
}
