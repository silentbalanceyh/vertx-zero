package io.vertx.mod.rbac.atom.acl;

import cn.vertxup.rbac.domain.tables.pojos.SVisitant;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.cv.em.AclType;
import io.vertx.up.eon.em.EmSecure;
import io.vertx.up.specification.secure.Acl;
import io.vertx.up.specification.secure.AclView;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 * Data Structure with calculation
 */
public class AclData implements Acl {

    private final Set<String> fields = new TreeSet<>();
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

    private final EmSecure.ActPhase phase;
    private final JsonObject config = new JsonObject();

    public AclData(final SVisitant visitant) {
        if (Objects.nonNull(visitant)) {
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
            /*
             * Convert to `EAGER` as default
             */
            this.phase = Ut.toEnum(visitant::getPhase, EmSecure.ActPhase.class, EmSecure.ActPhase.EAGER);
        } else {
            this.phase = EmSecure.ActPhase.ERROR;
        }
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

    @Override
    public Set<String> aclVisible() {
        return this.commonMap.keySet();
    }

    @Override
    public EmSecure.ActPhase phase() {
        return this.phase;
    }

    @Override
    public void bind(final JsonObject record) {
        if (Ut.isNotNil(record)) {
            /*
             * Bind all fields here
             */
            this.fields.addAll(record.fieldNames());
        }
    }

    @Override
    public Acl config(final JsonObject config) {
        if (Ut.isNotNil(config)) {
            this.config.mergeIn(config);
        }
        return this;
    }

    @Override
    public JsonObject config() {
        return this.config;
    }

    @Override
    public JsonObject acl() {
        final JsonObject acl = new JsonObject();
        /*
         * capture access field information
         */
        final JsonArray access = new JsonArray();
        final JsonArray edition = new JsonArray();
        this.commonMap.forEach((field, aclField) -> {
            /*
             * access: []
             * edition: []
             */
            if (aclField.isAccess()) {
                access.add(field);
            }
            if (aclField.isEdit()) {
                edition.add(field);
            }
        });
        if (Ut.isNotNil(access)) {
            acl.put("access", access);
        }
        if (Ut.isNotNil(edition) && Ut.isNil(this.config)) {
            acl.put("edition", edition);
        }
        /*
         * access + this.fields
         * 1) When access > this.fields, it should be edition
         * 2) Then it's readonly
         */
        if (Ut.isNil(this.config)) {
            final Set<String> accessArr = Ut.toSet(access.copy());
            accessArr.addAll(this.fields);
            acl.put("fields", Ut.toJArray(accessArr));
        }
        return acl;
    }
}
