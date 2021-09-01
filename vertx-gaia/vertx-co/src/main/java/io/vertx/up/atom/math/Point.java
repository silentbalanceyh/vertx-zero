package io.vertx.up.atom.math;

import io.vertx.core.json.JsonArray;
import io.vertx.up.eon.Values;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Data Structure for spec structure
 * 2 - [ x, y ]
 * 3 - [ x, y, z ]
 * 4 - [ x, y, z, ? ]
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class Point<T> implements Serializable {
    private final transient List<T> data = new ArrayList<>();

    @SafeVarargs
    private Point(final T... input) {
        this.data.addAll(Arrays.asList(input));
    }

    @SuppressWarnings({"all"})
    private Point(final List inputList) {
        this.data.addAll(inputList);
    }

    /*
     * [x,y] structure
     */
    public static <T> Point<T> create(final T x, final T y) {
        return new Point<>(x, y);
    }

    /*
     * [x,y,z] structure
     */
    public static <T> Point<T> create(final T x, final T y, final T z) {
        return new Point<>(x, y, z);
    }

    public static <T> Point<T> create(final String literal) {
        final JsonArray result = Ut.toJArray(literal);
        return new Point<>(result.getList());
    }

    public static <T> Point<T> create(final JsonArray jsonArray) {
        final JsonArray result = Ut.sureJArray(jsonArray);
        return new Point<>(result.getList());
    }

    //  -------------------- 2D Api ------------------------
    public boolean ok2D() {
        return 2 == this.data.size()
            && Objects.nonNull(this.data.get(Values.IDX))
            && Objects.nonNull(this.data.get(Values.ONE));
    }

    public T x() {
        return this.data.get(Values.IDX);
    }

    public T y() {
        return this.data.get(Values.ONE);
    }

    //  -------------------- 3D Api ------------------------
    public boolean ok3D() {
        return 3 == this.data.size()
            && Objects.nonNull(this.data.get(Values.IDX))
            && Objects.nonNull(this.data.get(Values.ONE))
            && Objects.nonNull(this.data.get(Values.TWO));
    }

    public T z() {
        return (2 < this.data.size()) ? this.data.get(Values.TWO) : null;
    }
    //  -------------------- nD Api ------------------------

    public boolean ok() {
        return this.data.stream().allMatch(Objects::nonNull);
    }

    public Point<T> raise(final T element) {
        this.data.add(element);
        return this;
    }
}
