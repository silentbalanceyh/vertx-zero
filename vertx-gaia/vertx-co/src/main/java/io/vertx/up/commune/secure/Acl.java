package io.vertx.up.commune.secure;

import io.vertx.core.json.JsonObject;

import java.io.Serializable;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public interface Acl extends Serializable {
    /*
     * projection calculation
     */
    Set<String> projection();

    /*
     * JsonObject calculation
     */
    JsonObject aclData();
}
