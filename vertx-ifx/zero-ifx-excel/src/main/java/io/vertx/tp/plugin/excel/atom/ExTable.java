package io.vertx.tp.plugin.excel.atom;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.error._404ConnectMissingException;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExTable implements Serializable {
    /* Field */
    private final transient List<String> fields = new ArrayList<>();
    private final transient List<ExRecord> values = new ArrayList<>();

    /* Metadata Row */
    private transient final String sheet;
    private transient String name;
    private transient String description;
    private transient ExConnect connect;

    public ExTable(final String sheet) {
        this.sheet = sheet;
    }

    public String getName() {
        return this.name;
    }

    /*
     * ( Bean )
     */
    public void setName(final String name) {
        this.name = name;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    /*
     * Class<?>
     * Dao / Pojo
     */
    @SuppressWarnings("all")
    public <T> Class<T> getPojo() {
        return (Class<T>) this.getConnect().getPojo();
    }

    public Class<?> getDao() {
        return this.getConnect().getDao();
    }

    public String getPojoFile() {
        return this.getConnect().getPojoFile();
    }

    /*
     * Business Unique
     */
    public JsonObject whereUnique(final JsonObject data) {
        final JsonArray unique = this.getConnect().getUnique();
        final JsonObject filters = new JsonObject();
        Ut.itJArray(unique, String.class, (field, index) -> {
            final Object value = data.getValue(field);
            if (Objects.nonNull(value)) {
                filters.put(field, value);
            }
        });
        return filters;
    }

    /*
     * System Unique
     */
    @SuppressWarnings("unchecked")
    public <ID> ID whereKey(final JsonObject data) {
        final String keyField = this.getConnect().getKey();
        if (Objects.nonNull(keyField)) {
            final Object id = data.getValue(keyField);
            return null == id ? null : (ID) id;
        } else {
            return null;
        }
    }

    /*
     * ( No Bean ) Iterator row of Add operation
     */
    public void add(final String field) {
        if (Ut.notNil(field)) {
            this.fields.add(field);
        }
    }

    public void add(final ExRecord record) {
        if (!record.isEmpty()) {
            this.values.add(record);
        }
    }

    /*
     * Get row values of List, ExRecord row data.
     */
    public List<ExRecord> get() {
        return this.values;
    }

    /*
     * Extract field by index
     */
    public String field(final int index) {
        return this.fields.size() <= index ? null : this.fields.get(index);
    }

    public int size() {
        return this.fields.size();
    }

    private ExConnect getConnect() {
        Fn.outWeb(null == this.connect, _404ConnectMissingException.class, this.getClass(), this.name);
        return this.connect;
    }

    public void setConnect(final ExConnect connect) {
        this.connect = connect;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExTable)) {
            return false;
        }
        final ExTable table = (ExTable) o;
        return this.name.equals(table.name) &&
                this.sheet.equals(table.sheet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.sheet);
    }

    @Override
    public String toString() {
        final StringBuilder content = new StringBuilder();
        content.append("sheet = ").append(this.sheet).append(",");
        content.append("name = ").append(this.name).append(",");
        content.append("description = ").append(this.description).append("\n");
        content.append("daoCls = ").append(this.connect).append(",\n");
        this.fields.forEach(field -> content.append(field).append(","));
        content.append("\n");
        this.values.forEach(row -> content.append(row.toString()).append("\n"));
        return content.toString();
    }
}
