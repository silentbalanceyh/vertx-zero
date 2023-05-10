package cn.originx.scaffold.plugin;

import cn.originx.cv.OxCv;
import io.horizon.eon.em.typed.ChangeFlag;
import io.horizon.uca.log.Annal;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.builtin.DataAtom;
import io.vertx.tp.error._501FabricIssueException;
import io.vertx.up.commune.exchange.BMapping;
import io.vertx.up.commune.exchange.DFabric;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.util.Objects;

@SuppressWarnings("unchecked")
public abstract class AbstractPlugin<T> {
    protected transient DataAtom atom;
    protected transient DFabric fabric;

    public T bind(final DataAtom atom) {
        this.atom = atom;
        return (T) this;
    }

    public T bind(final DFabric fabric) {
        this.fabric = fabric;
        return (T) this;
    }

    protected Annal logger() {
        return Annal.get(this.getClass());
    }

    protected ChangeFlag operation(final JsonObject options) {
        return Ut.toEnum(() -> options.getString(OxCv.CONFIGURATION_OPERATION), ChangeFlag.class, ChangeFlag.NONE);
    }

    protected DFabric fabric(final JsonObject options) {
        Fn.out(Objects.isNull(this.fabric), _501FabricIssueException.class, this.getClass());
        final Object mapping = options.getValue(KName.MAPPING);
        final BMapping item = this.mapping(mapping);
        /* 双条件检查，为 NULL 和 Empty 都没有任何 Mapping 配置*/
        if (Objects.isNull(item) || item.isEmpty()) {
            this.logger().info("[ Combiner ] No mapping! {0}", options.encode());
            return this.fabric;
        } else {
            this.logger().info("[ Combiner ] Mapping found! {0}", item.toString());
            return this.fabric.copy(item);
        }
    }

    private BMapping mapping(final Object value) {
        if (value instanceof String) {
            final JsonObject config = Ut.ioJObject(value.toString());
            return this.mapping(config);
        } else if (value instanceof JsonObject) {
            return new BMapping((JsonObject) value);
        } else {
            return null;
        }
    }
}
