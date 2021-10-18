package io.vertx.tp.optic.ambient;

import io.vertx.tp.optic.extension.Init;
import io.vertx.up.fn.Fn;

/*
 * OOB数据初始化专用接口
 */
public interface AoRefine extends Init {

    static AoRefine combine() {
        return Fn.pool(Pool.REFINE_POOL, CombineRefine.class.getName(),
            CombineRefine::new);
    }

    static AoRefine schema() {
        return Fn.pool(Pool.REFINE_POOL, SchemaRefine.class.getName(),
            SchemaRefine::new);
    }

    static AoRefine model() {
        return Fn.pool(Pool.REFINE_POOL, ModelRefine.class.getName(),
            ModelRefine::new);
    }
}
