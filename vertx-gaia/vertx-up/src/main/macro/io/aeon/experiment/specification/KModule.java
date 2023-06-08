package io.aeon.experiment.specification;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ClassDeserializer;
import com.fasterxml.jackson.databind.ClassSerializer;
import com.fasterxml.jackson.databind.JsonObjectDeserializer;
import com.fasterxml.jackson.databind.JsonObjectSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.horizon.eon.em.EmDS;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.extension.KTransform;
import io.vertx.up.atom.shape.KField;
import io.vertx.up.atom.shape.KJoin;
import io.vertx.up.atom.shape.KPoint;
import io.vertx.up.plugin.jooq.JooqPin;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Objects;

public class KModule implements Serializable {

    private String name;
    private String pojo;
    private String mode;
    private String modeKey;     // mode = EXTENSION
    private KField field;
    private KColumn column;
    @JsonIgnore
    private String identifier;

    private KJoin connect;     // connect for 1 join 1

    @JsonSerialize(using = ClassSerializer.class)
    @JsonDeserialize(using = ClassDeserializer.class)
    private Class<?> daoCls;

    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private JsonObject header;

    private KTransform transform;

    private JsonObject aop;

    public KField getField() {
        return this.field;
    }

    public void setField(final KField field) {
        this.field = field;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String identifier() {
        if (Objects.nonNull(this.column)) {
            /* Crud Identifier Extract in Old Version */
            return this.column.getIdentifier();
        } else {
            /*
             * HAtom new version to put code logical into configuration file
             * `identifier` attach on configuration
             */
            return this.identifier;
        }
    }

    public KModule identifier(final String identifier) {
        this.identifier = identifier;
        return this;
    }

    public String getPojo() {
        return this.pojo;
    }

    public void setPojo(final String pojo) {
        this.pojo = pojo;
    }


    public Class<?> getDaoCls() {
        return this.daoCls;
    }

    public void setDaoCls(final Class<?> daoCls) {
        this.daoCls = daoCls;
    }

    public JsonObject getHeader() {
        return this.header;
    }

    public void setHeader(final JsonObject header) {
        this.header = header;
    }

    public KColumn getColumn() {
        return this.column;
    }

    public void setColumn(final KColumn column) {
        this.column = column;
    }

    public String getTable() {
        Objects.requireNonNull(this.daoCls);
        return JooqPin.initTable(this.daoCls);
    }

    public Class<?> getPojoCls() {
        Objects.requireNonNull(this.daoCls);
        return JooqPin.initPojo(this.daoCls);
    }

    public KJoin getConnect() {
        return this.connect;
    }

    public void setConnect(final KJoin connect) {
        this.connect = connect;
    }

    public KPoint getConnect(final String identifier) {
        if (Objects.isNull(this.connect)) {
            return null;
        }
        return this.connect.point(identifier);
    }

    public EmDS.Stored getMode() {
        if (Objects.isNull(this.mode)) {
            return EmDS.Stored.PRIMARY;
        } else {
            return Ut.toEnum(() -> this.mode, EmDS.Stored.class, EmDS.Stored.PRIMARY);
        }
    }

    public void setMode(final EmDS.Stored mode) {
        if (Objects.isNull(mode)) {
            this.mode = EmDS.Stored.PRIMARY.name();
        } else {
            this.mode = mode.name();
        }
    }

    public KTransform getTransform() {
        return this.transform;
    }

    public void setTransform(final KTransform transform) {
        this.transform = transform;
    }

    public String getModeKey() {
        return this.modeKey;
    }

    public void setModeKey(final String modeKey) {
        this.modeKey = modeKey;
    }

    public JsonObject getAop() {
        return this.aop;
    }

    public void setAop(final JsonObject aop) {
        this.aop = aop;
    }

    @Override
    public String toString() {
        return "IxModule{" +
            "name='" + this.name + '\'' +
            ", pojo='" + this.pojo + '\'' +
            ", mode='" + this.mode + '\'' +
            ", modeKey='" + this.modeKey + '\'' +
            ", field=" + this.field +
            ", column=" + this.column +
            ", connect=" + this.connect +
            ", daoCls=" + this.daoCls +
            ", header=" + this.header +
            ", transform=" + this.transform +
            ", aop=" + this.aop +
            '}';
    }
}
