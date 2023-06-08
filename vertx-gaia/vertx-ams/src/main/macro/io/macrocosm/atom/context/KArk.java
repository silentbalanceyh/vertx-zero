package io.macrocosm.atom.context;

import io.macrocosm.specification.app.HApp;
import io.macrocosm.specification.program.HArk;
import io.macrocosm.specification.secure.HoI;
import io.modello.atom.app.KApp;
import io.modello.atom.app.KDS;
import io.modello.atom.app.KDatabase;
import io.modello.atom.app.KTenement;

import java.util.Objects;

/**
 * 「默认配置容器」
 *
 * @author lang : 2023-06-06
 */
public class KArk implements HArk {
    private KDS<KDatabase> ds;
    private HoI owner;
    private HApp app;

    private KArk(final String name) {
        this.app = new KApp(name);
        this.owner = new KTenement();
        this.ds = new KDS<>();
    }

    public static HArk of(final String name) {
        return new KArk(name);
    }

    public static HArk of() {
        return new KArk(null);
    }

    @Override
    public HApp app() {
        return this.app;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends KDatabase> KDS<T> database() {
        return (KDS<T>) this.ds;
    }

    @Override
    public HoI owner() {
        return this.owner;
    }

    @Override
    public HArk apply(final HArk target) {
        if (Objects.nonNull(target) && target instanceof KArk) {
            final KArk targetRef = (KArk) target;
            this.app = this.app.apply(targetRef.app);
            this.ds = this.ds.apply(targetRef.ds);
            this.owner = this.owner.apply(targetRef.owner);
        }
        return this;
    }
}
