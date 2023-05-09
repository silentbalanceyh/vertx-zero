package io.horizon.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.OriginalNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.HorizonModule;
import io.horizon.eon.VString;
import io.horizon.fn.HFn;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lang : 2023-05-09
 */
@SuppressWarnings("all")
class HJackson {

    private static final JsonMapper MAPPER = JsonMapper.builder()
        /*
         * Previous code
         * JsonMapper.builder().configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
         * JsonMapper.builder().configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true);
         * MAPPER.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
         * MAPPER.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true);
         *
         * Case Sensitive
         * Below new code logical
         */
        .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
        .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true)
        .build();

    static {
        // Ignore null value
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // Non-standard JSON but we allow C style comments in our JSON
        MAPPER.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // Big Decimal
        MAPPER.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);
        // Case Sensitive
        final HorizonModule module = new HorizonModule();
        MAPPER.registerModule(module);
        MAPPER.setPropertyNamingStrategy(OriginalNamingStrategy.JOOQ_NAME);
    }

    static JsonMapper mapper() {
        return MAPPER.copy();
    }

    static <T> String serialize(final T t) {
        return HFn.runOr(null, () -> HFn.failOr(() -> MAPPER.writeValueAsString(t), t), t);
    }

    static <T, R extends Iterable> R serializeJson(final T t) {
        final String content = serialize(t);
        return HFn.failOr(null, () -> HFn.runOr(content.trim().startsWith(VString.LEFT_BRACE), null,
            /*
             * Switch to smart serialization on the object to avoid
             * issue when met {} or []
             * 递归调用
             */
            () -> (R) new JsonObject(content),
            () -> ((R) new JsonArray(content))
        ), content);
    }

    static <T> T deserialize(final JsonObject value, final Class<T> type) {
        return HFn.runOr(null,
            () -> deserialize(value.encode(), type), value);
    }

    static <T> T deserialize(final JsonArray value, final Class<T> type) {
        return HFn.runOr(null,
            () -> deserialize(value.encode(), type), value);
    }

    static <T> List<T> deserialize(final JsonArray value, final TypeReference<List<T>> type) {
        return HFn.runOr(new ArrayList<>(),
            () -> deserialize(value.encode(), type), value);
    }

    static <T> T deserialize(final String value, final Class<T> type) {
        return HFn.runOr(null,
            () -> HFn.failOr(() -> MAPPER.readValue(value, type)), value);
    }

    static <T> T deserialize(final String value, final TypeReference<T> type) {
        // Turn Off Smart Json when TypeReference<T>
        // final String smart = deserializeSmart(value, (Class<T>) type.getType());
        return HFn.runOr(null,
            () -> HFn.failOr(() -> MAPPER.readValue(value, type)), value);
    }

}
