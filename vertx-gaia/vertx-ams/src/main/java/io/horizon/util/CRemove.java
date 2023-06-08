package io.horizon.util;

import io.horizon.eon.VValue;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author lang : 2023/4/30
 */
class CRemove {
    static <T, V> List<T> remove(final List<T> list, final T entity, final Function<T, V> keyFn) {
        if (Objects.isNull(entity)) {
            return list;
        }
        final V keyAdd = keyFn.apply(entity);
        if (Objects.isNull(keyAdd)) {
            return list;
        }
        for (int idx = VValue.IDX; idx < list.size(); idx++) {
            final T original = list.get(idx);
            if (Objects.isNull(original)) {
                continue;
            }
            final V keyOld = keyFn.apply(original);
            if (keyAdd.equals(keyOld)) {
                list.remove(original);
                break;
            }
        }
        return list;
    }
}
