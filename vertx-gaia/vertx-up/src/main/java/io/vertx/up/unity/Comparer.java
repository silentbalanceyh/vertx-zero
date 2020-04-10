package io.vertx.up.unity;

import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/*
 * Compare two entity collection for
 * 1) Get `ADD` operation
 * 2) Get `UPDATE` operation
 * 3) Get `DELETE` operation
 */
class Comparer {

    static <T, R> ConcurrentMap<ChangeFlag, List<T>> compare(final List<T> original, final List<T> current,
                                                             final Function<T, R> fnValue,
                                                             final String mergedPojo) {
        final ConcurrentMap<ChangeFlag, List<T>> comparedMap = new ConcurrentHashMap<ChangeFlag, List<T>>() {
            {
                this.put(ChangeFlag.DELETE, new ArrayList<>());
                this.put(ChangeFlag.UPDATE, new ArrayList<>());
                this.put(ChangeFlag.ADD, new ArrayList<>());
            }
        };
        if (Objects.isNull(original) || original.isEmpty()) {
            /*
             * No `DELETE`, No `UPDATE`
             * In this situation, all the entity should be `ADD`
             * Not null for double checking
             */
            if (Objects.nonNull(current)) {
                comparedMap.get(ChangeFlag.ADD).addAll(current);
            }
        } else {
            /*
             * Calculation for `DELETE`
             */
            original.forEach(originalItem -> {
                final T latestItem = find(current, originalItem, fnValue);
                if (Objects.isNull(latestItem)) {
                    /*
                     * Delete
                     */
                    comparedMap.get(ChangeFlag.DELETE).add(originalItem);
                }
            });
            /*
             * Calculation for `ADD / UPDATE`
             */
            current.forEach(latestItem -> {
                final T previous = find(original, latestItem, fnValue);
                if (Objects.isNull(previous)) {
                    /*
                     * ADD
                     */
                    comparedMap.get(ChangeFlag.ADD).add(latestItem);
                } else {
                    /*
                     * original / current contains
                     * UPDATE
                     */
                    final T updated = combine(previous, latestItem, mergedPojo);
                    comparedMap.get(ChangeFlag.UPDATE).add(updated);
                }
            });
        }
        return comparedMap;
    }

    @SuppressWarnings("all")
    private static <T> T combine(final T old, final T latest, final String pojo) {
        if (Objects.isNull(old) && Objects.isNull(latest)) {
            return null;
        } else {
            if (Objects.isNull(old)) {
                return latest;
            } else if (Objects.isNull(latest)) {
                return old;
            } else {
                /*
                 * Convert old entity to json
                 */
                final JsonObject combineJson = Ut.sureJObject(To.toJson(old, pojo));
                /*
                 * Convert current entity to json
                 */
                final JsonObject latestJson = Ut.sureJObject(To.toJson(latest, pojo));
                /*
                 * Merged
                 */
                combineJson.mergeIn(latestJson, true);
                /*
                 * Deserialization
                 */
                final Class<?> clazz = latest.getClass();
                return (T) From.fromJson(combineJson, clazz, pojo);
            }
        }
    }

    private static <T, R> T find(final List<T> list, final T current, final Function<T, R> fnValue) {
        if (Objects.isNull(list) || list.isEmpty() || Objects.isNull(current)) {
            /*
             * Could not be found here
             * 1) list is null          ( Source List )
             * 2) list is empty         ( Source List )
             * 3) current is null       ( Target Entity )
             */
            return null;
        } else {
            final R comparedValue = fnValue.apply(current);
            if (Objects.isNull(comparedValue)) {
                /*
                 * Compared value is null, return null instead of deeply lookup
                 */
                return null;
            } else {
                return list.stream().filter(Objects::nonNull)
                        .filter(each -> comparedValue.equals(fnValue.apply(each)))
                        .findAny().orElse(null);
            }
        }
    }
}
