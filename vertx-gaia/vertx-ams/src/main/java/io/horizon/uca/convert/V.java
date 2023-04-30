package io.horizon.uca.convert;

import io.horizon.eon.cache.CStore;

@SuppressWarnings("all")
public class V {

    public static Vto vInstant() {
        return CStore.CCT_VTO.pick(InstantVto::new, InstantVto.class.getName());
        // Fn.po?lThread(POOL_VTO, InstantVto::new, InstantVto.class.getName());
        // return Ut.singleton(InstantVto.class);
    }

    public static Vto vInteger() {
        return CStore.CCT_VTO.pick(IntVto::new, IntVto.class.getName());
        // Fn.po?lThread(POOL_VTO, IntVto::new, IntVto.class.getName());
        // return Ut.singleton(IntVto.class);
    }

    public static Vto vLong() {
        return CStore.CCT_VTO.pick(LongVto::new, LongVto.class.getName());
        // Fn.po?lThread(POOL_VTO, LongVto::new, LongVto.class.getName());
        // return Ut.singleton(LongVto.class);
    }

    public static Vto vShort() {
        return CStore.CCT_VTO.pick(ShortVto::new, ShortVto.class.getName());
        // Fn.po?lThread(POOL_VTO, ShortVto::new, ShortVto.class.getName());
        // return Ut.singleton(ShortVto.class);
    }

    public static Vto vDouble() {
        return CStore.CCT_VTO.pick(DoubleVto::new, DoubleVto.class.getName());
        // Fn.po?lThread(POOL_VTO, DoubleVto::new, DoubleVto.class.getName());
        // return Ut.singleton(DoubleVto.class);
    }

    public static Vto vFloat() {
        return CStore.CCT_VTO.pick(FloatVto::new, FloatVto.class.getName());
        // Fn.po?lThread(POOL_VTO, FloatVto::new, FloatVto.class.getName());
        // return Ut.singleton(FloatVto.class);
    }

    public static Vto vBoolean() {
        return CStore.CCT_VTO.pick(BooleanVto::new, BooleanVto.class.getName());
        // Fn.po?lThread(POOL_VTO, BooleanVto::new, BooleanVto.class.getName());
        // return Ut.singleton(BooleanVto.class);
    }

    public static Vto vDate() {
        return CStore.CCT_VTO.pick(DateVto::new, DateVto.class.getName());
        // Fn.po?lThread(POOL_VTO, DateVto::new, DateVto.class.getName());
        // return Ut.singleton(DateVto.class);
    }
}
