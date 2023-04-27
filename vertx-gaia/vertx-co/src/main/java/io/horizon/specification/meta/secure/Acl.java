package io.horizon.specification.meta.secure;

import io.horizon.eon.em.secure.ActPhase;
import io.vertx.core.json.JsonObject;

import java.io.Serializable;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Acl extends Serializable {
    /*
     * Acl configuration to store
     * seeker configuration
     */
    Acl config(JsonObject config);

    JsonObject config();

    /*
     * projection calculation
     */
    Set<String> aclVisible();

    /*
     * JsonObject calculation
     */
    JsonObject acl();

    /*
     * Phase
     */
    ActPhase phase();

    /*
     * Record bind
     */
    void bind(JsonObject record);
}
