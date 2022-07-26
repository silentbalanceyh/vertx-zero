package io.vertx.aeon.atom.iras;

import io.vertx.aeon.eon.HCache;
import io.vertx.aeon.eon.HName;
import io.vertx.aeon.specification.boot.HOn;
import io.vertx.aeon.specification.program.HAlive;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class HBoot implements Serializable {
    private final Set<Class<?>> alive = new HashSet<>();
    private final Class<?> componentOn;
    private Class<?> componentOff;
    private Class<?> componentRun;

    private HBoot(final JsonObject configJ) {
        // component
        final JsonObject componentJ = Ut.valueJObject(configJ, HName.COMPONENT);
        this.componentOn = Ut.valueClass(componentJ, HName.ON);
        // alive
        final JsonArray aliveA = Ut.valueJArray(configJ, HName.ALIVE);
        this.alive.addAll(Ut.toClass(aliveA)
            .stream()
            // clazz必须实现HAlive接口
            .filter(clazz -> Ut.isImplement(clazz, HAlive.class))
            .collect(Collectors.toSet()));
    }

    public static HBoot configure(final JsonObject configJ) {
        final JsonObject configuration = Ut.valueJObject(configJ);
        return HCache.CC_BOOT.pick(() -> new HBoot(configuration), configuration.hashCode());
    }

    // Component: on / off / run
    public HOn pickOn(final Class<?> defaultClass) {
        final Class<?> clazzOn = Objects.isNull(this.componentOn) ? defaultClass : this.componentOn;
        return HCache.CCT_ON.pick(() -> Ut.instance(clazzOn), clazzOn.getName());
    }

    // alive references to build list
    public Set<Class<?>> alive() {
        return this.alive;
    }
}
