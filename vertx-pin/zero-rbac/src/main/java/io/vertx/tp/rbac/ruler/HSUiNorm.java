package io.vertx.tp.rbac.ruler;

import io.vertx.aeon.atom.secure.HPermit;
import io.vertx.aeon.specification.secure.AbstractAdmit;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.ruler.element.HAdmitCompiler;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class HSUiNorm extends AbstractAdmit {

    @Override
    public Future<JsonObject> configure(final HPermit permit) {
        /*
         * Ui 的配置流程，配置完成后会输入
         * {
         *     "surface": uiSurface,
         *     "qr":      uiQr
         * }
         */
        final JsonObject request = permit.uiJ();
        final JsonObject qrJ = Ut.valueJObject(request, KName.Rbac.QR);
        request.put(KName.Rbac.QR, this.valueQr(qrJ, null));
        return Future.succeededFuture(request);
    }

    @Override
    @SuppressWarnings("all")
    public Future<JsonObject> compile(final HPermit permit, final JsonObject request) {
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
        return compiler.ingest(qr, config).compose(data -> Fn.wrapJ(data, output));
    }
}
