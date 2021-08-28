package feign.codec;

import feign.FeignException;
import feign.Response;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.io.IOException;
import java.lang.reflect.Type;

public class JsonObjectDecoder implements Decoder {
    @Override
    public Object decode(final Response response, final Type type)
        throws IOException, FeignException {
        if (JsonObject.class == type) {
            final String content = Ut.ioString(response.body().asInputStream());
            return new JsonObject(content);
        }
        return null;
    }
}
