package io.vertx.tp.rbac.ruler.element;

import io.horizon.eon.em.cloud.ScIn;
import io.vertx.tp.rbac.cv.em.PackType;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
interface __H1H {
    // ScIn = Supplier :: HAdmitCompiler
    ConcurrentMap<ScIn, Supplier<HAdmitCompiler>> ADMIN_COMPILER = new ConcurrentHashMap<>() {
        {
            this.put(ScIn.DAO, UiDaoCompiler::new);
            this.put(ScIn.WEB, UiWebCompiler::new);
        }
    };
    // HType = Supplier :: HEyelet
    // VType = Supplier :: HEyelet
    // QType = Supplier :: HEyelet
    ConcurrentMap<Enum, Supplier<HEyelet>> EYELET = new ConcurrentHashMap<>() {
        {
            put(PackType.HType.IN, EyeletRow::new);
        }
    };
}
