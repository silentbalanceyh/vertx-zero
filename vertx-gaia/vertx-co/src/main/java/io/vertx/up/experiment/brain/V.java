package io.vertx.up.experiment.brain;

import io.vertx.up.util.Ut;

@SuppressWarnings("all")
public class V {
    public static Vto vInstant() {
        return Ut.singleton(InstantVto.class);
    }

    public static Vto vInteger() {
        return Ut.singleton(IntVto.class);
    }

    public static Vto vLong() {
        return Ut.singleton(LongVto.class);
    }

    public static Vto vShort() {
        return Ut.singleton(ShortVto.class);
    }

    public static Vto vDouble() {
        return Ut.singleton(DoubleVto.class);
    }

    public static Vto vFloat() {
        return Ut.singleton(FloatVto.class);
    }

    public static Vto vBoolean() {
        return Ut.singleton(BooleanVto.class);
    }

    public static Vto vDate() {
        return Ut.singleton(DateVto.class);
    }
}
