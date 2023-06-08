package io.vertx.mod.rbac.ruler.element;

import io.vertx.mod.rbac.cv.em.PackType;
import io.vertx.up.eon.em.EmSecure;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
interface __H1H {
    // ScIn = Supplier :: HAdmitCompiler
    ConcurrentMap<EmSecure.ScIn, Supplier<HAdmitCompiler>> ADMIN_COMPILER = new ConcurrentHashMap<>() {
        {
            this.put(EmSecure.ScIn.DAO, UiDaoCompiler::new);
            this.put(EmSecure.ScIn.WEB, UiWebCompiler::new);
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
