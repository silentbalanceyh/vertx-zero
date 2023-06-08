package io.vertx.up.commune.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ClassDeserializer;
import com.fasterxml.jackson.databind.ClassSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.horizon.uca.log.Annal;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * ## Atom Part
 * Pojo metadata container
 * Meta of Java Object
 * Here are definition for HAtom upgrade.
 * The Yaml Data Structure is as following:
 * // <pre><code class="yaml">
 *     type:                                    # POJO Type of Jooq Generated
 *     mapping:                                 # Mapping from entity -> field
 *         pojoField: jsonField
 * // </code></pre>
 */
public class Mojo implements Serializable {

    private static final Annal LOGGER = Annal.get(Mojo.class);
    @JsonIgnore
    private final ConcurrentMap<String, String> columns = new ConcurrentHashMap<>();
    @JsonIgnore
    private String pojoFile;
    @JsonProperty(KName.TYPE)
    @JsonSerialize(using = ClassSerializer.class)
    @JsonDeserialize(using = ClassDeserializer.class)
    private Class<?> type;
    @JsonProperty(KName.MAPPING)
    private ConcurrentMap<String, String> mapping = new ConcurrentHashMap<>();

    public Class<?> getType() {
        return this.type;
    }

    public void setType(final Class<?> type) {
        this.type = type;
    }

    public Mojo on(final String pojoFile) {
        this.pojoFile = pojoFile;
        return this;
    }

    public String getPojoFile() {
        return this.pojoFile;
    }

    /*
     * field -> outField
     * 1) field is declared in `Pojo` class
     * 2) outField is not declared but often is provided in input `Json`
     *
     * Example:
     *
     * zSigma -> sigma
     */
    public ConcurrentMap<String, String> getOut() {
        // Fix no mapping issue for empty mapping conversion.
        Fn.runAt(null == this.mapping, LOGGER, () -> this.mapping = new ConcurrentHashMap<>());
        return this.mapping;
    }

    public String getOut(final String key) {
        return this.getOut().getOrDefault(key, null);
    }

    /*
     * outField -> field
     * Reverted into `getOut`, it does not store
     *
     * Example:
     *
     * sigma -> zSigma
     */
    @SuppressWarnings("all")
    public ConcurrentMap<String, String> getIn() {
        Fn.runAt(mapping.keySet().size() != mapping.values().size(), LOGGER,
            () -> LOGGER.warn(Info.VALUE_SAME,
                mapping.keySet().size(), mapping.values().size()));
        final ConcurrentMap<String, String> mapper =
            new ConcurrentHashMap<>();
        mapping.forEach((key, value) -> mapper.put(value, key));
        return mapper;
    }

    public String getIn(final String key) {
        return this.getIn().getOrDefault(key, null);
    }

    /*
     * It's for input
     * Column -> zSigma
     */
    public ConcurrentMap<String, String> getInColumn() {
        return this.columns;
    }

    /*
     * It's for output
     * Column -> sigma
     */
    public ConcurrentMap<String, String> getOutColumn() {
        final ConcurrentMap<String, String> revert = new ConcurrentHashMap<>();
        if (!this.columns.isEmpty()) {
            final ConcurrentMap<String, String> fieldMap = this.getIn();
            /*
             * Actual Field -> Column
             */
            this.columns.forEach((key, value) -> {
                final String outField = fieldMap.get(key);
                if (Objects.nonNull(outField)) {
                    revert.put(value, outField);
                }
            });
        }
        return revert;
    }

    /*
     * Replace column mapping here, this method must be called
     * or
     * this.columns are invalid
     */
    public Mojo bindColumn(final ConcurrentMap<String, String> columns) {
        if (null != columns && !columns.isEmpty()) {
            this.columns.putAll(columns);
        }
        return this;
    }

    public Mojo bind(final Mojo mojo) {
        this.type = mojo.type;
        this.mapping.putAll(mojo.mapping);
        return this;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Mojo)) {
            return false;
        }
        final Mojo mojo = (Mojo) o;
        return Objects.equals(this.type, mojo.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.type);
    }

    @Override
    public String toString() {
        /*
         *
         */
        final StringBuilder report = new StringBuilder();
        report.append("==> Column: \n");
        this.columns.forEach((column, field) -> report
            .append(column).append('=').append(field).append('\n'));
        /*
         *
         */
        report.append("==> Pojo: \n");
        this.mapping.forEach((actual, input) -> report
            .append(actual).append('=').append(input).append('\n'));
        return report.toString();
    }
}
