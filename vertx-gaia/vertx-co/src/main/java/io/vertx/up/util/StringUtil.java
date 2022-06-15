package io.vertx.up.util;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.Strings;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Debugger;
import org.apache.commons.jexl3.*;

import java.util.*;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lang
 */
final class StringUtil {
    private static final JexlEngine EXPR = new JexlBuilder()
        .cache(4096).silent(false).create();
    private static final String SEED =
        "01234567890qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
    private static final String CAPTCHA_SEED =
        "23456789qwertyuipasdfghjkzxcvbnmQWERTYUPASDFGHJKLZXCVBNM";
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

    /// Captcha related. ///
    private static char randomCaptchaChar() {
        return randomChar(CAPTCHA_SEED);
    }

    static String captcha(final int length) {
        return random(length, StringUtil::randomCaptchaChar);
    }

    /// Random related. ///
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

    /*
     * Input Data Structure
     * {
     *     "field1": "expr1",
     *     "field2": "expr2"
     * }
     *
     * 1. If expr contains ` character, it will be replaced by parsed result.
     * 2. If expr does not contain ` character, it will be kept.
     *
     * Then the method will create new parts of normalized to avoid modify input exprObject
     */
    static JsonObject expression(final JsonObject exprObject, final JsonObject params) {
        // Iterator On Json Object
        final JsonObject parsed = new JsonObject();
        if (Ut.notNil(exprObject)) {
            exprObject.fieldNames().forEach(k -> {
                final Object value = exprObject.getValue(k);
                if (value instanceof String) {


                    // 「String」
                    final Object formatted = expressionWith((String) value, params);
                    parsed.put(k, formatted);
                } else if (value instanceof JsonObject) {


                    // 「JsonObject」
                    final JsonObject formatted = expression((JsonObject) value, params);
                    parsed.put(k, formatted);
                } else if (value instanceof JsonArray) {


                    // 「JsonArray」
                    final JsonArray formatted = expression((JsonArray) value, params);
                    parsed.put(k, formatted);
                } else {
                    // 「Keep」Non-String Part include `null`
                    parsed.put(k, value);
                }
            });
        }
        return parsed;
    }

    static JsonArray expression(final JsonArray exprArray, final JsonObject params) {
        final JsonArray normalized = new JsonArray();
        if (Ut.notNil(exprArray)) {
            exprArray.forEach(valueElement -> {
                if (valueElement instanceof String) {

                    // Element = String
                    final Object formatted = expressionWith((String) valueElement, params);
                    normalized.add(formatted);
                } else if (valueElement instanceof JsonObject) {


                    // Element = JsonObject
                    final JsonObject formatted = expression((JsonObject) valueElement, params);
                    normalized.add(formatted);
                } else if (valueElement instanceof JsonArray) {


                    // Element = JsonArray
                    final JsonArray formatted = expression((JsonArray) valueElement, params);
                    normalized.add(formatted);
                } else {


                    // Element = Other
                    normalized.add(valueElement);
                }
            });
        }
        return normalized;
    }

    private static Object expressionWith(final String valueExpr, final JsonObject params) {
        if (Ut.notNil(valueExpr)) {
            final Object valueResult;
            if (valueExpr.contains("`")) {
                // Actual Parsing
                valueResult = expressionT(valueExpr, params);
            } else {
                // 「Keep」Original String
                valueResult = valueExpr;
            }
            return valueResult;
        } else {
            // 「Keep」Empty String
            return valueExpr;
        }
    }

    @SuppressWarnings("unchecked")
    static <T> T expressionT(final String valueExpr, final JsonObject params) {
        try {
            /*
             * cache(4096), the default cache size is 4096.
             * silent(false): silent = false means the exception will be thrown.
             */
            final JexlExpression expression = EXPR.createExpression(valueExpr);
            // Parameter
            final JexlContext context = new MapContext();
            Ut.itJObject(params, (value, key) -> context.set(key, value));
            // Processed
            final Object result = expression.evaluate(context);
            if (Objects.nonNull(result)) {
                return (T) result;
            } else {
                return null;
            }
        } catch (final JexlException ex) {
            // ex.printStackTrace();    // For Debug
            if (Debugger.onStackTracing()) {
                ex.printStackTrace();
            }
            return null;                // Get null
            // throw new JexlExpressionException(StringUtil.class, expr, ex);
        }
    }

    static JsonObject prefix(final JsonObject data, final String prefix) {
        // Add prefix to each key to build new JsonObject
        final JsonObject resultJ = new JsonObject();
        data.fieldNames().forEach(k -> resultJ.put(prefix + Strings.DOT + k, data.getValue(k)));
        return resultJ;
    }

    /*
     * The engine is:
     *
     *      JexlEngine EXPR = new JexlBuilder().cache(4096).silent(false).create()
     *
     * The basic expression parsing method for default usage such as:
     *
     * `Message ${name} and ${code}`
     *
     * Be careful of expression syntax:
     * 1) The start/end character should be "`".
     * 2) The variable expression part is "${name}".
     *
     * This api is for message parsing only, this expression must start/end with "`" so that
     * it could be parsed, other situations will be ignored.
     */
    static String expression(final String expr, final JsonObject params) {
        return expressionT(expr, params);
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

    static Set<String> matched(final String input, final String regex) {
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(input);
        final Set<String> matchSet = new HashSet<>();
        while (matcher.find()) {
            final String found = matcher.group();
            matchSet.add(found);
        }
        return matchSet;
    }

    static String path(final String folder, final String file) {
        Objects.requireNonNull(file);
        final String valueFolder;
        if (Ut.isNil(folder)) {
            valueFolder = "/";
        } else {
            if (folder.endsWith("/")) {
                // Fix issue of deployment on production environment data loading
                valueFolder = folder.substring(0, folder.lastIndexOf("/"));
            } else {
                valueFolder = folder;
            }
        }
        final String valueFile;
        if (file.startsWith("/")) {
            valueFile = file;
        } else {
            valueFile = "/" + file;
        }
        // Convert `//` to `/`
        return (valueFolder + valueFile).replace("//", "/");
    }

    // Regex Matcher for string
    static boolean isMatch(final String regex, final String original) {
        return Fn.getNull(() -> {
            final Pattern pattern = Pattern.compile(regex);
            final Matcher matcher = pattern.matcher(original);
            return matcher.matches();
        }, regex, original);
    }
}
