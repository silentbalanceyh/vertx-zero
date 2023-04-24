package io.vertx.up.experiment.shape;

import io.horizon.specification.modeler.HAttribute;
import io.horizon.specification.modeler.HModel;
import io.horizon.specification.modeler.TypeAtom;
import io.horizon.specification.modeler.TypeField;

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
    private transient final TypeAtom metaAtom = TypeAtom.create();


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
            final TypeField field = attr.field();
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

    public TypeAtom shape() {
        return this.metaAtom;
    }

    // ==================== HtField Information ==================
    public ConcurrentMap<String, TypeField> types() {
        final ConcurrentMap<String, TypeField> typeMap = new ConcurrentHashMap<>();
        this.modelRef.attribute().forEach((name) -> {
            final HAttribute attribute = this.modelRef.attribute(name);
            typeMap.put(name, attribute.field());
        });
        return typeMap;
    }

    public TypeField type(final String field) {
        final HAttribute attribute = this.modelRef.attribute(field);
        if (Objects.isNull(attribute)) {
            return null;
        }
        return attribute.field();
    }
}
