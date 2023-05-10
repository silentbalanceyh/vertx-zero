package io.vertx.up.specification.secure;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface AclView extends Serializable {
    /* Get name */
    String field();

    /* Depend set */
    AclView depend(boolean depend);

    /* If depend */
    boolean isDepend();

    /* If access */
    boolean isAccess();

    /* If edition */
    boolean isEdit();

    /* If readonly */
    boolean isReadOnly();

    /* Complex Process */
    default ConcurrentMap<String, AclView> complexMap() {
        return new ConcurrentHashMap<>();
    }
}
