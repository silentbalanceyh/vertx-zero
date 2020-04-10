package io.vertx.tp.atom.modeling;

import io.vertx.up.commune.Json;

import java.io.Serializable;

/**
 * Ox模型中必须存在的内容
 */
interface AoShared extends Serializable, Json {

    String identifier();

    String file();

    String namespace();
}
