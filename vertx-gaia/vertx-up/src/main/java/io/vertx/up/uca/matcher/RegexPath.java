package io.vertx.up.uca.matcher;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexPath {

    private static final Pattern RE_OPERATORS_NO_STAR =
        Pattern.compile("([\\(\\)\\$\\+\\.])");

    public static Pattern createRegex(String path) {
        // escape path from any regex special chars
        path = RE_OPERATORS_NO_STAR.matcher(path).replaceAll("\\\\$1");
        // allow usage of * at the end as per documentation
        if (path.charAt(path.length() - 1) == '*') {
            path = path.substring(0, path.length() - 1) + ".*";
        }
        // We need to search for any :<token name> tokens in the String and replace them with named capture groups
        final Matcher m = Pattern.compile(":([A-Za-z][A-Za-z0-9_]*)").matcher(path);
        final StringBuffer sb = new StringBuffer();
        final List<String> groups = new ArrayList<>();
        int index = 0;
        while (m.find()) {
            final String param = "p" + index;
            final String group = m.group().substring(1);
            if (groups.contains(group)) {
                throw new IllegalArgumentException("Cannot use identifier " + group + " more than once in pattern string");
            }
            m.appendReplacement(sb, "(?<" + param + ">[^/]+)");
            groups.add(group);
            index++;
        }
        m.appendTail(sb);
        path = sb.toString();
        return Pattern.compile(path);
    }
}
