package io.horizon.eon.em.typed;

/**
 * ## For Complex Usage.
 *
 * ### 1. Type Defined
 *
 * 1. JsonArray: The final format is [].
 * 2. JsonObject: The final format is {}.
 * 3. Elementary: The final format is other.
 *
 * ### 2. Code Sample
 *
 * ```json
 * // <pre><code class="json">
 *     {
 *         "source": "JsonArray | JsonObject | Elementary",
 *         "fields": []
 *     }
 * // </code></pre>
 * ```
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public enum DataFormat {
    /**
     * Common Json Array format.
     */
    JsonArray,
    /**
     * Common Json Object format.
     */
    JsonObject,
    /**
     * Elementary data format.
     */
    Elementary,
}
