package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.core.JsonGenerator;
import io.vertx.up.atom.unity.UObject;

import java.io.IOException;

/**
 * # 「Tp」Jackson Serializer
 *
 * Blade is a game of `sword` processing, it's for {@link UObject} data structure serialization.
 * * Uson is defined by Zero Framework and it contains following features:
 * *
 * * * Stream Api for Json processing.
 * * * Fluent Api that are mocked as Vert.x native.
 * *
 * * This deserializer is for {@link UObject} serialization if needed, the internal Json data object
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
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class BladeSerializer extends JsonSerializer<UObject> {
    @Override
    public void serialize(final UObject blade,
                          final JsonGenerator jgen,
                          final SerializerProvider provider) throws IOException {
        jgen.writeObject(blade.to().getMap());
    }
}
