package io.vertx.up.util;

import io.horizon.eon.VString;
import io.vertx.core.json.JsonObject;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lang
 */
final class StringUtil {

    private StringUtil() {
    }

    static String from(final Object value) {
        return null == value ? VString.EMPTY : value.toString();
    }

    static String from(final JsonObject value) {
        return null == value ? VString.EMPTY : value.toString();
    }


    static JsonObject prefix(final JsonObject data, final String prefix) {
        // Add prefix to each key to build new JsonObject
        final JsonObject resultJ = new JsonObject();
        data.fieldNames().forEach(k -> resultJ.put(prefix + VString.DOT + k, data.getValue(k)));
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
}
