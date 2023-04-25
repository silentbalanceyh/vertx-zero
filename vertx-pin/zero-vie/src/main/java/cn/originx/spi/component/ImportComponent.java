package cn.originx.spi.component;

import cn.originx.refine.Ox;
import cn.originx.scaffold.stdn.AbstractHFile;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.tp.atom.modeling.builtin.DataAtom;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.up.atom.record.Apt;
import io.vertx.up.commune.ActIn;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

/**
 * ## 「Channel」上传通道
 *
 * ### 1. 基本介绍
 *
 * 不带任何<strong>集成功能</strong>的标准上传通道，支持如下功能
 *
 * - 上传专用（导入完成）
 * - 请求`POST /api/ox/:identifier/import`
 *
 * ### 2. 通道详细
 *
 * - 类型：ADAPTOR
 *
 * ### 3. 请求格式
 *
 * > 文件上传直接处理
 *
 * ### 4. 响应格式
 *
 * ```json
 * // <pre><code class="json">
 * {
 *     "data": true
 * }
 * // </code></pre>
 * ```
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ImportComponent extends AbstractHFile {

    @Override
    public String diffKey() {
        return KName.CODE;
    }

    @Override
    public JsonArray preprocessDefault(final Apt apt) {
        return apt.dataS();
    }

    @Override
    public Future<JsonArray> transferIn(final JsonArray current) {
        return this.inImport(current, this.diffKey());
    }

    @Override
    public Future<JsonArray> transferAsync(final Apt apt, final ActIn request,
                                           final DataAtom atom) {
        /* 构造添加队列 / 更新队列 */
        final String user = request.userId();
        final Apt calculated = Ke.compmared(apt, user);
        return Ke.atomyFn(this.getClass(), calculated).apply(
            /*
             * 添加
             */
            inserted -> this.insertAsync(inserted, atom),
            /*
             * 更新
             */
            updated -> this.updateAsync(updated, calculated.dataO(), atom)
        );
    }

    private Future<JsonArray> updateAsync(final JsonArray input, final JsonArray previous, final DataAtom atom) {

        final JsonArray diffData = this.diffAdd(input, atom);

        /* 更新二期库 */
        return this.completer(atom).update(diffData)

            /* 生成变更历史 */
            .compose(newArray -> this.trackAsyncU(previous, newArray, atom));

    }

    private Future<JsonArray> insertAsync(final JsonArray input, final DataAtom atom) {
        final JsonArray diffData = this.diffAdd(input, atom);
        Ut.itJArray(diffData).forEach(each -> Ox.elementUuid(each, atom));

        /* 更新二期库 */
        return this.completer(atom).create(diffData)

            /* 生成变更历史 */
            .compose(newArray -> this.trackAsyncC(newArray, atom));
    }
}
