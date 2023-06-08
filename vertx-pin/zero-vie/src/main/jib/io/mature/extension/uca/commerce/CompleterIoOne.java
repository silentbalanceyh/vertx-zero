package io.mature.extension.uca.commerce;

import io.horizon.spi.robin.Switcher;
import io.mature.extension.uca.concrete.AgileAdd;
import io.mature.extension.uca.concrete.AgileDelete;
import io.mature.extension.uca.concrete.AgileEdit;
import io.mature.extension.uca.concrete.AgileFind;
import io.mature.extension.uca.log.Ko;
import io.mature.extension.uca.plugin.AgileSwitcher;
import io.modello.specification.action.HDao;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.atom.modeling.builtin.DataAtom;
import io.vertx.up.unity.Ux;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class CompleterIoOne implements CompleterIo<JsonObject> {
    private final transient AgileSwitcher switcher;

    protected CompleterIoOne(final HDao dao, final DataAtom atom) {
        this.switcher = new AgileSwitcher().initialize(atom, dao);
    }

    @Override
    public CompleterIo<JsonObject> bind(final Switcher switcher) {
        this.switcher.bind(switcher);
        return this;
    }

    @Override
    public Future<JsonObject> create(final JsonObject input) {
        return this.switcher.switchAsync(input, AgileAdd::new)
            .compose(arrow -> arrow.processAsync(input), this::failure);
    }

    @Override
    public Future<JsonObject> update(final JsonObject input) {
        return this.switcher.switchAsync(input, AgileEdit::new)
            .compose(arrow -> arrow.processAsync(input), this::failure);
    }

    @Override
    public Future<JsonObject> remove(final JsonObject input) {
        return this.switcher.switchAsync(input, AgileDelete::new)
            .compose(arrow -> arrow.processAsync(input), this::failure);
    }

    @Override
    public Future<JsonObject> find(final JsonObject input) {
        return this.switcher.switchAsync(input, AgileFind::new)
            .compose(arrow -> arrow.processAsync(input), this::failure);
    }

    protected Future<JsonObject> failure(final Throwable error) {
        if (Objects.nonNull(error)) {
            error.printStackTrace();
        }
        Ko.db(this.getClass(), this.switcher.atom(), error);
        return Ux.futureJ();
    }
}
