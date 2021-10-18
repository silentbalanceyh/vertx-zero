package io.vertx.up.uca.options;

import java.util.concurrent.ConcurrentMap;

/**
 * @param <T>
 *
 * @author Lang
 */
public interface ServerVisitor<T>
    extends Visitor<ConcurrentMap<Integer, T>> {

    String YKEY_TYPE = "type";

    String YKEY_CONFIG = "config";

    String YKEY_NAME = "name";

    String KEY = "server";
}
