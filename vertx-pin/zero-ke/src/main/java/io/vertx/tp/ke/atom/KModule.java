package io.vertx.tp.ke.atom;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.atom.connect.KJoin;
import io.vertx.tp.ke.atom.view.KColumn;
import io.vertx.tp.ke.cv.em.DSMode;
import io.vertx.up.commune.exchange.DictConfig;
import io.vertx.up.commune.exchange.DictEpsilon;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

public class KModule implements Serializable {

    private String name;
    private String table;
    private String pojo;
    private String mode;
    private String modeKey;     // mode = EXTENSION
    private KField field;
    private KColumn column;

    private KJoin connect;     // connect for 1 join 1

    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private JsonObject epsilon; // dict / consume

    @JsonSerialize(using = JsonArraySerializer.class)
    @JsonDeserialize(using = JsonArrayDeserializer.class)
    private JsonArray source;   // dict / source

    @JsonSerialize(using = ClassSerializer.class)
    @JsonDeserialize(using = ClassDeserializer.class)
    private Class<?> pojoCls;

    @JsonSerialize(using = ClassSerializer.class)
    @JsonDeserialize(using = ClassDeserializer.class)
    private Class<?> daoCls;

    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private JsonObject header;

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

    public String getIdentifier() {
        if (Objects.nonNull(this.column)) {
            return this.column.getIdentifier();
        } else {
            return null;
        }
    }

    public String getPojo() {
        return this.pojo;
    }

    public void setPojo(final String pojo) {
        this.pojo = pojo;
    }

    public Class<?> getPojoCls() {
        return this.pojoCls;
    }

    public void setPojoCls(final Class<?> pojoCls) {
        this.pojoCls = pojoCls;
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
        return this.table;
    }

    public void setTable(final String table) {
        this.table = table;
    }

    public KJoin getConnect() {
        return this.connect;
    }

    public void setConnect(final KJoin connect) {
        this.connect = connect;
    }

    public ConcurrentMap<String, DictEpsilon> getEpsilon() {
        return Ux.dictEpsilon(Objects.isNull(this.epsilon) ? new JsonObject() : this.epsilon);
    }

    public void setEpsilon(final JsonObject epsilon) {
        this.epsilon = epsilon;
    }

    public DictConfig getSource() {
        final JsonArray source = Objects.isNull(this.source) ? new JsonArray() : this.source;
        return new DictConfig(source);
    }

    public void setSource(final JsonArray source) {
        this.source = source;
    }

    public DSMode getMode() {
        if (Objects.isNull(this.mode)) {
            return DSMode.PRIMARY;
        } else {
            return Ut.toEnum(() -> this.mode, DSMode.class, DSMode.PRIMARY);
        }
    }

    public void setMode(final DSMode mode) {
        if (Objects.isNull(mode)) {
            this.mode = DSMode.PRIMARY.name();
        } else {
            this.mode = mode.name();
        }
    }

    public String getModeKey() {
        return this.modeKey;
    }

    public void setModeKey(final String modeKey) {
        this.modeKey = modeKey;
    }

    @Override
    public String toString() {
        return "IxModule{" +
                "name='" + this.name + '\'' +
                ", table='" + this.table + '\'' +
                ", pojo='" + this.pojo + '\'' +
                ", mode='" + this.mode + '\'' +
                ", modeKey='" + this.modeKey + '\'' +
                ", field=" + this.field +
                ", column=" + this.column +
                ", connect=" + this.connect +
                ", epsilon=" + this.epsilon +
                ", source=" + this.source +
                ", pojoCls=" + this.pojoCls +
                ", daoCls=" + this.daoCls +
                ", header=" + this.header +
                '}';
    }
}
