package io.aeon.experiment.mixture;

import io.aeon.experiment.mu.KTag;
import io.vertx.up.eon.em.DataFormat;

import java.util.List;

/**
 * ## Attribute Interface
 *
 * ### 1. Intro
 *
 * Here are the parsing workflow:
 *
 * 1. When <strong>isArray</strong> = true, the dataFormat is `JsonArray`.
 * 2. Otherwise, extract `type` field of `sourceConfig` to get actual dataFormat.
 *
 * ### 2. Limitation
 *
 * #### 2.1. JsonArray
 *
 * 1. INTERNAL: Must contains `fields` configuration for sub-elements.
 * 2. REFERENCE/EXTERNAL: No.
 * 3. The `type` is {@link io.vertx.core.json.JsonArray};
 *
 * #### 2.2. JsonObject
 *
 * 1. INTERNAL: Must contains `fields` configuration for sub-elements.
 * 2. REFERENCE/EXTERNAL: No.
 * 3. The `type` is {@link io.vertx.core.json.JsonObject};
 *
 * #### 2.3. Elementary
 *
 * 1. The config must contains `type` field to define java class type name.
 * 2. It's default value here. `type = {@link java.lang.String}`, `source = Elementary`.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HAttribute {
    /**
     * Return to the `rule` that is related to current attribute
     *
     * @return {@link HRule}
     */
    HRule refRule();

    KTag tag();

    /**
     * - JsonArray: []
     * - JsonObject: {}
     * - Elementary: Default ( Simple Type )
     *
     * @return {@link DataFormat}
     */
    DataFormat format();
    // -------------------- Type Information of current attribute

    /**
     * The type of current attribute here
     *
     * @return {@link HTField}
     */
    HTField field();

    /**
     * When current attribute is
     * - JsonArray
     * - JsonObject
     *
     * @return {@link List}
     */
    List<HTField> fields();
}
