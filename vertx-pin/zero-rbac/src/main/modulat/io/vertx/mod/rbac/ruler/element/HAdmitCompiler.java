package io.vertx.mod.rbac.ruler.element;

import io.aeon.atom.secure.KPermit;
import io.horizon.uca.cache.Cc;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.error._404AdmitCompilerNullException;
import io.vertx.up.eon.em.EmSecure;
import io.vertx.up.fn.Fn;

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
    static HAdmitCompiler create(final KPermit permit, final Class<?> target) {
        final EmSecure.ScIn in = permit.source();
        final Supplier<HAdmitCompiler> supplier = __H1H.ADMIN_COMPILER.get(in);
        if (Objects.isNull(supplier)) {
            return null;
        }
        final HAdmitCompiler compiler = CCT_ADMIT_COMPILER.pick(supplier, in.name());

        // Error-80225
        final Class<?> targetCls = Objects.isNull(target) ? HAdmitCompiler.class : target;
        Fn.out(Objects.isNull(compiler), _404AdmitCompilerNullException.class, targetCls, in);
        return compiler;
    }

    Future<JsonArray> ingest(JsonObject qr, JsonObject config);
}
