package io.vertx.up.util;

import io.horizon.eon.VString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
final class IOPath {

    private IOPath() {
    }


    static List<String> ladder(final String path) {
        if (Ut.isNil(path)) {
            return new ArrayList<>();
        }
        final String[] splitArr = path.split(VString.SLASH);
        final List<String> itemList = new ArrayList<>();
        for (int idx = 0; idx < splitArr.length; idx++) {
            final StringBuilder item = new StringBuilder();
            for (int jdx = 0; jdx < idx; jdx++) {
                item.append(splitArr[jdx]).append(VString.SLASH);
            }
            item.append(splitArr[idx]);
            final String itemStr = item.toString();
            if (Ut.notNil(itemStr)) {
                itemList.add(itemStr);
            }
        }
        return itemList;
    }

    static String first(final String path, final String separator) {
        final String[] names = path.split(separator);
        String result = null;
        for (final String found : names) {
            if (StringUtil.notNil(found)) {
                result = found;
                break;
            }
        }
        return result;
    }

    @SuppressWarnings("all")
    static String last(final String path, final String separator) {
        final String[] names = path.split(separator);
        String result = null;
        for (int idx = names.length - 1; idx < names.length; idx--) {
            final String found = names[idx];
            if (StringUtil.notNil(found)) {
                result = found;
                break;
            }
        }
        return result;
    }
}
