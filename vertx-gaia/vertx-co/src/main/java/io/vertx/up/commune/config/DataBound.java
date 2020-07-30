package io.vertx.up.commune.config;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.Values;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*
 * Data Bound feature for result, DTO to store metadata information here.
 * Pre-Condition
 * 1. criteria:
 *    Only available in query engine enabled here.
 *
 * Post-Condition
 * 1. projection:
 *    - []: No limitation
 *    - [xxx,xxx]: Filter the data result for columns are in projection
 * 2. rows:
 *    - { "field": [xxx] }
 *    Process field filters, all the fields must be in JsonArray
 */
@SuppressWarnings("all")
public class DataBound implements Serializable {

    private final transient Set<String> projection = new HashSet<>();
    private final transient JsonObject criteria = new JsonObject();
    private final transient JsonArray credit = new JsonArray();
    private final transient ConcurrentMap<String, Set<String>> rows =
            new ConcurrentHashMap<>();

    /*
     * Visitant Resource data structure for data bound
     * The visitant resource must be unique in our environment
     */
    private final transient JsonObject view = new JsonObject();

    public JsonObject toJson() {
        final JsonObject json = new JsonObject();
        json.put("projection", Ut.toJArray(this.projection));
        json.put("criteria", this.criteria);
        /* Rows */
        final JsonObject rows = new JsonObject();
        Ut.itMap(this.rows, (field, rowSet) -> rows.put(field, Ut.toJArray(rowSet)));
        json.put("rows", rows);
        json.put("credit", credit);
        /* View */
        if (Ut.notNil(view)) {
            json.put("view", view);
        }
        return json;
    }

    /*
     * Add view data into DataBound for visitant usage in future
     */
    public DataBound addView(final JsonObject view) {
        if (Ut.notNil(view)) {
            this.view.mergeIn(view);
        }
        return this;
    }

    /*
     * Projection modification: projection field here.
     */
    public DataBound addProjection(final String projection) {
        final JsonArray array = Ut.toJArray(projection);
        array.stream().filter(Objects::nonNull)
                .map(item -> (String) item)
                .forEach(this.projection::add);
        return this;
    }

    /*
     * Row modification: rows object.
     */
    public DataBound addRows(final String rows) {
        final JsonObject rowJson = Ut.toJObject(rows);
        Ut.<JsonArray>itJObject(rowJson, (array, key) -> {
            final Set<String> set = new HashSet<>(array.getList());
            final Set<String> origianl = this.rows.get(key);
            if (null == origianl || origianl.isEmpty()) {
                this.rows.put(key, set);
            } else {
                origianl.addAll(set);
                /* Replace old */
                this.rows.put(key, origianl);
            }
        });
        return this;
    }

    /*
     * Specification data flow for credit refresh about impact
     */
    public DataBound addCredit(final String credit) {
        final JsonArray array = Ut.toJArray(credit);
        array.stream().filter(Objects::nonNull)
                .map(item -> (String) item)
                .map(literal -> literal.split(" "))
                .filter(splitted -> Values.TWO == splitted.length)
                .map(splitted -> new JsonObject()
                        .put("method", splitted[0])
                        .put("uri", splitted[1])
                ).forEach(this.credit::add);
        return this;
    }

    /*
     * Criteria modification: complex logical to merge criteria
     */
    // TODO: Merge Criteria
    public DataBound addCriteria(final String criteria) {
        final JsonObject criteriaJson = Ut.toJObject(criteria);
        if (!criteriaJson.isEmpty()) {
            /*
             * Contains value here.
             */
            if (this.criteria.isEmpty()) {
                /*
                 * No data here.
                 */
                this.criteria.mergeIn(criteriaJson);
            } else {
                /*
                 * Existing data here.
                 */
                this.criteria.put(Strings.EMPTY, Boolean.FALSE);
                final String key = "$" + this.criteria.size();
                this.criteria.put(key, criteria);
            }
        }
        return this;
    }
}
