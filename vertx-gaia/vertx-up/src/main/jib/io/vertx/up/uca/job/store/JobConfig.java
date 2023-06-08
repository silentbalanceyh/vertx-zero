package io.vertx.up.uca.job.store;

import io.vertx.up.atom.element.JComponent;
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

    private transient JComponent store;
    private transient JComponent interval;
    private transient JComponent client;

    public JComponent getStore() {
        return Optional.ofNullable(this.store).orElse(new JComponent());
    }

    public void setStore(final JComponent store) {
        this.store = store;
    }

    public JComponent getInterval() {
        final JComponent componentOption = Optional.ofNullable(this.interval).orElse(new JComponent());
        if (Objects.isNull(componentOption.getComponent())) {
            componentOption.setComponent(VertxInterval.class);
        }
        return componentOption;
    }

    public void setInterval(final JComponent interval) {
        this.interval = interval;
    }

    public JComponent getClient() {
        return Optional.ofNullable(this.client).orElse(new JComponent());
    }

    public void setClient(final JComponent client) {
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
