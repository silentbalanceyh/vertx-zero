package io.vertx.tp.crud.cv;

import io.vertx.tp.crud.uca.input.Pre;
import io.vertx.tp.crud.uca.next.Co;
import io.vertx.tp.crud.uca.op.Agonic;
import io.vertx.tp.crud.uca.trans.Tran;
import io.vertx.up.uca.cache.Cc;

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
