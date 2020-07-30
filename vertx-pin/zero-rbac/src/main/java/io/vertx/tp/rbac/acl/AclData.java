package io.vertx.tp.rbac.acl;

import cn.vertxup.rbac.domain.tables.pojos.SVisitant;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.cv.em.AclType;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 * Data Structure with calculation
 */
public class AclData implements Serializable {
    /*
     * Acl processing
     * 1) visible
     * 2) view
     * 3) edit
     */
    private final ConcurrentMap<String, AclItem> commonMap =
            new ConcurrentHashMap<>();

    /*
     * Acl complex
     * 1) variety
     * 2) vow
     */
    private final ConcurrentMap<AclType, ConcurrentMap<String, AclMap>> complexMap =
            new ConcurrentHashMap<>();

    private final ConcurrentMap<String, JsonObject> dependMap =
            new ConcurrentHashMap<>();

    public AclData(final SVisitant visitant) {
        /*
         * Visible processing
         */
        final Set<String> visibleSet = Ut.toSet(Ut.toJArray(visitant.getAclVisible()));
        final Set<String> viewSet = Ut.toSet(Ut.toJArray(visitant.getAclView()));
        /*
         * Depend processing
         */
    }
}
