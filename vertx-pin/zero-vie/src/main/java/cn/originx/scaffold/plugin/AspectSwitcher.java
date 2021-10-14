package cn.originx.scaffold.plugin;

import cn.originx.refine.Ox;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.data.DataAtom;
import io.vertx.tp.optic.plugin.AspectPlugin;
import io.vertx.up.commune.exchange.DiFabric;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.function.Function;

/*
 * 横切专用处理，用于加载
 * 1）前置插件
 * 2）后置插件
 * 目前整个 Ox 支持的插件
 * - plugin.component：     主插件
 * - plugin.compressor：    批量执行插件（JsonArray -> JsonObject）
 * - plugin.todo:           生成TODO待办的插件
 * - plugin.activity:       生成变更历史专用插件
 */
public class AspectSwitcher {
    private final transient JsonObject options;
    private final transient AspectPlugin plugin;

    public AspectSwitcher(final DataAtom atom, final JsonObject optionsInput, final DiFabric fabric) {
        /* 合并横切配置 */
        final JsonObject options = new JsonObject();
        options.mergeIn(Ut.sureJObject(optionsInput));
        this.options = options;
        /* identifier 真实替换 */
        this.options.put(KName.IDENTIFIER, atom.identifier());
        /*
         * 使用横切配置初始化插件
         * 如果初始化成功：plugin != null，则绑定 DataAtom 到插件中
         * */
        this.plugin = Ox.pluginComponent(options);
        if (Objects.nonNull(this.plugin)) {
            this.plugin.bind(atom);
            if (Objects.nonNull(fabric)) {
                /*
                 * 「字典翻译器的BUG」
                 * 每个插件的字典翻译器使用直接构造时的拷贝
                 * 1. 每个插件共享的是字典翻译器的拷贝
                 * 2. 由于某个字典翻译器很有可能被重设，每个插件在执行时相互之间不影响
                 * 3. 防止字典翻译器穿透的情况发生
                 *
                 * 每个插件必须使用独立的字典翻译器，插件使用的字典翻译器由通道组件来创建拷贝
                 */
                this.plugin.bind(fabric.createCopy());
            }
            Ox.Log.infoHub(this.getClass(), "( plugin.component ) 核心插件: 模型 identifier = {0}, 插件：{1}，配置：{2}",
                atom.identifier(), this.getClass().getName(), optionsInput.encode());
        }
    }

    /*
     * 前置处理
     */
    private Future<JsonArray> beforeAsync(final JsonArray input) {
        if (Objects.isNull(this.plugin)) {
            return Ux.future(input);
        } else {
            return this.plugin.beforeAsync(input, this.options);
        }
    }

    /*
     * 后置处理
     */
    private Future<JsonArray> afterAsync(final JsonArray input) {
        if (Objects.isNull(this.plugin)) {
            return Ux.future(input);
        } else {
            return this.plugin.afterAsync(input, this.options);
        }
    }

    /*
     * 前置处理
     */
    private Future<JsonObject> beforeAsync(final JsonObject input) {
        if (Objects.isNull(this.plugin)) {
            return Ux.future(input);
        } else {
            return this.plugin.beforeAsync(input, this.options);
        }
    }

    /*
     * 后置处理
     */
    private Future<JsonObject> afterAsync(final JsonObject input) {
        if (Objects.isNull(this.plugin)) {
            return Ux.future(input);
        } else {
            return this.plugin.afterAsync(input, this.options);
        }
    }

    /*
     * 单条记录执行前后插件
     */
    public Future<JsonObject> run(final JsonObject input, final Function<JsonObject, Future<JsonObject>> runner) {
        final JsonObject params = Ut.sureJObject(input);
        return Ux.future(params)
            /*
             * 前置处理
             */
            .compose(this::beforeAsync)
            /*
             * 主逻辑
             */
            .compose(runner)
            /*
             * 后置请求处理，只有 Success 时执行
             */
            .compose(this::afterAsync);
    }

    /*
     * 多条记录执行前后插件
     */
    public Future<JsonArray> run(final JsonArray input, final Function<JsonArray, Future<JsonArray>> runner) {
        final JsonArray params = Ut.sureJArray(input);
        return Ux.future(params)
            /*
             * 前置处理
             */
            .compose(this::beforeAsync)
            /*
             * 主逻辑
             */
            .compose(runner)
            /*
             * 后置请求处理，只有 Success 时执行
             */
            .compose(this::afterAsync);
    }
}
