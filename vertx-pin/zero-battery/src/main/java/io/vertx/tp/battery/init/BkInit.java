package io.vertx.tp.battery.init;

import io.horizon.spi.modeler.ExModulat;
import io.horizon.spi.modeler.Modulat;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;

import static io.vertx.tp.battery.refine.Bk.LOG;

public class BkInit {
    /**
     * 模块化初始化，根据应用直接处理模块本身的初始化
     *
     * @param vertx Vertx 实例
     *
     * @return 返回初始化结果
     */
    public static Future<Boolean> initModulat(final Vertx vertx) {
        return Ke.mapApp(appJ -> {
            final String appKey = Ut.valueString(appJ, KName.KEY);
            if (Objects.isNull(appKey)) {
                LOG.Init.warn(BkInit.class, "App Id = null, ignored initialized!!");
                return Ux.futureF();
            }
            final Modulat modulat = new ExModulat();
            return modulat.extension(appJ).compose(nil -> Ux.futureT());
        }, (result) -> {
            final boolean initialized = result.stream().allMatch(item -> item);
            LOG.Init.info(BkInit.class, "Modulat configuration initialized!! Size = {0}, Ret = {1}",
                String.valueOf(result.size()), initialized);
            return Ux.future(initialized);
        });
    }
}
