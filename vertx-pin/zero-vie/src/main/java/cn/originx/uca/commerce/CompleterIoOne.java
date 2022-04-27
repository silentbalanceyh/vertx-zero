package cn.originx.uca.commerce;

import cn.originx.uca.concrete.AgileAdd;
import cn.originx.uca.concrete.AgileDelete;
import cn.originx.uca.concrete.AgileEdit;
import cn.originx.uca.concrete.AgileFind;
import cn.originx.uca.log.Ko;
import cn.originx.uca.plugin.AgileSwitcher;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.builtin.DataAtom;
import io.vertx.tp.optic.robin.Switcher;
import io.vertx.up.experiment.mixture.HDao;
import io.vertx.up.unity.Ux;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class CompleterIoOne implements CompleterIo<JsonObject> {
    private final transient AgileSwitcher switcher;

    CompleterIoOne(final HDao dao, final DataAtom atom) {
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
