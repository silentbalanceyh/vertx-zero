package io.mature.extension.stellaris.vendor;

import io.macrocosm.specification.app.HApp;
import io.macrocosm.specification.program.HArk;
import io.mature.extension.stellaris.OkA;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.exchange.BMapping;
import io.vertx.up.atom.exchange.BTree;
import io.vertx.up.atom.exchange.DFabric;
import io.vertx.up.atom.exchange.DSetting;
import io.vertx.up.atom.typed.UTenant;
import io.vertx.up.commune.config.Database;
import io.vertx.up.eon.KName;
import io.vertx.up.uca.cache.RapidKey;
import io.vertx.up.unity.Ux;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractParty implements OkB {
    protected final transient OkA partyA;

    protected AbstractParty(final OkA partyA) {
        this.partyA = partyA;
    }

    /**
     * 获取数据库对象默认实现，调用`Uncommon.database()`
     *
     * > 「RD」桥梁模式扩展。
     *
     * @return {@link Database}
     */
    @Override
    public Database configDatabase() {
        return this.partyA.configDatabase();
    }

    /**
     * 获取应用配置对象默认实现，调用`Uncommon.application()`
     *
     * > 「RD」桥梁模式扩展
     *
     * @return {@link io.macrocosm.specification.program.HArk}
     */
    @Override
    public HArk configApp() {
        return this.partyA.configApp();
    }

    /**
     * 「Abstract」子类必须实现的方法，用于构建字典翻译器专用配置
     *
     * @return {@link Future}<{@link DSetting}>
     */
    abstract protected Future<DSetting> configDict();

    /**
     * 「Async」根据统一标识符异步构造某一个模型的字典翻译器
     *
     * @param identifier {@link String} 传入的模型统一标识符
     *
     * @return `{@link Future}<{@link DFabric}>`
     */
    @Override
    public Future<DFabric> fabric(final String identifier) {
        final MultiMap params = this.input(identifier);
        params.add(KName.CACHE_KEY, RapidKey.JOB_DIRECTORY);
        return this.configDict().compose(dict -> Ux.dictCalc(dict, params).compose(dictData -> {
            final BTree mapping = this.mapping();
            final BMapping mappingItem = mapping.child(identifier);
            /*
             * DualItem
             */
            final DFabric fabric = DFabric.create(mappingItem)
                .epsilon(dict.getEpsilon())
                .dictionary(dictData);
            return Ux.future(fabric);
        }));
    }

    /**
     * 根据传入的模型统一标识符构造统一的参数哈希表
     *
     * 哈希表结构：
     *
     * ```json
     * // <pre><code class="json">
     * {
     *     "identifier": "模型标识符",
     *     "sigma": "统一标识符",
     *     "appId": "应用主键，对应X_APP中的key值"
     * }
     * // </code></pre>
     * ```
     *
     * @param identifier {@link String} 模型统一标识符
     *
     * @return {@link MultiMap} 返回参数哈希表
     */
    private MultiMap input(final String identifier) {
        final MultiMap params = MultiMap.caseInsensitiveMultiMap();
        params.add(KName.IDENTIFIER, identifier);
        /*
         * 应用处理
         */
        final JsonObject data = this.inputData();
        params.add(KName.SIGMA, data.getString(KName.SIGMA));
        params.add(KName.APP_ID, data.getString(KName.APP_ID));
        return params;
    }

    protected JsonObject inputData() {
        final JsonObject params = new JsonObject();
        final HArk ark = this.partyA.configApp();
        if (Objects.isNull(ark)) {
            final UTenant tenant = this.partyA.partyA();
            final JsonObject application = tenant.getApplication();
            params.put(KName.SIGMA, application.getString(KName.SIGMA));
            params.put(KName.APP_ID, application.getString(KName.APP_ID));
        } else {
            final HApp app = ark.app();
            params.put(KName.APP_ID, app.option(KName.APP_ID));
            params.put(KName.SIGMA, app.option(KName.SIGMA));
        }
        return params;
    }
}
