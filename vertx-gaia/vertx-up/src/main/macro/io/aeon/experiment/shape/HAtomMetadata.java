package io.aeon.experiment.shape;

import io.modello.specification.atom.HAttribute;
import io.modello.specification.atom.HModel;
import io.modello.specification.meta.HMetaAtom;
import io.modello.specification.meta.HMetaField;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class HAtomMetadata {

    private transient final HModel modelRef;
    private transient final String identifier;
    private transient final HMetaAtom metaAtom = HMetaAtom.of();


    public HAtomMetadata(final HModel modelRef) {
        // Model reference
        this.modelRef = modelRef;
        // Extract identifier from modelRef
        this.identifier = modelRef.identifier();
        // Calculation
        modelRef.attribute().forEach(name -> {
            // Name Here
            final HAttribute attr = modelRef.attribute(name);
            /*
             * Why ?
             */
            Objects.requireNonNull(attr);
            final HMetaField field = attr.field();
/*            if (Objects.isNull(attr)) {
                if (Objects.isNull(this.htFieldFn)) {
                    field = null;
                } else {
                    field = this.htFieldFn.apply(name);
                }
            } else {
                field = attr.field();
            }*/
            if (Objects.nonNull(field)) {
                this.metaAtom.add(field);
            }
        });
    }

    // ==================== Model Information ==================
    @SuppressWarnings("unchecked")
    public <T extends HModel> T model() {
        return (T) this.modelRef;
    }

    public String identifier() {
        return this.identifier;
    }

    // ==================== Attribute Information ==================
    public HAttribute attribute(final String name) {
        return this.modelRef.attribute(name);
    }

    public Set<String> attribute() {
        return this.modelRef.attribute();
    }

    // ==================== HtAtom Information ==================
    // name = alias
    public ConcurrentMap<String, String> alias() {
        return this.metaAtom.alias();
    }

    public HMetaAtom shape() {
        return this.metaAtom;
    }

    // ==================== HtField Information ==================
    public ConcurrentMap<String, HMetaField> types() {
        final ConcurrentMap<String, HMetaField> typeMap = new ConcurrentHashMap<>();
        this.modelRef.attribute().forEach((name) -> {
            final HAttribute attribute = this.modelRef.attribute(name);
            typeMap.put(name, attribute.field());
        });
        return typeMap;
    }

    public HMetaField type(final String field) {
        final HAttribute attribute = this.modelRef.attribute(field);
        if (Objects.isNull(attribute)) {
            return null;
        }
        return attribute.field();
    }
}
