package io.vertx.up.fn.wait;

import java.util.Objects;
import java.util.function.Function;

public class Tuple<T, U> {
    public final T first;
    public final U second;

    Tuple(final T t, final U u) {
        this.first = Objects.requireNonNull(t);
        this.second = Objects.requireNonNull(u);
    }

    public static <T> Tuple<T, T> swapIf(final Tuple<T, T> t, final Function<T, Function<T, Boolean>> p) {
        return p.apply(t.first).apply(t.second) ? t.swap() : t;
    }

    private Tuple<U, T> swap() {
        return new Tuple<>(this.second, this.first);
    }

    @Override
    public String toString() {
        return String.format("(%s,%s)", this.first, this.second);
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o.getClass() == this.getClass())) {
            return false;
        } else {
            @SuppressWarnings("rawtypes") final Tuple that = (Tuple) o;
            return this.first.equals(that.first) && this.second.equals(that.second);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.first.hashCode();
        result = prime * result + this.second.hashCode();
        return result;
    }
}
