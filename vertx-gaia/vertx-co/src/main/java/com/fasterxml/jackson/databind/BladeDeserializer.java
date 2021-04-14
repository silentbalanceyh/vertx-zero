package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.core.JsonParser;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.unity.Uson;

import java.io.IOException;

/**
 * # 「Tp」Jackson Deserializer
 *
 * Blade is a game of `sword` processing, it's for {@link io.vertx.up.atom.unity.Uson} data structure serialization.
 * Uson is defined by Zero Framework and it contains following features:
 *
 * * Stream Api for Json processing.
 * * Fluent Api that are mocked as Vert.x native.
 *
 * This deserializer is for {@link io.vertx.up.atom.unity.Uson} serialization if needed, the internal Json data object
 * will be deserialize instead of itself.
 *
 * This component is used by {@link com.fasterxml.jackson.databind.module.ZeroModule} as following:
 *
 * ```java
 * // <pre><code class="java">
 *       this.addDeserializer(Uson.class, new BladeDeserializer());
 * // </code></pre>
 * ```
 *
 * Here are `Uson` usage segment:
 *
 * ```java
 * // <pre><code class="java">
 *
 *      // Before App Initialized ( Public Api ), Stream api ( Fluent ) here about Uson usage
 *      .compose(appData -> Uson.create(appData).remove(KeField.APP_KEY).toFuture())
 * // </code></pre>
 * ```
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class BladeDeserializer extends JsonDeserializer<Uson> { // NOPMD

    @Override
    public Uson deserialize(final JsonParser parser,
                            final DeserializationContext context)
            throws IOException {
        final JsonNode node = parser.getCodec().readTree(parser);
        return Uson.create(new JsonObject(node.toString()));
    }
}
