package cn.originx.optic.component;

import cn.originx.refine.Ox;
import cn.originx.scaffold.component.AbstractAdaptor;
import cn.originx.scaffold.plugin.AspectSwitcher;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.builtin.DataAtom;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.plugin.excel.ExcelClient;
import io.vertx.tp.plugin.excel.ExcelInfix;
import io.vertx.up.atom.query.engine.Qr;
import io.vertx.up.commune.ActIn;
import io.vertx.up.commune.ActOut;
import io.vertx.up.commune.Envelop;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * ## 「Channel」导出通道
 *
 * ### 1. 基本介绍
 *
 * 不带任何<strong>集成功能</strong>的导出通道，支持如下功能
 *
 * - 根据`identifier`执行数据导出
 * - 可输入导出的查询条件以及列条件
 * - 请求`POST /api/ox/:identifier/export`
 *
 * > 导出格式是Excel二进制文件流。
 *
 * ### 2. 通道详细
 *
 * - 类型：ADAPTOR
 * - Qr参数中的`criteria`部分
 *
 * ### 3. 请求格式
 *
 * > 参考Zero查询引擎中的Qr标准格式。
 *
 * ```json
 * // <pre><code class="json">
 * {
 *     "criteria":{
 *          "field1,=": "value1",
 *          "$op":{
 *              "field2,<": "value2",
 *              "field2,>": "value3"
 *          }
 *     },
 *     "columns": [
 *          "column1",
 *          "column2",
 *          "..."
 *     ]
 * }
 * // </code></pre>
 * ```
 *
 * ### 4. 响应格式
 *
 * ```shell
 * // <pre><code>
 * ....（二进制数据）
 * // </code></pre>
 * ```
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ExportComponent extends AbstractAdaptor {

    /**
     * 「Async」通道主方法
     *
     * @param request 通道的标准请求参数，类型{@link ActIn}。
     *
     * @return 返回`{@link Future}<{@link ActOut}>`
     */
    @Override
    public Future<ActOut> transferAsync(final ActIn request) {
        final DataAtom atom = this.atom();
        final Envelop envelop = request.getEnvelop();
        final JsonObject body = request.getJObject();
        /*
         * 导出的头 / 将移除的列
         */
        final ConcurrentMap<String, String> exported =
            new ConcurrentHashMap<>();
        final JsonArray removed = new JsonArray();

        final String identifier = atom.identifier();
        final ExcelClient client = ExcelInfix.getClient();

        /*
         * 导出的头信息
         */
        final List<String> exportedColumns = new ArrayList<>();

        /*
         * 请求顺序
         * 1. 导入导出的第一阶段：读取列信息
         */
        return Ox.viewFull(envelop, identifier)

            /*
             * 初始化列
             */
            .compose(columns -> {
                /*
                 * 后期需要使用，放在外围
                 */
                final List<String> columnField = new ArrayList<>();

                /*
                 * 列标题：title，列字段：dataIndex
                 */
                Ut.itJArray(columns).forEach(column -> {
                    final String key = column.getString("dataIndex");
                    final String label = column.getString("title");
                    /*
                     * 提取数据，形成 header / data 两部分
                     */
                    if (Ut.notNil(key) && Ut.notNil(label)) {
                        exported.put(key, label);
                        columnField.add(key);
                    }
                });

                return Ux.future(columnField);
            })

            /*
             * 列计算
             */
            .compose(columnList -> {
                /* 前端传入参数 */
                final JsonArray expected = Ut.valueJArray(body.getJsonArray("columns"));

                /* 包含的内容在 includes */
                final Set<String> includes = Ut.toSet(expected);
                final Set<String> calculated = new HashSet<>();
                columnList.forEach(column -> {
                    if (includes.contains(column)) {
                        calculated.add(column);
                    } else {
                        removed.add(column);
                    }
                });

                /* 导出专用 */
                expected.stream()
                    .filter(Objects::nonNull)
                    .map(column -> (String) column)
                    .forEach(exportedColumns::add);

                /* 构造最终计算出来带顺序的列 */
                return Ux.future(Ut.toJArray(calculated));
            })

            /*
             * 计算列过滤
             * */
            .compose(projection -> {
                /* 查询条件 */
                final JsonObject criteria = body.getJsonObject(Qr.KEY_CRITERIA);

                /* 基本查询，当前 sigma */
                final JsonObject query = new JsonObject();
                if (Objects.isNull(criteria)) {
                    query.put(Qr.KEY_CRITERIA, new JsonObject());
                } else {
                    query.put(Qr.KEY_CRITERIA, criteria);
                }

                query.put(Qr.KEY_PROJECTION, projection);
                return Ux.future(query);
            })
            .compose(query -> this.dao().searchAsync(query))
            /*
             * 字典翻译数据
             */
            .compose(response -> {
                /* 提取将要导出的数据信息 */
                final JsonArray data = Ut.valueJArray(response.getJsonArray("list"));
                /* 字典翻译 KeField.Api.EPSILON */
                final JsonObject epsilonJson = this.options().getJsonObject("epsilon");
                return this.fabric(epsilonJson).inTo(data);
            }).compose(data -> {
                /* 留下列信息 */
                removed.stream().map(item -> (String) item).forEach(exported::remove);
                /* 开启插件AOP */
                final AspectSwitcher aspect = new AspectSwitcher(atom, this.options(), this.fabric(atom));
                /* 合并表格 */
                return aspect.run(data, Ux::future)
                    .compose(processed -> Ke.combineAsync(processed, exported, exportedColumns, atom.shape()));
            })
            .compose(data -> client.exportAsync(identifier, data, atom.shape()))
            .compose(ActOut::future);
    }
}
