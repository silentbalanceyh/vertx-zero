package io.vertx.tp.rbac.acl;

import cn.vertxup.rbac.domain.tables.pojos.SVisitant;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.cv.em.AclType;
import io.vertx.up.commune.secure.Acl;
import io.vertx.up.commune.secure.AclView;
import io.vertx.up.util.Ut;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 * Data Structure with calculation
 */
public class AclData implements Acl {
    /*
     * Acl processing
     * 1) visible
     * 2) view
     * 3) edit
     */
    private final ConcurrentMap<String, AclView> commonMap =
            new ConcurrentHashMap<>();

    /*
     * Acl complex
     * 1) variety
     * 2) vow
     */
    private final ConcurrentMap<AclType, ConcurrentMap<String, AclView>> complexMap =
            new ConcurrentHashMap<AclType, ConcurrentMap<String, AclView>>() {
                {
                    this.put(AclType.DATA, new ConcurrentHashMap<>());
                    this.put(AclType.REFERENCE, new ConcurrentHashMap<>());
                }
            };

    private final ConcurrentMap<String, JsonObject> dependMap =
            new ConcurrentHashMap<>();
    private final ConcurrentMap<String, AclType> complexType =
            new ConcurrentHashMap<>();

    public AclData(final SVisitant visitant) {
        /*
         * Depend processing
         */
        Ut.<JsonObject>itJObject(Ut.toJObject(visitant.getAclVerge()),
                (config, field) -> this.dependMap.put(field, config));
        /*
         * Visible processing
         */
        final Set<String> visibleSet = Ut.toSet(Ut.toJArray(visitant.getAclVisible()));
        final Set<String> viewSet = Ut.toSet(Ut.toJArray(visitant.getAclView()));
        visibleSet.forEach(field -> {
            final boolean view = viewSet.contains(field);
            final AclView simple = new AclItem(field, view, true);
            simple.depend(this.dependMap.containsKey(field));
            this.commonMap.put(field, simple);
        });
        /*
         * variety json
         */
        final JsonObject varietyJson = Ut.toJObject(visitant.getAclVariety());
        this.initComplex(varietyJson, AclType.DATA, viewSet);
        final JsonObject vowJson = Ut.toJObject(visitant.getAclVow());
        this.initComplex(vowJson, AclType.REFERENCE, viewSet);
    }

    private void initComplex(final JsonObject input, final AclType type, final Set<String> viewSet) {
        Ut.<JsonObject>itJObject(input, (config, field) -> {
            final boolean view = viewSet.contains(field);
            final AclView complex = new AclMap(field, view, config);
            complex.depend(this.dependMap.containsKey(field));
            this.complexType.put(field, type);
            this.complexMap.get(type).put(field, complex);
        });
    }

    public Set<String> getProjection() {
        return null;
    }
}
