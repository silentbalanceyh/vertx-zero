package io.vertx.up.experiment.brain;

import io.vertx.up.fn.Fn;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@SuppressWarnings("all")
public class V {
    private static final ConcurrentMap<String, Vto> POOL_VTO = new ConcurrentHashMap<>();

    public static Vto vInstant() {
        return Fn.poolThread(POOL_VTO, InstantVto::new, InstantVto.class.getName());
        // return Ut.singleton(InstantVto.class);
    }

    public static Vto vInteger() {
        return Fn.poolThread(POOL_VTO, IntVto::new, IntVto.class.getName());
        // return Ut.singleton(IntVto.class);
    }

    public static Vto vLong() {
        return Fn.poolThread(POOL_VTO, LongVto::new, LongVto.class.getName());
        // return Ut.singleton(LongVto.class);
    }

    public static Vto vShort() {
        return Fn.poolThread(POOL_VTO, ShortVto::new, ShortVto.class.getName());
        // return Ut.singleton(ShortVto.class);
    }

    public static Vto vDouble() {
        return Fn.poolThread(POOL_VTO, DoubleVto::new, DoubleVto.class.getName());
        // return Ut.singleton(DoubleVto.class);
    }

    public static Vto vFloat() {
        return Fn.poolThread(POOL_VTO, FloatVto::new, FloatVto.class.getName());
        // return Ut.singleton(FloatVto.class);
    }

    public static Vto vBoolean() {
        return Fn.poolThread(POOL_VTO, BooleanVto::new, BooleanVto.class.getName());
        // return Ut.singleton(BooleanVto.class);
    }

    public static Vto vDate() {
        return Fn.poolThread(POOL_VTO, DateVto::new, DateVto.class.getName());
        // return Ut.singleton(DateVto.class);
    }
}
