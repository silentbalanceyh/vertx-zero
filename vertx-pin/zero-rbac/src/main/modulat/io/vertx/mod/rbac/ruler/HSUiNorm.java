package io.vertx.mod.rbac.ruler;

import io.aeon.atom.secure.KPermit;
import io.aeon.experiment.specification.secure.AbstractAdmit;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.ruler.element.HAdmitCompiler;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class HSUiNorm extends AbstractAdmit {

    @Override
    public Future<JsonObject> configure(final KPermit permit, final JsonObject requestJ) {
        return super.configure(permit, requestJ, KPermit::uiJ);
    }

    @Override
    @SuppressWarnings("all")
    public Future<JsonObject> compile(final KPermit permit, final JsonObject request) {
        /* 提取类型和参数 */
        final HAdmitCompiler compiler = HAdmitCompiler.create(permit, getClass());

        final JsonObject config = Ut.valueJObject(request, KName.Rbac.UI);
        final JsonObject qr = Ut.valueJObject(config, KName.Rbac.QR);

        /*
         * output 定义
         * {
         *     "group": "type"
         * }
         * 1）如果定义了 group 则按字段对 data 执行分组
         * 2）否则直接处理 data 节点的数据
         */
        final JsonObject output = Ut.valueJObject(config, KName.OUTPUT);
        return compiler.ingest(qr, config).compose(data -> Fn.ifJArray(data, output));
    }
}
