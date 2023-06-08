package io.vertx.mod.workflow.atom.configuration;

import cn.vertxup.workflow.cv.em.RecordMode;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.workflow.atom.runtime.WRecord;
import io.vertx.mod.workflow.uca.modeling.Respect;
import io.vertx.up.eon.KName;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class MetaInstance {
    private final transient ConfigRecord record;
    private final transient ConfigTodo todo;
    private final transient ConfigLinkage linkage;
    private final transient ConfigChild children;

    /*
     * The format of startJson to build following instances:
     * - ConfigRecord
     * - ConfigTodo
     * - ConfigLinkage
     * - ConfigChild
     *
     * The configuration format
     * {
     *     "todo": {},
     *     "record": {},
     *     "children": {},
     *     "linkage": {
     *         "linkageAttachment": {},
     *         "linkageTicket": {},
     *         "linkageAsset": {}
     *     }
     * }
     */
    private MetaInstance(final JsonObject startJson, final JsonObject linkageJson) {
        final JsonObject sure = Ut.valueJObject(startJson);
        /*
         * ConfigRunner for
         * - record
         * - todo
         * - linkage
         * - children
         *
         * All the configuration came from `StartConfig`
         */
        this.record = Ux.fromJson(sure.getJsonObject(KName.RECORD, new JsonObject()), ConfigRecord.class);
        this.children = Ux.fromJson(sure.getJsonObject(KName.CHILDREN, new JsonObject()), ConfigChild.class);
        this.linkage = new ConfigLinkage(linkageJson);
        this.todo = new ConfigTodo(sure);
    }

    private MetaInstance(final WRecord record, final MetaInstance input) {
        this.record = input.record;
        this.linkage = input.linkage;
        this.children = input.children;
        this.todo = new ConfigTodo(record);
    }

    public static MetaInstance input(final JsonObject configJson, final JsonObject linkageJson) {
        return new MetaInstance(configJson, linkageJson);
    }

    public static MetaInstance output(final WRecord record, final MetaInstance input) {
        return new MetaInstance(record, input);
    }

    // ------------------- Record Part ---------------------
    /*
     * Component Key for Cache component building
     */
    public String recordComponentKey(final Class<?> componentCls, final String componentConfig) {
        Objects.requireNonNull(componentCls);
        final StringBuilder componentKey = new StringBuilder();
        componentKey.append(componentCls.getName());
        componentKey.append(this.record.hashCode());
        if (Ut.isNotNil(componentConfig)) {
            componentKey.append(componentConfig.hashCode());
        }
        return componentKey.toString();
    }

    /*
     * Execution Mode of RecordMode ( Stored in `record` )
     */
    public RecordMode recordMode() {
        Objects.requireNonNull(this.record);
        return this.record.getMode();
    }

    /*
     * Bridge On UxJooq, this configuration is stored into json of `todo`
     * -- modelComponent field
     * It means that the function name is for record but the actual dao class stored into `todo`
     * instead of directly processing.
     */
    public UxJooq recordDao() {
        Objects.requireNonNull(this.todo);
        // Null Pointer Maybe when the clazz is not here with warning message.
        return Ux.Jooq.on(this.todo.dao());
    }

    public String recordIndent() {
        Objects.requireNonNull(this.record);
        return this.record.getIndent();
    }

    public Boolean recordSkip() {
        return this.record.getVirtual();
    }

    public String recordKeyU(final JsonObject recordData) {
        Objects.requireNonNull(this.record);
        final String key = this.record.unique(recordData);
        Objects.requireNonNull(key);
        return key;
    }

    public Set<String> recordKeyU(final JsonArray recordData) {
        Objects.requireNonNull(this.record);
        final Set<String> keys = new HashSet<>();
        Ut.itJArray(recordData).forEach(record -> keys.add(this.record.unique(record)));
        return keys;
    }

    // -------------------- Linkage Part -----------------
    public Set<String> linkFields() {
        Objects.requireNonNull(this.linkage);
        return this.linkage.fields();
    }

    public boolean linkSkip() {
        return Objects.isNull(this.linkage);
    }

    public Respect linkRespect(final String field) {
        Objects.requireNonNull(this.linkage);
        return this.linkage.respect(field);
    }

    // -------------------- Todo Generate ------------------
    /*
     * Initialize Todo Record into following data structure
     * {
     *      "field1": "value1",
     *      "field2": "value2",
     *      "...": "...",
     *      "record": "JsonObject / JsonArray"
     * }
     *
     *
     * Build data as following:
     * - serial/code:               Generated by Indent
     * - name:                      Expression Processing
     * - flowDefinitionKey
     *   flowDefinitionId:          Workflow Engine
     * - modelKey                   「JsonObject」
     *   modelChild:                「JsonArray」Relation between ticket / record
     */
    public Future<JsonObject> todoInitialize(final JsonObject todoData) {
        return this.todo.initialize(todoData);
    }

    // -------------------- Child Part --------------------
    public UxJooq childDao() {
        if (Objects.isNull(this.children)) {
            return null;
        }
        if (Objects.isNull(this.children.getDao())) {
            return null;
        }
        return Ux.Jooq.on(this.children.getDao());
    }

    public JsonObject childIn(final JsonObject params) {
        final JsonObject childData = new JsonObject();
        final Set<String> fields = Ut.toSet(this.children.getFields());
        // JsonObject / JsonArray Serialization
        final Set<String> complex = Ut.toSet(this.children.getComplex());
        fields.forEach(field -> {
            final Object value = params.getValue(field);
            if (complex.contains(field)) {
                // Complex -> To String Required
                childData.put(field, Ut.encryptJ(value));
            } else {
                // Common Field
                childData.put(field, value);
            }
        });
        return childData;
    }

    public JsonObject childOut(final JsonObject queryJ) {
        // JsonObject / JsonArray Serialization
        final Set<String> complex = Ut.toSet(this.children.getComplex());
        final JsonObject responseJ = queryJ.copy();
        complex.forEach(field -> {
            final Object value = queryJ.getValue(field);
            if (complex.contains(field) && value instanceof String) {
                final Object replaced = Ut.decryptJ((String) value);
                responseJ.put(field, replaced);
            }
        });
        return responseJ;
    }

    public JsonArray childAuditor() {
        if (Objects.isNull(this.children)) {
            return new JsonArray();
        }
        return this.children.getAuditor();
    }
}
