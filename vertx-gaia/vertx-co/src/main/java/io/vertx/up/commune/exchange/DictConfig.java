package io.vertx.up.commune.exchange;

import io.vertx.core.json.JsonArray;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*
 * 1) Tabular related data
 * 2）Assist related data
 * 3）Category related data
 * Configuration for three critical data to define rule here.
 * Rules definition here ( Multi rule definition )
 * [
 *     {
 *         "source":"CATEGORY / TABULAR / ASSIST",
 *         "types": [
 *              "xxx": "xxx"
 *         ],
 *         "key": "assistKey",
 *         "sourceComponent": "class",
 *         "filters": {
 *         }
 *     }
 * ]
 */
public class DictConfig implements Serializable {

    /*
     * Source definition here for directory configuration
     */
    private final transient List<DictSource> source = new ArrayList<>();
    private final transient ConcurrentMap<String, DictEpsilon> epsilon = new ConcurrentHashMap<>();
    private transient Class<?> component;

    /*
     * Build object of Dict
     */
    public DictConfig(final String literal) {
        if (Ut.isJArray(literal)) {
            final JsonArray parameters = new JsonArray(literal);
            /* Initialize */
            this.init(parameters);
        }
    }

    public DictConfig(final JsonArray input) {
        if (Objects.nonNull(input)) {
            /* Initialize */
            this.init(input);
        }
    }

    private void init(final JsonArray input) {
        /* Normalize `DictSource` List */
        Ut.itJArray(input)
                .map(DictSource::new)
                .forEach(this.source::add);
    }

    public DictConfig bind(final Class<?> component) {
        if (Objects.isNull(component)) {
            /*
             * When component not found,
             * clear source data cache to empty list.
             * It's force action here to clear source instead of others
             * 1) If you don't bind Class<?> component, the source will be cleared
             * 2) If you want to bind Class<?> component, it means that all the inited dict
             * will be impact
             */
            this.source.clear();
            this.epsilon.clear();
        } else {
            this.component = component;
        }
        return this;
    }

    public DictConfig bind(final ConcurrentMap<String, DictEpsilon> epsilon) {
        if (Objects.nonNull(epsilon)) {
            this.epsilon.putAll(epsilon);
        }
        return this;
    }

    public Class<?> getComponent() {
        return this.component;
    }

    public ConcurrentMap<String, DictEpsilon> getEpsilon() {
        return this.epsilon;
    }

    public boolean validSource() {
        return !this.source.isEmpty();
    }

    public boolean valid() {
        /*
         * When current source is not empty,
         * The `dictComponent` is required here.
         */
        return this.validSource() && Objects.nonNull(this.component);
    }

    public List<DictSource> getSource() {
        return this.source;
    }
}
