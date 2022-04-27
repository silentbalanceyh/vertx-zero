package io.vertx.tp.atom.modeling.data;

import cn.vertxup.atom.domain.tables.pojos.MAttribute;
import io.vertx.tp.atom.modeling.Model;

import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 * 专用开关变量，用于处理所有 DataAtom 中的开关操作
 */
class AtomMarker {
    private transient final Model modelRef;

    AtomMarker(final Model modelRef) {
        this.modelRef = modelRef;
    }

    boolean trackable() {
        final Boolean result = this.modelRef.dbModel().getIsTrack();
        return Objects.isNull(result) ? Boolean.FALSE : Boolean.TRUE;
    }

    Set<String> track(final Boolean defaultValue) {
        return this.audit(MAttribute::getIsTrack, defaultValue);
    }

    Set<String> in(final Boolean defaultValue) {
        return this.audit(MAttribute::getIsSyncIn, defaultValue);
    }

    Set<String> out(final Boolean defaultValue) {
        return this.audit(MAttribute::getIsSyncOut, defaultValue);
    }

    Set<String> confirm(final Boolean defaultValue) {
        return this.audit(MAttribute::getIsConfirm, defaultValue);
    }

    private Set<String> audit(final Function<MAttribute, Boolean> function, final Boolean defaultValue) {
        if (defaultValue) {
            /*
             * field = true
             */
            return this.audit(attr -> {
                final Boolean result = function.apply(attr);
                return Objects.isNull(result) ? Boolean.TRUE : result;
            });
        } else {
            /*
             * field = false
             */
            return this.audit(attr -> {
                final Boolean result = function.apply(attr);
                return Objects.isNull(result) ? Boolean.FALSE : !result;
            });
        }
    }

    private Set<String> audit(final Predicate<MAttribute> predicate) {
        return this.modelRef.dbAttributes().stream()
            .filter(Objects::nonNull)
            .filter(predicate)
            .map(MAttribute::getName)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
    }
}
