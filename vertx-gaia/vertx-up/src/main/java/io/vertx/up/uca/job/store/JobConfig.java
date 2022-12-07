package io.vertx.up.uca.job.store;

import io.vertx.up.atom.config.ComponentOption;
import io.vertx.up.uca.job.timer.VertxInterval;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/*
 * Job configuration in `vertx-job.yml`, the job node
 * job:
 * - store:
 *   - component:
 *   - config:
 * - interval:
 *   - component:
 *   - config:
 * - client:
 *   - config:
 */
public class JobConfig implements Serializable {

    private transient ComponentOption store;
    private transient ComponentOption interval;
    private transient ComponentOption client;

    public ComponentOption getStore() {
        return Optional.ofNullable(this.store).orElse(new ComponentOption());
    }

    public void setStore(final ComponentOption store) {
        this.store = store;
    }

    public ComponentOption getInterval() {
        final ComponentOption componentOption = Optional.ofNullable(this.interval).orElse(new ComponentOption());
        if (Objects.isNull(componentOption.getComponent())) {
            componentOption.setComponent(VertxInterval.class);
        }
        return componentOption;
    }

    public void setInterval(final ComponentOption interval) {
        this.interval = interval;
    }

    public ComponentOption getClient() {
        return Optional.ofNullable(this.client).orElse(new ComponentOption());
    }

    public void setClient(final ComponentOption client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "JobConfig{" +
            "store=" + this.store +
            ", interval=" + this.interval +
            ", client=" + this.client +
            '}';
    }
}
