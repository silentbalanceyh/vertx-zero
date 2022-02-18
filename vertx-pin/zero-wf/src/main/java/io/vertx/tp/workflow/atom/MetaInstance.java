package io.vertx.tp.workflow.atom;

import cn.zeroup.macrocosm.cv.em.RecordMode;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.up.eon.KName;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class MetaInstance {
    private final transient ConfigRecord record;
    private final transient ConfigTodo todo;
    private final transient ConfigLinkage linkage;

    private MetaInstance(final JsonObject startJson) {
        final JsonObject sure = Ut.sureJObject(startJson);
        /*
         * ConfigRunner for
         * - record
         * - todo
         * - linkage
         *
         * All the configuration came from `StartConfig`
         */
        this.record = Ux.fromJson(sure.getJsonObject(KName.RECORD, new JsonObject()), ConfigRecord.class);
        this.linkage = Ux.fromJson(sure.getJsonObject(KName.LINKAGE, new JsonObject()), ConfigLinkage.class);
        this.todo = new ConfigTodo(sure);
    }

    private MetaInstance(final WRecord record) {
        this.record = null;
        this.linkage = null;
        this.todo = new ConfigTodo(record);
    }

    public static MetaInstance input(final JsonObject configJson) {
        return new MetaInstance(configJson);
    }

    public static MetaInstance output(final WRecord record) {
        return new MetaInstance(record);
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
        if (Ut.notNil(componentConfig)) {
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

    public String recordKeyU(final JsonObject recordData) {
        Objects.requireNonNull(this.record);
        final String key = this.record.unique(recordData);
        Objects.requireNonNull(key);
        return key;
    }

    // -------------------- Linkage Part -----------------
    public Set<String> linkFields() {
        Objects.requireNonNull(this.linkage);
        return this.linkage.fields();
    }

    public boolean linkSkip() {
        return Objects.isNull(this.linkage);
    }

    public JsonObject linkCondition(final String field) {
        Objects.requireNonNull(this.linkage);
        return this.linkage.condition(field);
    }

    // -------------------- Serial Part --------------------
    public Future<JsonObject> serialT(final JsonObject todoData) {
        return this.todo.generate(todoData);
    }

    public Future<JsonObject> serialR(final JsonObject recordData) {
        Objects.requireNonNull(this.record);
        return Ke.umIndent(recordData, this.record.getIndent());
    }
}
