package io.aeon.atom.iras;

import io.aeon.eon.HName;
import io.aeon.refine.HLog;
import io.aeon.runtime.H1H;
import io.horizon.specification.cloud.action.HEvent;
import io.horizon.specification.cloud.boot.HOff;
import io.horizon.specification.cloud.boot.HOn;
import io.horizon.specification.cloud.boot.HRun;
import io.horizon.specification.cloud.program.HNebula;
import io.horizon.specification.cloud.program.HNova;
import io.horizon.specification.cloud.program.HNovae;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * 「启动组件」
 * 启动组件专用配置
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class HBoot implements Serializable {
    private static final ConcurrentMap<String, Class<?>> STORE_DEFINE = new ConcurrentHashMap<>() {
        {
            // 生命周期管理：启动、运行、停止专用组件
            // component
            this.put(HName.ON, HOn.class);                      // on
            this.put(HName.OFF, HOff.class);                    // off
            this.put(HName.RUN, HRun.class);                    // run

            // 和GitHub集成专用结构
            // alive
            this.put(HName.ALIVE_NOVAE, HNovae.class);          // novae
            this.put(HName.ALIVE_NOVA, HNova.class);            // nova
            this.put(HName.ALIVE_NEBULA, HNebula.class);        // nebula
        }
    };
    /*
     * 内部哈希表 map，存储结构如：
     * -- 接口名 = 实现类
     * 最终实例会链接到 CCT_EVENT 的组件缓存池，缓存键维度
     * -- 1）线程名
     * -- 2）类型名
     * 最终都是 HEvent 实例
     */
    private final ConcurrentMap<Class<?>, Class<?>> store = new ConcurrentHashMap<>();

    private HBoot(final JsonObject configJ) {
        // component + alive
        final JsonObject componentJ = Ut.valueJObject(configJ, HName.COMPONENT);
        final JsonObject aliveJ = Ut.valueJObject(configJ, HName.ALIVE);

        final JsonObject sourceJ = componentJ.copy().mergeIn(aliveJ, true);
        STORE_DEFINE.forEach((name, interfaceCls) -> {
            final Class<?> instanceCls = Ut.valueCI(sourceJ, name, interfaceCls);
            if (Objects.nonNull(instanceCls)) {
                // 接口 = 实现类
                this.store.put(interfaceCls, instanceCls);
            }
        });
        HLog.infoAeon(this.getClass(),
            "Aeon system detect ( size = {0} with keys = {1} ) components defined.",
            String.valueOf(this.store.size()),
            Ut.fromJoin(this.store.keySet().stream().map(Class::getName).collect(Collectors.toSet())));
    }

    public static HBoot configure(final JsonObject configJ) {
        final JsonObject configuration = Ut.valueJObject(configJ);
        return H1H.CC_BOOT.pick(() -> new HBoot(configuration), configuration.hashCode());
    }


    public <C> C pick(final Class<?> interfaceCls, final Vertx vertx) {
        return this.pick(interfaceCls, vertx, null);
    }

    @SuppressWarnings("all")
    public <C> C pick(final Class<?> interfaceCls, final Vertx vertx, final Class<?> defaultCls) {
        Objects.requireNonNull(interfaceCls);
        final Class<?> instanceCls = this.store.getOrDefault(interfaceCls, defaultCls);
        if (Objects.isNull(instanceCls)) {
            return null;
        }
        final HEvent event = H1H.CCT_EVENT.pick(() -> {
            final HEvent instance = Ut.instance(instanceCls);
            instance.bind(vertx);
            return instance;
        }, instanceCls.getName());
        HLog.infoAeon(getClass(), "Pick instance class {0} of {1} from component cached/pool.",
            instanceCls, interfaceCls);
        return (C) event;
    }
}
