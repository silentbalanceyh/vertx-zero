package io.modello.atom.app;

import io.horizon.exception.boot.CombineAppException;
import io.horizon.runtime.Macrocosm;
import io.horizon.util.HUt;
import io.macrocosm.specification.app.HApp;
import io.vertx.core.json.JsonObject;

import java.util.Objects;

/**
 * @author lang : 2023-06-06
 */
public class KApp implements HApp {

    private final String name;
    private final JsonObject configuration = new JsonObject();
    private final String ns;

    public KApp(final String name) {
        final String nameApp = HUt.envWith(Macrocosm.Z_APP, name);
        // 应用名称
        this.name = nameApp;
        // 名空间
        this.ns = HUt.nsApp(nameApp);
    }

    @Override
    public JsonObject option() {
        return this.configuration;
    }

    @Override
    public void option(final JsonObject configuration, final boolean clear) {
        if (HUt.isNil(configuration)) {
            return;
        }
        if (clear) {
            this.configuration.clear();
        }
        this.configuration.mergeIn(HUt.valueJObject(configuration), true);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T option(final String key) {
        return (T) this.configuration.getValue(key, null);
    }

    @Override
    public <T> void option(final String key, final T value) {
        this.configuration.put(key, value);
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public String ns() {
        return this.ns;
    }

    @Override
    public HApp apply(final HApp target) {
        if (Objects.isNull(target)) {
            return this;
        }
        if (target.equals(this)) {
            this.option().mergeIn(HUt.valueJObject(target.option()));
            return this;
        } else {
            throw new CombineAppException(this.getClass(), this.ns, this.name);
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final KApp kApp = (KApp) o;
        return Objects.equals(this.name, kApp.name) && Objects.equals(this.ns, kApp.ns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.ns);
    }
}
