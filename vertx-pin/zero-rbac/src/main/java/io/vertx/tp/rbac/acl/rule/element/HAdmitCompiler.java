package io.vertx.tp.rbac.acl.rule.element;

import io.vertx.aeon.eon.em.ScIn;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.uca.cache.Cc;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * 独立接口，对应不同的Ui模式的方法提取流程，每一种构造一个核心组件
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HAdmitCompiler {

    Cc<String, HAdmitCompiler> CCT_ADMIT_COMPILER = Cc.openThread();

    /*
     * 此处 qr 已经是做过 fromExpression 解析的值，可直接作为 qr 来处理
     */
    static HAdmitCompiler instance(final ScIn in) {
        final Supplier<HAdmitCompiler> supplier = __H1H.ADMIN_COMPILER.get(in);
        if (Objects.isNull(supplier)) {
            return null;
        }
        return CCT_ADMIT_COMPILER.pick(supplier, in.name());
    }

    Future<JsonArray> ingest(JsonObject qr, JsonObject config);
}
