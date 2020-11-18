package io.vertx.tp.plugin.history;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.jooq.JooqInfix;
import io.vertx.up.eon.Constants;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*
 * Builder for `identifier`
 * 1) Transfer identifier to TABLE_NAME
 * 2) Double check whether the TABLE EXISTING
 * 3) Build Data into TABLE_NAME
 */
@SuppressWarnings("all")
class TrashBuilder {

    private static final Annal LOGGER = Annal.get(TrashBuilder.class);
    private static ConcurrentMap<String, Field> FIELD_MAP = new ConcurrentHashMap<String, Field>() {
        {
            put("key", DSL.field(DSL.name("KEY"), String.class));
            put("identifier", DSL.field(DSL.name("IDENTIFIER"), String.class));
            put("record", DSL.field(DSL.name("RECORD"), String.class));
            put("sigma", DSL.field(DSL.name("SIGMA"), String.class));
            put("language", DSL.field(DSL.name("LANGUAGE"), String.class));
            put("active", DSL.field(DSL.name("ACTIVE"), Boolean.class));
            put("createdBy", DSL.field(DSL.name("CREATED_BY"), String.class));
            put("createdAt", DSL.field(DSL.name("CREATED_AT"), Timestamp.class));
        }
    };
    private final transient DSLContext context;
    private final transient String identifier;
    private final transient String tableName;

    TrashBuilder(final String identifier) {
        this.identifier = identifier;
        final String tableName = this.identifier.toUpperCase()
                /*
                 * Here two format
                 * 1) such as `ci.server` that contains `.`;
                 * 2) such as `x-tabular` that contains `-`;
                 */
                .replace('.', '_')
                .replace('-', '_');
        this.tableName = "HIS_" + tableName;
        this.context = JooqInfix.getDSL(Constants.DEFAULT_JOOQ_HISTORY);
    }

    @Fluent
    public TrashBuilder init() {
        this.context.createTableIfNotExists(DSL.name(this.tableName))
                /* Primary Key */
                .column("KEY", SQLDataType.VARCHAR(36).nullable(false))
                .column("IDENTIFIER", SQLDataType.VARCHAR(255))
                .column("RECORD", SQLDataType.CLOB)
                /* Uniform */
                .column("SIGMA", SQLDataType.VARCHAR(255))
                .column("LANGUAGE", SQLDataType.VARCHAR(20))
                .column("ACTIVE", SQLDataType.BOOLEAN)
                /* Auditor */
                .column("CREATED_AT", SQLDataType.LOCALDATETIME)
                .column("CREATED_BY", SQLDataType.VARCHAR(36))
                /* PRIMARY KEY */
                .constraint(DSL.constraint("PK_" + this.tableName).primaryKey(DSL.name("KEY")))
                .execute();
        LOGGER.info("[ ZERO-HIS ] The table `{0}` has been created successfully!", this.tableName);
        return this;
    }

    public boolean createHistory(final JsonObject content, final MultiMap params) {
        /*
         * Insert History
         */
        final InsertSetMoreStep steps = stepInsert(content);
        steps.execute();
        return true;
    }

    private InsertSetMoreStep stepInsert(final JsonObject content) {
        final InsertSetMoreStep steps = (InsertSetMoreStep) this.context.insertInto(DSL.table(this.tableName));
        steps.set(FIELD_MAP.get("key"), UUID.randomUUID().toString());
        steps.set(FIELD_MAP.get("identifier"), this.identifier);
        steps.set(FIELD_MAP.get("record"), content.encode());
        /*
         * Default Value
         */
        steps.set(FIELD_MAP.get("sigma"), content.getString("sigma"));
        steps.set(FIELD_MAP.get("language"), content.getString("language"));
        steps.set(FIELD_MAP.get("active"), Boolean.TRUE);
        steps.set(FIELD_MAP.get("createdBy"), content.getString("createdBy"));

        final Date date = new Date();
        final Timestamp timestamp = new Timestamp(date.getTime());
        steps.set(FIELD_MAP.get("createdAt"), timestamp);
        return steps;
    }

    public boolean createHistory(final JsonArray content, final MultiMap params) {
        final List<Query> batchOps = new ArrayList<>();
        Ut.itJArray(content).map(this::stepInsert).forEach(batchOps::add);
        final Batch batch = this.context.batch(batchOps);
        batch.execute();
        return true;
    }
}
