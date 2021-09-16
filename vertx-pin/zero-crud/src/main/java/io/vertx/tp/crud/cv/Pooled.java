package io.vertx.tp.crud.cv;

import io.vertx.tp.crud.uca.desk.IxKit;
import io.vertx.tp.crud.uca.input.Pre;
import io.vertx.tp.crud.uca.op.Agonic;
import io.vertx.tp.crud.uca.tran.Co;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
public interface Pooled {

    ConcurrentMap<String, Agonic> AGONIC_MAP = new ConcurrentHashMap<>();

    ConcurrentMap<String, Pre> PRE_MAP = new ConcurrentHashMap<>();

    ConcurrentMap<String, IxKit> POST_MAP = new ConcurrentHashMap<>();

    ConcurrentMap<String, Co> CO_MAP = new ConcurrentHashMap<>();
}
