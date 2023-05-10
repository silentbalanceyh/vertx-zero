package io.vertx.up.commune.secure;

import io.horizon.eon.VString;
import io.horizon.eon.VValue;
import io.horizon.uca.qr.syntax.Ir;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashSet;
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
    // 此处必须有序，有序才可执行查询
    private final Set<String> projection = new LinkedHashSet<>();
    private final JsonObject criteria = new JsonObject();
    private final JsonArray credit = new JsonArray();
    private final ConcurrentMap<String, Set<String>> rows =
        new ConcurrentHashMap<>();

    private final JsonObject seeker = new JsonObject();
    private final JsonObject viewData = new JsonObject();

    /*
     * {
     *     "projection":        "S_VIEW -> PROJECTION",
     *     "criteria":          "S_VIEW -> CRITERIA",
     *     "rows":              "S_VIEW -> ROWS",
     *     "credit":            "S_VIEW -> RENEW_CREDIT",
     *     "seeker":            {
     *         "config":        "S_RESOURCE -> SEEK_CONFIG",
     *         "component":     "S_RESOURCE -> SEEK_COMPONENT",
     *         "syntax":        "S_RESOURCE -> SEEK_SYNTAX"
     *     },
     *     "view":              {
     *         "S_VIEW 全字段"
     *     }
     * }
     */
    public JsonObject toJson() {
        final JsonObject json = new JsonObject();
        json.put(Ir.KEY_PROJECTION, Ut.toJArray(this.projection));
        json.put(Ir.KEY_CRITERIA, this.criteria);


        /* Rows */
        final JsonObject rows = new JsonObject();
        Ut.itMap(this.rows, (field, rowSet) -> rows.put(field, Ut.toJArray(rowSet)));
        json.put(KName.Rbac.ROWS, rows);


        /* Advanced, Impact, Seeker */
        json.put(KName.Rbac.CREDIT, credit);
        if (Ut.isNotNil(this.seeker)) {
            json.put(KName.SEEKER, this.seeker);
            json.put(KName.VIEW, this.viewData);
        }
        return json;
    }

    public DataBound addSeeker(final JsonObject seeker) {
        this.seeker.mergeIn(seeker);
        return this;
    }

    public DataBound addView(final JsonObject view) {
        this.viewData.mergeIn(view);
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
            .filter(splitted -> VValue.TWO == splitted.length)
            .map(splitted -> new JsonObject()
                .put(KName.METHOD, splitted[0])
                .put(KName.URI, splitted[1])
            ).forEach(this.credit::add);
        return this;
    }

    /*
     * Criteria modification: complex logical to merge criteria
     */
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
                this.criteria.put(VString.EMPTY, Boolean.FALSE);
                final String key = "$" + this.criteria.size();
                this.criteria.put(key, criteria);
            }
        }
        return this;
    }

    public JsonArray vProjection() {
        return Ut.toJArray(this.projection);
    }
}
