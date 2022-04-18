package io.vertx.up.experiment.meld;

import io.vertx.up.commune.Json;

import java.io.Serializable;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HApp extends Serializable, Json {
    /* Uniform Model identifier */
    String identifier();

    /*
     * Uniform namespace here
     */
    String namespace();

    /*
     * Uniform File Path for initialize
     */
    String file();
}
