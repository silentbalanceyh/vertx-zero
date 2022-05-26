package io.vertx.up.experiment.mixture;

import io.vertx.up.experiment.specification.KPair;

/**
 * To avoid Open-Source and Product Conflict
 * 1) The default KPair must be loaded from `HED` interface, here:
 * - E: encrypt
 * - D: decrypt
 * 2) Zero Framework Provide Non-Implementation to be sure any customer/user could not see
 * the content of privateKey/publicKey
 * 3) This implementation class could be configured in
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HED {
    /*
     * RSA / DSA / ECC / DH
     */
    KPair loadRSA();
}
