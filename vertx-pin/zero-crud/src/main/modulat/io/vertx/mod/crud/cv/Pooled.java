package io.vertx.mod.crud.cv;

import io.horizon.uca.cache.Cc;
import io.vertx.mod.crud.uca.input.Pre;
import io.vertx.mod.crud.uca.next.Co;
import io.vertx.mod.crud.uca.op.Agonic;
import io.vertx.mod.crud.uca.trans.Tran;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
public interface Pooled {
    Cc<String, Agonic> CC_AGONIC = Cc.openThread();
    Cc<String, Pre> CC_PRE = Cc.openThread();
    Cc<String, Tran> CC_TRAN = Cc.openThread();
    Cc<String, Co> CC_CO = Cc.openThread();
}
