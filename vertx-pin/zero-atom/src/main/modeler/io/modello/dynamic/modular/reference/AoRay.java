package io.modello.dynamic.modular.reference;

import io.modello.specification.HRecord;
import io.vertx.core.Future;
import io.vertx.mod.atom.modeling.element.DataTpl;

/**
 * ## Reference Processor ( Ray )
 *
 * ### 1. Intro
 *
 * The field of `reference` calculation components, after all the data have been read, these components could process `reference` field based on the records.
 *
 * Here are three categories of attribute's type:
 *
 * 1. INTERNAL : The standard attribute that has been mapped to database.
 * 2. REFERENCE : The reference attribute that has been mapped to other dependency attributes.
 * 3. EXTERNAL : The reserved attribute that has been mapped to third-part system.
 *
 * ### 2. Generic T
 *
 * The `T` type should be two common object such as:
 *
 * - {@link io.vertx.core.json.JsonObject} content of Json Record, the core type is {@link HRecord}.
 * - {@link io.vertx.core.json.JsonArray} content of Json Record[], the core type is {@link HRecord}[].
 *
 * ### 3. Standard Mode
 *
 * #### 3.1. INTERNAL
 *
 * This kind of attribute support following features:
 *
 * - It should be mapped to `X_ENTITY` fields instead of storing in other place.
 * - The data type must be based on database column type.
 * - `X_ENTITY` column type has been defined in abstract virtual layer to support <strong>Type Mapping</strong>.
 *
 * #### 3.2. REFERENCE
 *
 * This kind of attribute support following features:
 *
 * 1. Complex Type: Such as `up` and `down`, the data format is {@link io.vertx.core.json.JsonObject}/{@link io.vertx.core.json.JsonArray}, calculated to the result here.
 * 2. Dependency Type: The type is calculated by other `INTERNAL` field.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface AoRay<T> {
    /**
     * Bind the component to data model template {@link io.vertx.mod.atom.modeling.element.DataTpl}.
     *
     * @param tpl {@link io.vertx.mod.atom.modeling.element.DataTpl} The template that will be bind.
     *
     * @return {@link AoRay} The component reference
     */
    AoRay<T> on(DataTpl tpl);

    /**
     * This method will modify the input {@link HRecord} element(s).
     *
     * @param input Input element of {@link HRecord} for single/multi
     *
     * @return Return the modified data record(s).
     */
    T doRay(T input);

    Future<T> doRayAsync(final T input);
}
