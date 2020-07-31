package io.vertx.up.util;

import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.Strings;
import io.vertx.up.fn.Fn;
import org.apache.commons.jexl3.*;

import java.util.*;
import java.util.function.Supplier;

/**
 * @author lang
 */
final class StringUtil {
    private static final JexlEngine EXPR = new JexlBuilder()
            .cache(512).silent(false).create();
    private static final String SEED =
            "01234567890qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
    private static final String CHAR =
            "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";

    private StringUtil() {
    }

    static String from(final Object value) {
        return null == value ? Strings.EMPTY : value.toString();
    }

    static String from(final JsonObject value) {
        return null == value ? Strings.EMPTY : value.toString();
    }

    static Set<String> split(final String input, final String separator) {
        return Fn.getNull(new HashSet<>(), () -> {
            final String[] array = input.split(separator);
            final Set<String> result = new HashSet<>();
            for (final String item : array) {
                Fn.safeNull(() -> result.add(item.trim().intern()), item);
            }
            return result;
        }, input, separator);
    }

    /*
     * Object[] could not be cast to String[] directly
     * It means here must contain below method to process it.
     */
    static String join(final Object[] input, final String separator) {
        final Set<String> hashSet = new HashSet<>();
        Arrays.stream(input).filter(Objects::nonNull)
                .map(Object::toString).forEach(hashSet::add);
        return join(hashSet, separator);
    }

    static String join(final Collection<String> input, final String separator) {
        final String connector = (null == separator) ? Strings.COMMA : separator;
        return Fn.getJvm(() -> {
            final StringBuilder builder = new StringBuilder();
            final int size = input.size();
            int start = 0;
            for (final String item : input) {
                builder.append(item);
                start++;
                if (start < size) {
                    builder.append(connector);
                }
            }
            return builder.toString();
        }, input);
    }

    private static String repeat(final Integer times, final char fill) {
        final StringBuilder builder = new StringBuilder();
        for (int idx = 0; idx < times; idx++) {
            builder.append(fill);
        }
        return builder.toString();
    }

    static String adjust(final Integer seed, final Integer width, final char fill) {
        return adjust(seed.toString(), width, fill);
    }

    static String adjust(final String seed, final Integer width, final char fill) {
        final StringBuilder builder = new StringBuilder();
        final int seedLen = seed.length();
        int fillLen = width - seedLen;
        if (0 > fillLen) {
            fillLen = 0;
        }
        builder.append(repeat(fillLen, fill));
        builder.append(seed);
        return builder.toString();
    }

    private static char randomChar() {
        return randomChar(SEED);
    }

    private static char randomCharNoDigit() {
        return randomChar(CHAR);
    }

    private static char randomChar(final String seed) {
        final Random random = new Random();
        return seed.charAt(random.nextInt(seed.length()));
    }

    static String random(final int length) {
        return random(length, StringUtil::randomChar);
    }

    static String randomNoDigit(final int length) {
        return random(length, StringUtil::randomCharNoDigit);
    }

    private static String random(int length, final Supplier<Character> supplier) {
        final StringBuilder builder = new StringBuilder();
        while (0 < length) {
            final char seed = supplier.get();
            builder.append(seed);
            length--;
        }
        return builder.toString();
    }

    static String expression(final String expr, final JsonObject params) {
        try {
            final JexlExpression expression = EXPR.createExpression(expr);
            // Parameter
            final JexlContext context = new MapContext();
            Ut.itJObject(params, (value, key) -> context.set(key, value));
            return expression.evaluate(context).toString();
        } catch (final JexlException ex) {
            // ex.printStackTrace();   // For Debug
            return expr;            // For Keep original string
            // throw new JexlExpressionException(StringUtil.class, expr, ex);
        }
    }

    static boolean isNil(final String input) {
        return null == input || 0 == input.trim().length();
    }

    static boolean isNilOr(final String... inputs) {
        final long counter = Arrays.stream(inputs).filter(StringUtil::isNil).count();
        return counter != 0L;
    }

    static boolean notNil(final String input) {
        return !isNil(input);
    }
}
