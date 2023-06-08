package io.macrocosm.atom.boot;

import io.horizon.eon.VSpec;
import io.horizon.eon.em.EmApp;
import io.horizon.util.HUt;
import io.macrocosm.specification.config.HBoot;
import io.macrocosm.specification.config.HEnergy;
import io.vertx.core.json.JsonObject;

/**
 * @author lang : 2023-05-31
 */
public class KBoot implements HBoot {
    private final HEnergy energy;
    private final Class<?> launcherCls;
    private Class<?> mainClass;
    private String[] arguments;

    private EmApp.Type type;

    private KBoot(final JsonObject bootJ) {
        this.launcherCls = HUt.valueC(bootJ, VSpec.Boot.LAUNCHER);
        this.energy = KEnergy.of(bootJ);
    }

    public static HBoot of(final JsonObject bootJ) {
        return new KBoot(bootJ);
    }

    @Override
    public HBoot bind(final Class<?> mainClass, final String... arguments) {
        this.mainClass = mainClass;
        this.arguments = arguments;
        return this;
    }

    @Override
    public EmApp.Type app() {
        return this.type;
    }

    @Override
    public HBoot app(final EmApp.Type app) {
        this.type = app;
        return this;
    }

    @Override
    public Class<?> target() {
        return this.mainClass;
    }

    @Override
    public Class<?> launcher() {
        return this.launcherCls;
    }

    @Override
    public HEnergy energy() {
        return this.energy;
    }

    @Override
    public String[] args() {
        return this.arguments;
    }
}
