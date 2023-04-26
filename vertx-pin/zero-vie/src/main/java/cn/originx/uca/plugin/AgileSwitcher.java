package cn.originx.uca.plugin;

import cn.originx.uca.concrete.Arrow;
import io.horizon.specification.modeler.HDao;
import io.horizon.spi.robin.Switcher;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.builtin.DataAtom;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.function.Supplier;

import static io.vertx.tp.atom.refine.Ao.LOG;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AgileSwitcher {
    private transient DataAtom atom;
    private transient HDao dao;

    private transient boolean dynamic;
    private transient Switcher switcher;

    public AgileSwitcher() {
        this.dynamic = false;
    }

    public AgileSwitcher bind(final Switcher switcher) {
        this.switcher = switcher;
        this.dynamic = true;
        return this;
    }

    public AgileSwitcher initialize(final DataAtom atom, final HDao dao) {
        this.atom = atom;
        this.dao = dao;
        return this;
    }

    public DataAtom atom() {
        return this.atom;
    }

    public Future<Arrow> switchAsync(final JsonObject data, final Supplier<Arrow> supplier) {
        if (this.dynamic) {
            if (Objects.isNull(this.switcher)) {
                // Switcher is not configured
                return Ux.future(this.arrow(supplier, this.atom, this.dao));
            } else {
                // Switcher configured and here call switcher
                return this.switcher.atom(data, this.atom)
                    .compose(switched -> Ux.future(this.arrow(supplier, switched, this.dao)));
            }
        } else {
            return this.switchAsync(supplier);
        }
    }

    public Future<Arrow> switchAsync(final Supplier<Arrow> supplier) {
        return Ux.future(this.arrow(supplier, this.atom, this.dao));
    }


    private Arrow arrow(final Supplier<Arrow> supplier, final DataAtom atom, final HDao dao) {
        final Arrow arrow = supplier.get();
        /*
         * mount 方法
         * 1）直接在 AoDao 中绑定 DataAtom 的数据
         * 2）这次绑定主要目的是保证在使用过程中，让 AoDao 和 DataAtom 再次连接
         */
        dao.mount(atom);
        /*
         * 合约处理
         */
        Ut.contract(arrow, DataAtom.class, atom);
        Ut.contract(arrow, HDao.class, dao);
        LOG.Uca.info(this.getClass(), "Arrow selected: {0} for {1}",
            arrow.getClass().getName(), atom.identifier());
        return arrow;
    }
}
