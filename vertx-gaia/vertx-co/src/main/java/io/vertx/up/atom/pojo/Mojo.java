package io.vertx.up.atom.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ClassDeserializer;
import com.fasterxml.jackson.databind.ClassSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*
 * Pojo metadata container
 * Meta of Java Object
 */
public class Mojo implements Serializable {

    private static final Annal LOGGER = Annal.get(Mojo.class);
    private static final String TYPE = "type";
    private static final String MAPPING = "mapping";
    @JsonIgnore
    private final ConcurrentMap<String, String> columns = new ConcurrentHashMap<>();
    @JsonIgnore
    private transient String pojoFile;
    @JsonProperty(TYPE)
    @JsonSerialize(using = ClassSerializer.class)
    @JsonDeserialize(using = ClassDeserializer.class)
    private Class<?> type;
    @JsonProperty(MAPPING)
    private ConcurrentMap<String, String> config = new ConcurrentHashMap<>();

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
        Fn.safeSemi(null == this.config, LOGGER, () -> this.config = new ConcurrentHashMap<>());
        return this.config;
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
        Fn.safeSemi(config.keySet().size() != config.values().size(), LOGGER,
            () -> LOGGER.warn(Info.VALUE_SAME,
                config.keySet().size(), config.values().size()));
        final ConcurrentMap<String, String> mapper =
            new ConcurrentHashMap<>();
        config.forEach((key, value) -> mapper.put(value, key));
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
        this.config.putAll(mojo.config);
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
            .append(column).append('=' ).append(field).append('\n' ));
        /*
         *
         */
        report.append("==> Pojo: \n");
        this.config.forEach((actual, input) -> report
            .append(actual).append('=' ).append(input).append('\n' ));
        return report.toString();
    }
}
