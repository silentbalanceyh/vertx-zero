package cn.originx.optic.component;

import cn.originx.refine.Ox;
import cn.originx.scaffold.component.AbstractConnector;
import io.vertx.core.Future;
import io.vertx.tp.atom.modeling.data.DataAtom;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.tp.error._409IdentifierConflictException;
import io.vertx.tp.plugin.excel.ExcelClient;
import io.vertx.tp.plugin.excel.ExcelInfix;
import io.vertx.tp.plugin.excel.atom.ExTable;
import io.vertx.up.commune.ActIn;
import io.vertx.up.commune.ActOut;
import io.vertx.up.commune.Record;
import io.vertx.up.fn.Fn;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
public class ImportComponent extends AbstractConnector {
    /**
     * 读取的Excel客户端插件引用，类型{@link ExcelClient}
     *
     * ```json
     * // <pre><code class="java">
     *     ExcelClient client = ExcelInfix.getClient();
     * // </code></pre>
     * ```
     */
    private final transient ExcelClient client = ExcelInfix.getClient();

    /**
     * 「Async」通道主方法
     *
     * @param request 通道的标准请求参数，类型{@link ActIn}。
     *
     * @return 返回`{@link Future}<{@link ActOut}>`
     */
    @Override
    public Future<ActOut> transferAsync(final ActIn request) {
        /*
         * 导入专用
         */
        final File[] files = request.getFiles();
        /*
         * 只支持单文件导入
         */
        Ox.Log.infoUca(this.getClass(), "上传文件数量：{0}", String.valueOf(files.length));
        if (0 < files.length) {
            final File file = files[0];
            /* 这里文件必定存在 */
            return Fn.getJvm(() -> {
                final InputStream inputStream = new FileInputStream(file);
                final Set<ExTable> tables = this.client.ingest(inputStream, true);
                final DataAtom atom = this.atom();
                /*
                 * 批量插入收集数据
                 */
                final String identifier = atom.identifier();
                /*
                 * 表检查
                 */
                final long counter = tables.stream()
                    .filter(table -> !table.getName().equals(identifier))
                    .count();
                if (0 < counter) {
                    Ox.Log.infoUca(this.getClass(), "文件规范错误，期望 identifier = {0}", identifier);
                    return ActOut.future(new _409IdentifierConflictException(this.getClass(), identifier));
                } else {
                    final List<Record> records = new ArrayList<>();
                    tables.stream()
                        .filter(table -> identifier.equals(table.getName()))
                        .map(exRecord -> Ao.records(atom, exRecord))
                        .forEach(records::addAll);
                    return this.dao().insertAsync(records.toArray(new Record[]{}))
                        .compose(nil -> ActOut.future(Boolean.TRUE));
                }
            }, file);
        } else {
            return ActOut.future(Boolean.FALSE);
        }
    }
}
