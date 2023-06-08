package io.horizon.spi.ambient;

import io.horizon.spi.extension.Init;
import io.horizon.uca.cache.Cc;

/*
 * OOB数据初始化专用接口
 */
public interface AoRefine extends Init {

    static AoRefine combine() {
        return Pool.CC_REFINE.pick(CombineRefine::new, CombineRefine.class.getName());
        //  Fn.po?l(Pool.REFINE_POOL, CombineRefine.class.getName(),CombineRefine::new);
    }

    static AoRefine schema() {
        return Pool.CC_REFINE.pick(SchemaRefine::new, SchemaRefine.class.getName());
        // return Fn.po?l(Pool.REFINE_POOL, SchemaRefine.class.getName(),SchemaRefine::new);
    }

    static AoRefine model() {
        return Pool.CC_REFINE.pick(ModelRefine::new, ModelRefine.class.getName());
        // return Fn.po?l(Pool.REFINE_POOL, ModelRefine.class.getName(), ModelRefine::new);
    }
}

interface Pool {

    Cc<String, AoRefine> CC_REFINE = Cc.open();
}
