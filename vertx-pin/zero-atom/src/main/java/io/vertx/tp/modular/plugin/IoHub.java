package io.vertx.tp.modular.plugin;

import io.vertx.tp.atom.modeling.element.DataTpl;
import io.vertx.up.commune.Record;
import io.vertx.up.fn.Fn;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * ## Nerve System
 *
 * ### 1. Intro
 *
 * It's plug-in channel for `M_MODEL` to process different attribute in Sync mode ( Jooq < 3.10.8 ), the work phases are
 * as following:
 *
 * #### 1.1. Input
 *
 * - 1. IComponent ( Record )
 * - 2. INormalizer ( Attribute )
 *
 * #### 1.2. Output
 *
 * - 1. OComponent ( Record )
 * - 2. Reference Calculation
 * - 3. OExpression ( Attribute )
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface IoHub {

    ConcurrentMap<String, IoHub> HUB_MAP = new ConcurrentHashMap<>();

    static IoHub instance() {
        return Fn.poolThread(HUB_MAP, IoNerve::new);
    }

    Record in(Record record, DataTpl tpl);

    Record[] in(Record[] records, DataTpl tpl);

    Record out(Record record, DataTpl tpl);

    Record[] out(Record[] records, DataTpl tpl);
}
