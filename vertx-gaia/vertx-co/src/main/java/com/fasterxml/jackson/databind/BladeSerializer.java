package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.core.JsonGenerator;
import io.vertx.up.atom.unity.Uson;

import java.io.IOException;

/**
 * # 「Tp」Jackson Serializer
 *
 * Blade is a game of `sword` processing, it's for {@link io.vertx.up.atom.unity.Uson} data structure serialization.
 * * Uson is defined by Zero Framework and it contains following features:
 * *
 * * * Stream Api for Json processing.
 * * * Fluent Api that are mocked as Vert.x native.
 * *
 * * This deserializer is for {@link io.vertx.up.atom.unity.Uson} serialization if needed, the internal Json data object
 * * will be deserialize instead of itself.
 * *
 * * This component is used by {@link com.fasterxml.jackson.databind.module.ZeroModule} as following:
 * *
 * * ```java
 * * // <pre><code class="java">
 *  *       this.addSerializer(Uson.class, new BladeSerializer());
 *  * // </code></pre>
 * * ```
 *
 * @author Lang
 */
public class BladeSerializer extends JsonSerializer<Uson> {
    @Override
    public void serialize(final Uson blade,
                          final JsonGenerator jgen,
                          final SerializerProvider provider) throws IOException {
        jgen.writeObject(blade.to().getMap());
    }
}
