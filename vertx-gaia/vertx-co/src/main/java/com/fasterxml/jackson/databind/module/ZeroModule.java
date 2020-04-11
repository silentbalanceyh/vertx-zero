package com.fasterxml.jackson.databind.module;

import com.fasterxml.jackson.core.json.PackageVersion;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.ValueInstantiators;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedClassResolver;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.*;
import com.fasterxml.jackson.datatype.jsr310.deser.key.*;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.*;
import com.fasterxml.jackson.datatype.jsr310.ser.key.ZonedDateTimeKeySerializer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.unity.Uson;

import java.time.*;
import java.util.Iterator;

/**
 * # 「Tp」Jackson Extension
 *
 * This is internal module that be used by Zero Framework. Here I added more custom `serializer/deserializer`
 * for follow types:
 *
 * * JsonObject
 * * JsonArray
 * * Blade
 * * Class
 *
 * Here will ignore method description because it's inherited from jackson `SimpleModule`. Here are three
 * major `serializer/deserializer` for different situations.
 *
 * ## Usage
 *
 * This module will be used to build internal `mapper` of jackson for based data type architecture. Please refer
 * following code example to see how to use:
 *
 * ```java
 * // <pre><code class="java">
 *
 * private static final ObjectMapper MAPPER = new ObjectMapper();
 *
 * static{
 *      final ZeroModule module = new ZeroModule();
 *      Jackson.MAPPER.registerModule(module);
 *      Jackson.MAPPER.setPropertyNamingStrategy(OrignialNamingStrategy.JOOQ_NAME);
 * }
 *
 * // </code></pre>
 * ```
 *
 * Based on above code segments, you can registry new module instead of attributes settings for jackson serialization.
 *
 * @author lang
 */
public class ZeroModule extends SimpleModule {
    private static final long serialVersionUID = 1L;

    public ZeroModule() {
        super(PackageVersion.VERSION);
        // Serializer
        this.addSerializer(JsonObject.class, new JsonObjectSerializer());
        this.addSerializer(JsonArray.class, new JsonArraySerializer());
        this.addSerializer(Uson.class, new BladeSerializer());
        this.addSerializer(byte[].class, new ByteArraySerializer());
        // Deserializer
        this.addDeserializer(JsonObject.class, new JsonObjectDeserializer());
        this.addDeserializer(JsonArray.class, new JsonArrayDeserializer());
        this.addDeserializer(Uson.class, new BladeDeserializer());
        // Default Time
        this.addDeserializer(Instant.class, InstantDeserializer.INSTANT);
        this.addDeserializer(OffsetDateTime.class, InstantDeserializer.OFFSET_DATE_TIME);
        this.addDeserializer(ZonedDateTime.class, InstantDeserializer.ZONED_DATE_TIME);
        this.addDeserializer(Duration.class, DurationDeserializer.INSTANCE);
        // Fix Date Time issue
        this.addDeserializer(LocalDateTime.class, new AdjustDateTimeDeserializer());
        this.addDeserializer(LocalDate.class, LocalDateDeserializer.INSTANCE);
        this.addDeserializer(LocalTime.class, LocalTimeDeserializer.INSTANCE);
        this.addDeserializer(MonthDay.class, MonthDayDeserializer.INSTANCE);
        this.addDeserializer(OffsetTime.class, OffsetTimeDeserializer.INSTANCE);
        this.addDeserializer(Period.class, JSR310StringParsableDeserializer.PERIOD);
        this.addDeserializer(Year.class, YearDeserializer.INSTANCE);
        this.addDeserializer(YearMonth.class, YearMonthDeserializer.INSTANCE);
        this.addDeserializer(ZoneId.class, JSR310StringParsableDeserializer.ZONE_ID);
        this.addDeserializer(ZoneOffset.class, JSR310StringParsableDeserializer.ZONE_OFFSET);
        this.addSerializer(Duration.class, DurationSerializer.INSTANCE);
        this.addSerializer(Instant.class, InstantSerializer.INSTANCE);
        this.addSerializer(LocalDateTime.class, LocalDateTimeSerializer.INSTANCE);
        this.addSerializer(LocalDate.class, LocalDateSerializer.INSTANCE);
        this.addSerializer(LocalTime.class, LocalTimeSerializer.INSTANCE);
        this.addSerializer(MonthDay.class, MonthDaySerializer.INSTANCE);
        this.addSerializer(OffsetDateTime.class, OffsetDateTimeSerializer.INSTANCE);
        this.addSerializer(OffsetTime.class, OffsetTimeSerializer.INSTANCE);
        this.addSerializer(Period.class, new ToStringSerializer(Period.class));
        this.addSerializer(Year.class, YearSerializer.INSTANCE);
        this.addSerializer(YearMonth.class, YearMonthSerializer.INSTANCE);
        this.addSerializer(ZonedDateTime.class, ZonedDateTimeSerializer.INSTANCE);
        this.addSerializer(ZoneId.class, new ToStringSerializer(ZoneId.class));
        this.addSerializer(ZoneOffset.class, new ToStringSerializer(ZoneOffset.class));
        this.addKeySerializer(ZonedDateTime.class, ZonedDateTimeKeySerializer.INSTANCE);
        this.addKeyDeserializer(Duration.class, DurationKeyDeserializer.INSTANCE);
        this.addKeyDeserializer(Instant.class, InstantKeyDeserializer.INSTANCE);
        this.addKeyDeserializer(LocalDateTime.class, LocalDateTimeKeyDeserializer.INSTANCE);
        this.addKeyDeserializer(LocalDate.class, LocalDateKeyDeserializer.INSTANCE);
        this.addKeyDeserializer(LocalTime.class, LocalTimeKeyDeserializer.INSTANCE);
        this.addKeyDeserializer(MonthDay.class, MonthDayKeyDeserializer.INSTANCE);
        this.addKeyDeserializer(OffsetDateTime.class, OffsetDateTimeKeyDeserializer.INSTANCE);
        this.addKeyDeserializer(OffsetTime.class, OffsetTimeKeyDeserializer.INSTANCE);
        this.addKeyDeserializer(Period.class, PeriodKeyDeserializer.INSTANCE);
        this.addKeyDeserializer(Year.class, YearKeyDeserializer.INSTANCE);
        this.addKeyDeserializer(YearMonth.class, YearMothKeyDeserializer.INSTANCE);
        this.addKeyDeserializer(ZonedDateTime.class, ZonedDateTimeKeyDeserializer.INSTANCE);
        this.addKeyDeserializer(ZoneId.class, ZoneIdKeyDeserializer.INSTANCE);
        this.addKeyDeserializer(ZoneOffset.class, ZoneOffsetKeyDeserializer.INSTANCE);
    }

    @Override
    public void setupModule(final SetupContext context) {
        super.setupModule(context);
        context.addValueInstantiators(new ValueInstantiators.Base() {
            @Override
            public ValueInstantiator findValueInstantiator(final DeserializationConfig config, final BeanDescription beanDesc, final ValueInstantiator defaultInstantiator) {
                final JavaType type = beanDesc.getType();
                final Class<?> raw = type.getRawClass();
                if (ZoneId.class.isAssignableFrom(raw) && defaultInstantiator instanceof StdValueInstantiator) {
                    final StdValueInstantiator inst = (StdValueInstantiator) defaultInstantiator;
                    final AnnotatedClass ac;
                    if (raw == ZoneId.class) {
                        ac = beanDesc.getClassInfo();
                    } else {
                        ac = AnnotatedClassResolver.resolve(config, config.constructType(ZoneId.class), config);
                    }

                    if (!inst.canCreateFromString()) {
                        final AnnotatedMethod factory = ZeroModule.this._findFactory(ac, "of", String.class);
                        if (factory != null) {
                            inst.configureFromStringCreator(factory);
                        }
                    }
                }

                return defaultInstantiator;
            }
        });
    }

    protected AnnotatedMethod _findFactory(final AnnotatedClass cls, final String name, final Class... argTypes) {
        final int argCount = argTypes.length;
        final Iterator var5 = cls.getFactoryMethods().iterator();

        AnnotatedMethod method;
        do {
            if (!var5.hasNext()) {
                return null;
            }

            method = (AnnotatedMethod) var5.next();
        } while (!name.equals(method.getName()) || method.getParameterCount() != argCount);

        for (int i = 0; i < argCount; ++i) {
            final Class<?> argType = method.getParameter(i).getRawType();
            if (!argType.isAssignableFrom(argTypes[i])) {
            }
        }

        return method;
    }
}
