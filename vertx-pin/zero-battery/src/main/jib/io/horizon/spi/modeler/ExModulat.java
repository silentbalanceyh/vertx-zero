package io.horizon.spi.modeler;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.battery.atom.PowerApp;
import io.vertx.mod.battery.uca.dock.Ark;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ExModulat implements Modulat {
    @Override
    public Future<JsonObject> extension(final JsonObject appJson) {
        final String key = appJson.getString(KName.KEY);
        return this.extension(key).compose(moduleJ -> {
            /*
             * appJ + moduleJ to build response ( Final )
             */
            final JsonObject original = moduleJ.copy();
            original.mergeIn(appJson, true);
            return Ux.future(original);
        });
    }

    /*
     * {
     *     "configKey1": {},
     *     "configKey2": {}
     * }
     */
    @Override
    public Future<JsonObject> extension(final String appId) {
        Objects.requireNonNull(appId);
        final JsonObject appJ = new JsonObject();
        // 解决无法连接导致AppId为空的问题
        appJ.put(KName.KEY, appId);
        return this.moduleAdmin(appId)
            .compose(moduleJ -> {
                appJ.mergeIn(moduleJ, true);
                return Ux.future(appJ);
            })
            .compose(nil -> this.moduleBag(appId))
            .compose(bagJ -> {
                appJ.mergeIn(bagJ, true);
                return Ux.future(appJ);
            });
    }

    /*
     * 该方法会提取应用存储专用的配置信息，该配置信息会被存储到 B_BLOCK 表中，开发中心可修订
     * 配置程序，如：
     * {
     *     "store": "mXXX"
     * }
     * 格式，最终使用时用
     * mXXX = configuration
     * 的 JSON 格式
     *
     * 输入的格式
     */
    private Future<JsonObject> moduleAdmin(final String appId) {
        return PowerApp.CCA_BAG_ADMIN.pick(() -> {
            final Ark ark = Ark.configure();
            return ark.modularize(appId)
                .compose(data -> Ux.future((JsonObject) data));
        }, appId);
    }

    /*
     * This is for bags extracting from the system, the condition for X_BAG is as
     *
     * ENTRY = true AND ENTRY_ID __ NOT NULL
     *
     * It will build the entry of all modules as ( Bag = App ) in our platform instead.
     * All the configuration page will be built in above `moduleAdmin` method for detail configuration.
     */
    private Future<JsonObject> moduleBag(final String appId) {
        return PowerApp.CCA_BAG_DATA.pick(() -> {
            final Ark ark = Ark.bag();
            return ark.modularize(appId)
                .compose(data -> Ux.future((JsonArray) data));
        }, appId).compose(bags -> {
            final JsonObject response = new JsonObject();
            response.put(KName.App.BAGS, bags);
            return Ux.future(response);
        });
    }
}
