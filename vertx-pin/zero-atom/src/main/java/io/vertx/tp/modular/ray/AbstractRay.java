package io.vertx.tp.modular.ray;

import io.vertx.tp.atom.modeling.data.DataAtom;
import io.vertx.tp.atom.modeling.element.DataTpl;
import io.vertx.tp.error._501AnonymousAtomException;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public abstract class AbstractRay<T> implements AoRay<T> {
    protected transient DataTpl tpl;
    /*
     * DataQuote map
     */
    protected transient ConcurrentMap<String, RaySource> references =
            new ConcurrentHashMap<>();

    @Override
    public AoRay<T> on(final DataTpl tpl) {
        this.tpl = tpl;
        /*
         * DataAtom map for reference
         */
        final DataAtom atom = tpl.atom();
        if (Objects.isNull(atom)) {
            throw new _501AnonymousAtomException(tpl.getClass());
        }

        /*
         * Tpl 转换
         */
        tpl.matrixReference().forEach((source, dataQuote) -> {
            /*
             * Ray 模式，创建每个 source 的映射表
             */
            final DataAtom atomRef = atom.get(source);
            if (Objects.nonNull(atomRef)) {
                final RaySource raySource = RaySource.create(dataQuote, atomRef);
                this.references.put(source, raySource);
            }
        });
        return this;
    }

    @Override
    public T attach(final T input) {
        /*
         * Tpl 中的 DataQuote 检查
         */
        if (this.references.isEmpty()) {
            return input;
        } else {
            return this.doAttach(input);
        }
    }

    public abstract T doAttach(T input);
}
