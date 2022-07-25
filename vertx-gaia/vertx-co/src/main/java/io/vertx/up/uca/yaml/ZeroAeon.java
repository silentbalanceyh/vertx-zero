package io.vertx.up.uca.yaml;

import io.vertx.aeon.atom.configuration.HAeon;
import io.vertx.aeon.eon.HPath;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ZeroAeon implements Node<HAeon> {

    @Override
    public HAeon read() {
        /*
         * 检查配置文件 aeon/zapp.yml
         * 1. 如果配置文件存在则启用 Cloud 环境。
         */
        if (!Ut.ioExist(HPath.P_ZAPP)) {
            return null;
        }
        final JsonObject configuration = ZeroTool.readCloud(null, true);
        /*
         * 双分流读取数据
         */
        return null;
    }
}
