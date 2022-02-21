package io.vertx.tp.workflow.atom;

import cn.zeroup.macrocosm.cv.em.RecordMode;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.em.ChangeFlag;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class ConfigRecord implements Serializable {
    private transient String unique = KName.KEY;
    private transient String indent;
    private transient ChangeFlag flag;
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
