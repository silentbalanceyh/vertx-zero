package io.vertx.aeon.atom;

import io.vertx.aeon.atom.configuration.HKinect;
import io.vertx.aeon.eon.HName;
import io.vertx.aeon.uca.HLog;
import io.vertx.core.json.JsonObject;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.uca.yaml.ZeroVertx;
import io.vertx.up.util.Ut;

import java.util.Objects;

/**
 * 「开关」开关专用模块
 *
 * 1. Aeon系统配置鉴别专用
 * 2. 启动参数鉴别
 * 3. Aeon系统环境准备
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class HSwitcher {
    /*
     * HUp -> 标准启动 / Aeon启动
     */
    private static final Node<JsonObject> VISITOR = Ut.singleton(ZeroVertx.class);

    // Kinect
    private static HKinect H_KINECT;

    static {
        final JsonObject configuration = VISITOR.read();
        if (Ut.notNil(configuration)) {
            HLog.infoUp(HSwitcher.class, "Aeon System Enabled!");
            final JsonObject kinect = Ut.valueJObject(configuration, HName.KINECT);
            HLog.infoUp(HSwitcher.class, "Aeon `kinect` configuration = {0}.", kinect.encode());
            H_KINECT = Ut.deserialize(kinect, HKinect.class);
        }
    }

    public static boolean isAeon() {
        return Objects.nonNull(H_KINECT);
    }

}
