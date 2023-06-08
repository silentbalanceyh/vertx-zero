package io.horizon.spi.web;

import io.aeon.experiment.specification.KCredential;
import io.vertx.core.Future;

/*
 * It's for account to do `O_USER` data creation
 * 1) It will connect any other system to process credential data
 * 2) The data structure is as following
 * {
 *      "realm": "The Security Domain",
 *      "authorization": "The Type of Credential"
 * }
 */
public interface Credential {
    /*
     * The sigma could identify the application
     * in multi environments
     */
    Future<KCredential> fetchAsync(final String sigma);
}
