package io.vertx.up.runtime;

import io.horizon.eon.VSpec;
import io.horizon.eon.VString;
import io.horizon.spi.boot.HEquip;
import io.macrocosm.atom.boot.KSetting;
import io.macrocosm.specification.config.HConfig;
import io.macrocosm.specification.config.HSetting;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.KPlugin;
import io.vertx.up.eon.configure.YmlCore;
import io.vertx.up.util.Ut;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lang : 2023-05-30
 */
public class ZeroEquip implements HEquip {
    /**
     * @return {@link io.macrocosm.atom.boot.KSetting}
     */
    @Override
    public HSetting initialize() {
        /*
         * 基础装配 vertx.yml 读取
         * - up/config/vertx.yml
         * - vertx.yml
         *
         * zero:
         *    lime: key1,key2,key3
         * boot:
         *
         */
        final JsonObject configuration = ZeroIo.read(null, true);
        final HSetting setting = KSetting.of();
        final JsonObject configZero = Ut.valueJObject(configuration, KName.Internal.ZERO);
        setting.container(HConfig.of(configZero));
        final String extension = Ut.valueString(configZero, YmlCore.LIME);

        final JsonObject configBoot = Ut.valueJObject(configuration, VSpec.Boot.__KEY);
        setting.launcher(HConfig.of(configBoot));

        /*
         * zero:
         *    lime: key1,key2,key3
         */
        final JsonObject configExtension = new JsonObject();
        final Set<String> keys = Ut.toSet(extension, VString.COMMA);
        final Set<String> internal = Arrays.stream(KPlugin.FILE_KEY)
            .collect(Collectors.toSet());
        keys.stream().filter(field -> !internal.contains(field)).forEach(field -> {
            // lime file
            // - vertx-key1.yml
            // - vertx-key2.yml
            // - vertx-key3.yml
            final JsonObject fileData = ZeroIo.read(field, false);
            configExtension.mergeIn(fileData, true);
        });
        /*
         * 文件合并处理，所有 key 值
         */
        Ut.<JsonObject>itJObject(configExtension).forEach(entry -> {
            final String key = entry.getKey();
            setting.infix(key, HConfig.of(entry.getValue()));
        });
        /*
         * <Internal>
         * server = {}
         * inject = {}
         * error = {}
         * resolver = {}
         */
        Arrays.stream(KPlugin.FILE_KEY).forEach(field -> {
            final JsonObject fileData = ZeroIo.read(field, true);
            setting.infix(field, HConfig.of(fileData));
        });
        return setting;
    }
}
