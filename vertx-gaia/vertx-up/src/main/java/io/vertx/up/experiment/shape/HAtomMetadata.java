package io.vertx.up.experiment.shape;

import io.vertx.up.experiment.mixture.HAttribute;
import io.vertx.up.experiment.mixture.HModel;
import io.vertx.up.experiment.mixture.HTAtom;
import io.vertx.up.experiment.mixture.HTField;

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
    private transient final HTAtom htAtom = HTAtom.create();


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
            final HTField field = attr.field();
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
                this.htAtom.add(field);
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
        return this.htAtom.alias();
    }

    public HTAtom shape() {
        return this.htAtom;
    }

    // ==================== HtField Information ==================
    public ConcurrentMap<String, HTField> types() {
        final ConcurrentMap<String, HTField> typeMap = new ConcurrentHashMap<>();
        this.modelRef.attribute().forEach((name) -> {
            final HAttribute attribute = this.modelRef.attribute(name);
            typeMap.put(name, attribute.field());
        });
        return typeMap;
    }

    public HTField type(final String field) {
        final HAttribute attribute = this.modelRef.attribute(field);
        if (Objects.isNull(attribute)) {
            return null;
        }
        return attribute.field();
    }
}
