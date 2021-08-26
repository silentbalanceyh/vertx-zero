package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import io.vertx.up.util.Ut;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * # 「Tp」Jackson Deserializer
 *
 * This serializer inherit from `LocalDateTimeDeserializer` for the calculation of date time
 * with TimeZone. When you set the date time to java object, here are standard of datetime, the
 * default format is UTC.
 *
 * This component is used by {@link com.fasterxml.jackson.databind.module.ZeroModule} as following:
 *
 * ```java
 * // <pre><code class="java">
 *      this.addDeserializer(LocalDateTime.class, new AdjustDateTimeDeserializer());
 * // </code></pre>
 * ```
 *
 * ## Designed
 *
 * After `java 8`, it provide `LocalDateTime, LocalDate, LocalTime` for datetime calculation, it means that you can
 * use previous three datetime classes instead of `Date` or `Calendar` here. These api are more useful when you are in
 * development of some real business project.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AdjustDateTimeDeserializer extends LocalDateTimeDeserializer {

    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public AdjustDateTimeDeserializer() {
        super(DEFAULT_FORMATTER);
    }

    @Override
    public LocalDateTime deserialize(final JsonParser parser, final DeserializationContext context) throws IOException {
        if (parser.hasTokenId(6)) {
            final String string = parser.getText().trim();
            if (string.length() == 0) {
                return null;
            } else {
                try {
                    if (this._formatter == DEFAULT_FORMATTER && string.length() > 10 && string.charAt(10) == 'T' ) {
                        /// System.out.println(string.endsWith("Z"));
                        return string.endsWith("Z") ? Ut.toDateTime(Ut.parse(string)) :
                            LocalDateTime.parse(string, DEFAULT_FORMATTER);
                    } else {
                        return LocalDateTime.parse(string, this._formatter);
                    }
                } catch (final DateTimeException var12) {
                    return (LocalDateTime) this._handleDateTimeException(context, var12, string);
                }
            }
        } else {
            if (parser.isExpectedStartArrayToken()) {
                JsonToken t = parser.nextToken();
                if (t == JsonToken.END_ARRAY) {
                    return null;
                }

                final LocalDateTime result;
                if ((t == JsonToken.VALUE_STRING || t == JsonToken.VALUE_EMBEDDED_OBJECT) && context.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
                    result = this.deserialize(parser, context);
                    if (parser.nextToken() != JsonToken.END_ARRAY) {
                        this.handleMissingEndArrayForSingle(parser, context);
                    }

                    return result;
                }

                if (t == JsonToken.VALUE_NUMBER_INT) {
                    final int year = parser.getIntValue();
                    final int month = parser.nextIntValue(-1);
                    final int day = parser.nextIntValue(-1);
                    final int hour = parser.nextIntValue(-1);
                    final int minute = parser.nextIntValue(-1);
                    t = parser.nextToken();
                    if (t == JsonToken.END_ARRAY) {
                        result = LocalDateTime.of(year, month, day, hour, minute);
                    } else {
                        final int second = parser.getIntValue();
                        t = parser.nextToken();
                        if (t == JsonToken.END_ARRAY) {
                            result = LocalDateTime.of(year, month, day, hour, minute, second);
                        } else {
                            int partialSecond = parser.getIntValue();
                            if (partialSecond < 1000 && !context.isEnabled(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS)) {
                                partialSecond *= 1000000;
                            }

                            if (parser.nextToken() != JsonToken.END_ARRAY) {
                                throw context.wrongTokenException(parser, this.handledType(), JsonToken.END_ARRAY, "Expected array to end");
                            }

                            result = LocalDateTime.of(year, month, day, hour, minute, second, partialSecond);
                        }
                    }

                    return result;
                }

                context.reportInputMismatch(this.handledType(), "Unexpected token (%s) within Array, expected VALUE_NUMBER_INT", t);
            }

            if (parser.hasToken(JsonToken.VALUE_EMBEDDED_OBJECT)) {
                return (LocalDateTime) parser.getEmbeddedObject();
            } else {
                if (parser.hasToken(JsonToken.VALUE_NUMBER_INT)) {
                    this._throwNoNumericTimestampNeedTimeZone(parser, context);
                }

                return (LocalDateTime) this._handleUnexpectedToken(context, parser, "Expected array or string.", new Object[0]);
            }
        }
    }
}
