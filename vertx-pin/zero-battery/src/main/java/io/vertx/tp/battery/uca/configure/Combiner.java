package io.vertx.tp.battery.uca.configure;

import cn.vertxup.battery.domain.tables.pojos.BBag;
import cn.vertxup.battery.domain.tables.pojos.BBlock;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.fn.Fn;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */

@SuppressWarnings("all")
public interface Combiner<I, M> {

    static Combiner<BBag, ConcurrentMap<String, BBag>> forBag() {
        return Fn.poolThread(Pool.COMBINER, CombinerBag::new, CombinerBag.class.getName());
    }

    static Combiner<BBag, List<BBlock>> forBlock() {
        return Fn.poolThread(Pool.COMBINER, CombinerBlock::new, CombinerBlock.class.getName());
    }

    static Combiner<JsonObject, BBag> outBag() {
        return Fn.poolThread(Pool.COMBINER, CombinerOutBag::new, CombinerOutBag.class.getName());
    }

    static Combiner<JsonObject, Collection<BBag>> outChildren() {
        return Fn.poolThread(Pool.COMBINER, CombinerOutChildren::new, CombinerOutChildren.class.getName());
    }

    Future<JsonObject> configure(I bag, M map);
}

@SuppressWarnings("all")
interface Pool {
    ConcurrentMap<String, Combiner> COMBINER = new ConcurrentHashMap<>();
}
