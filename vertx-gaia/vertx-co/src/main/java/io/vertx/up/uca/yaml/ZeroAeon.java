package io.vertx.up.uca.yaml;

import io.vertx.aeon.atom.iras.HAeon;
import io.vertx.aeon.atom.iras.HBoot;
import io.vertx.aeon.eon.HName;
import io.vertx.aeon.eon.HPath;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ZeroAeon implements Node<HAeon> {

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
        final JsonObject configJ = ZeroTool.readCloud(null, true);
        /*
         * 直接转换成 HAeon 配置
         * mode   = MIN, MIX, MIRROR
         * kinect - 开发专用代码管理
         * kidd   - 出厂设置代码管理
         * kzero  - 「只读」连接 vertx-zero-cloud 代码管理
         */
        final JsonObject aeonJ = Ut.valueJObject(configJ, HName.AEON);
        /*
         * aeon:
         *   name: app-xxx
         *   workspace: /var/tmp/xxx-cloud
         *   mode:
         *   repo:
         *     kzero:
         *     kinect:
         *     kidd:
         *   app:
         *     code:
         *     language:
         */
        final HAeon aeon = HAeon.configure(aeonJ);
        if (Objects.nonNull(aeon)) {
            this.assemble(aeon);
        }
        return aeon;
    }

    private void assemble(final HAeon aeon) {
        // zapp-axis.yml
        final JsonObject axisJ = ZeroTool.readCloud(HPath.AXIS, true);
        aeon.assemble(HBoot.configure(Ut.valueJObject(axisJ, HName.BOOT)));
    }
}
