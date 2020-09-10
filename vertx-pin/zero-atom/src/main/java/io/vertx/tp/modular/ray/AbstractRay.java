package io.vertx.tp.modular.ray;

import io.vertx.tp.atom.modeling.data.DataAtom;
import io.vertx.tp.atom.modeling.element.DataReference;
import io.vertx.tp.atom.modeling.element.DataTpl;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public abstract class AbstractRay<T> implements AoRay<T> {
    protected transient DataTpl tpl;
    /*
     * DataReference map
     */
    protected transient ConcurrentMap<String, DataReference> references =
            new ConcurrentHashMap<>();
    protected transient ConcurrentMap<String, DataAtom> atoms =
            new ConcurrentHashMap<>();

    @Override
    public AoRay<T> on(final DataTpl tpl) {
        this.tpl = tpl;
        this.references.putAll(tpl.matrixReference());
        /*
         * DataAtom map for reference
         */
        final DataAtom atom = tpl.atom();
        if (Objects.nonNull(atom)) {
            this.references.keySet().forEach(source -> {
                final DataAtom atomRef = atom.get(source);
                if (Objects.nonNull(atomRef)) {
                    this.atoms.put(source, atomRef);
                }
            });
        }
        return this;
    }

    @Override
    public T attach(final T input) {
        /*
         * Tpl 中的 DataReference 检查
         */
        if (this.references.isEmpty()) {
            return input;
        } else {
            return this.doAttach(input);
        }
    }

    public abstract T doAttach(T input);
}
