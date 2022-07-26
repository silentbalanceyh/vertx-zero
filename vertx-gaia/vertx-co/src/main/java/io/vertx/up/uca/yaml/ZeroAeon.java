package io.vertx.up.uca.yaml;

import io.vertx.aeon.atom.configuration.HAeon;
import io.vertx.aeon.eon.HName;
import io.vertx.aeon.eon.HPath;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ZeroAeon implements Node<HAeon> {

    private static final JsonObject CONFIG_J = ZeroTool.readCloud(null, true);

    @Override
    public HAeon read() {
        /*
         * 检查配置文件 aeon/zapp.yml
         * 1. 如果配置文件存在则启用 Cloud 环境。
         * 2. 解析基础Json转换成 HAeon 核心配置对象。
         */
        if (!Ut.ioExist(HPath.P_ZAPP)) {
            return null;
        }

        /*
         * 直接转换成 HAeon 配置
         * mode   = MIN, MIX, MIRROR
         * kinect - 开发专用代码管理
         * kidd   - 出厂设置代码管理
         * kzero  - 「只读」连接 vertx-zero-cloud 代码管理
         */
        final JsonObject aeonJ = Ut.valueJObject(CONFIG_J, HName.AEON);
        return HAeon.configure(aeonJ);
    }
}
