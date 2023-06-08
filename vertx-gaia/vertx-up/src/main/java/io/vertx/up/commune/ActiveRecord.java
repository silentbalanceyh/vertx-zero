package io.vertx.up.commune;

import io.horizon.uca.log.Annal;
import io.modello.specification.HRecord;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*
 * Provide abstract record to avoid more writing method of record
 * Each extension record could inherit from this
 */
public abstract class ActiveRecord implements HRecord {
    /*
     * The core data structure to store data in json format
     */
    private final transient JsonObject data = new JsonObject();

    protected Annal getLogger() {
        return Annal.get(this.getClass());
    }

    /*
     * This method is used in sub-class only
     */
    protected JsonObject data() {
        return this.data;
    }

    // -------------- Data Checked ---------------
    /*
     * Whether data is empty
     */
    @Override
    public boolean isEmpty() {
        return this.data.isEmpty();
    }

    @Override
    public boolean isPersist() {
        // TODO: This feature is for future usage
        return false;
    }

    @Override
    public boolean isValue(final String field) {
        return this.data.containsKey(field);
    }

    // -------------- Data Get/Set ---------------
    /*
     * Get the value by field ( field = value ) of record
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(final String field) {
        final Object value = this.data.getValue(field);
        return (T) value;
    }

    /*
     * Get sub json by fields ( field1 = value1, field2 = value2 ) of record
     * Be careful: include `null`
     */
    @Override
    public JsonObject get(final String... fields) {
        final JsonObject json = new JsonObject();
        Arrays.stream(fields).forEach(each -> json.put(each, this.data.getValue(each)));
        return json;
    }

    @Override
    public ConcurrentMap<String, Class<?>> types() {
        /*
         * Wait for sub class overwrite
         */
        return new ConcurrentHashMap<>();
    }

    /*
     * The same as `toJson()`, return to all data of current record
     */
    @Override
    public JsonObject get() {
        return this.toJson();
    }

    /*
     * Set single pair `field = value`
     */
    @Override
    public <V> HRecord set(final String field, final V value) {
        if (this.field().contains(field)) {
            final Class<?> type = this.types().get(field);
            this.data.put(field, Ut.aiJValue(value, type));
        } else {
            this.getLogger().debug("The field `{0}` has not been defined in model: `{1}`",
                field, this.identifier());
        }
        return this;
    }

    @Override
    public <V> HRecord attach(final String field, final V value) {
        this.data.put(field, value);
        return this;
    }

    /*
     * Set all data from `JsonObject`
     */
    @Override
    public HRecord set(final JsonObject data) {
        if (!Ut.isNil(data)) {
            data.stream().filter(Objects::nonNull)
                .forEach(entry -> this.set(entry.getKey(), entry.getValue()));
        }
        return this;
    }

    /*
     * Add `field = value` when the original data value of field is `null`
     */
    @Override
    public <V> HRecord add(final String field, final V value) {
        if (null == this.data.getValue(field)) {
            this.set(field, value);
        }
        return this;
    }

    /*
     * Add `JsonObject` with add
     */
    @Override
    public HRecord add(final JsonObject data) {
        if (Ut.isNotNil(data)) {
            data.stream().filter(Objects::nonNull)
                .forEach(entry -> this.add(entry.getKey(), entry.getValue()));
        }
        return this;
    }

    /*
     * Remove by `field`
     */
    @Override
    public HRecord remove(final String field) {
        if (this.data.containsKey(field)) {
            this.data.remove(field);
        }
        return this;
    }

    /*
     * Remove by `fields`
     */
    @Override
    public HRecord remove(final String... fields) {
        Arrays.stream(fields).forEach(this::remove);
        return this;
    }

    // -------------- Data Clone ---------------
    /*
     * Clone subset of current `record`
     */
    @Override
    public HRecord createSubset(final String... fields) {
        /*
         * Call createNew() record here, you must set new record created instead of other
         * method. Different record has different creation methods.
         * */
        final HRecord record = this.createNew();
        Arrays.stream(fields).forEach(field -> record.set(field, this.get(field)));
        return record;
    }

    /*
     * Clone current record
     */
    @Override
    public HRecord createCopy() {
        final HRecord record = this.createNew();
        record.set(this.data);
        return record;
    }

    // --------------- Json interface ------------
    /*
     * It does not contain `null` value here.
     */
    @Override
    public JsonObject toJson() {
        final JsonObject json = this.data.copy();
        this.data.fieldNames().stream()
            .filter(field -> Objects.isNull(json.getValue(field)))
            .forEach(json::remove);
        return json;
    }

    @Override
    public void fromJson(final JsonObject json) {
        if (Ut.isNotNil(json)) {
            this.data.mergeIn(json);
        }
    }

    // ---------------- Meta Data ----------------
    /*
     * Get data size
     */
    @Override
    public int size() {
        return this.data.size();
    }

    /*
     * Get data field names
     */
    @Override
    public Set<String> fieldUse() {
        return this.data.fieldNames();
    }

    // ---------------- Optional Overwrite ------
    @Override
    public Set<String> joins() {
        return new HashSet<>();
    }
}
