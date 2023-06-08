package io.vertx.mod.battery.uca.configure;

import cn.vertxup.battery.domain.tables.pojos.BBag;
import cn.vertxup.battery.domain.tables.pojos.BBlock;
import io.horizon.uca.cache.Cc;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */

@SuppressWarnings("all")
public interface Combiner<I, M> {

    static Combiner<BBag, ConcurrentMap<String, BBag>> forBag() {
        return Pool.CC_COMBINER.pick(CombinerBag::new, CombinerBag.class.getName());
        // Fn.po?lThread(Pool.COMBINER, CombinerBag::new, CombinerBag.class.getName());
    }

    static Combiner<BBag, List<BBlock>> forBlock() {
        return Pool.CC_COMBINER.pick(CombinerBlock::new, CombinerBlock.class.getName());
        // return Fn.po?lThread(Pool.COMBINER, CombinerBlock::new, CombinerBlock.class.getName());
    }

    static Combiner<JsonObject, BBag> outDao() {
        return Pool.CC_COMBINER.pick(CombinerDao::new, CombinerDao.class.getName());
        // return Fn.po?lThread(Pool.COMBINER, CombinerDao::new, CombinerDao.class.getName());
    }

    static Combiner<JsonObject, BBag> outBag() {
        return Pool.CC_COMBINER.pick(CombinerOutBag::new, CombinerOutBag.class.getName());
        // return Fn.po?lThread(Pool.COMBINER, CombinerOutBag::new, CombinerOutBag.class.getName());
    }

    static Combiner<JsonObject, Collection<BBag>> outChildren() {
        return Pool.CC_COMBINER.pick(CombinerOutChildren::new, CombinerOutChildren.class.getName());
        // return Fn.po?lThread(Pool.COMBINER, CombinerOutChildren::new, CombinerOutChildren.class.getName());
    }

    Future<JsonObject> configure(I bag, M map);
}

@SuppressWarnings("all")
interface Pool {
    Cc<String, Combiner> CC_COMBINER = Cc.openThread();
}
