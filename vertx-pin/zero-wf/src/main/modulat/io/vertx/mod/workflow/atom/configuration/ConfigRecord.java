package io.vertx.mod.workflow.atom.configuration;

import cn.vertxup.workflow.cv.em.RecordMode;
import io.horizon.eon.em.typed.ChangeFlag;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class ConfigRecord implements Serializable {
    /*
     * Whether current record operation is virtual
     * 1. When record is Single ( JsonObject ), skip virtual checking here.
     * 2. When record is Batch ( JsonArray )
     * -- virtual = FALSE ( default ), actual crud will be triggered.
     * -- virtual = TRUE ( configured ), actual crud will not be triggered except the last step ( End )
     *
     * For example
     *
     * 1. File Management:  Single, skip virtual checking here.
     * 2. Asset KIncome:     Batch, Related linkage `asset` as record, ( virtual = true )
     * 3. Other workflow:   Batch, Actual entities will be in `record` node ( virtual = false ), CRUD happened.
     */
    private transient Boolean virtual = Boolean.FALSE;
    // Default unique field: `key` as default
    private transient String unique = KName.KEY;
    // Code generation here
    private transient String indent;
    // ADD, UPDATE, DELETE
    private transient ChangeFlag flag;
    // DAO      - Static Dao of Jooq
    // ATOM     - Dynamic Model
    // CASE     - Camunda Case of Incident
    private transient RecordMode mode = RecordMode.DAO;

    public String getIndent() {
        return this.indent;
    }

    public void setIndent(final String indent) {
        this.indent = indent;
    }

    public String getUnique() {
        return this.unique;
    }

    public void setUnique(final String unique) {
        this.unique = unique;
    }

    public ChangeFlag getFlag() {
        return this.flag;
    }

    public void setFlag(final ChangeFlag flag) {
        this.flag = flag;
    }

    public RecordMode getMode() {
        return this.mode;
    }

    public void setMode(final RecordMode mode) {
        this.mode = mode;
    }

    public String unique(final JsonObject params) {
        return params.getString(this.unique, null);
    }

    public Boolean getVirtual() {
        return this.virtual;
    }

    public void setVirtual(final Boolean virtual) {
        this.virtual = virtual;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ConfigRecord record = (ConfigRecord) o;
        return Objects.equals(this.unique, record.unique) && this.flag == record.flag && this.mode == record.mode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.unique, this.flag, this.mode);
    }
}
