package io.vertx.tp.atom.modeling.builtin;

import cn.vertxup.atom.domain.tables.pojos.MAttribute;
import io.aeon.experiment.mu.KReference;
import io.aeon.experiment.shape.HAtomReference;
import io.horizon.atom.app.KApp;
import io.horizon.eon.em.modeler.AttributeType;
import io.horizon.specification.modeler.HAtom;
import io.horizon.specification.modeler.HAttribute;
import io.horizon.specification.modeler.HDao;
import io.vertx.tp.atom.modeling.Model;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Set;

/**
 * ## Reference Calculation
 *
 * {@link HAtomReference}
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class AtomReference extends HAtomReference {
    /**
     * 「Fluent」Build reference metadata information based on `Model`.
     *
     * @param modelRef {@link io.vertx.tp.atom.modeling.Model} Input `M_MODEL` definition.
     * @param app      {@link KApp} The application name.
     */
    public AtomReference(final Model modelRef, final KApp app) {
        super(app);
        /* type = REFERENCE */
        final Set<MAttribute> attributes = modelRef.dbAttributes();
        attributes.stream()
            // condition1, Not Null
            .filter(Objects::nonNull)
            // condition2, source is not Null
            .filter(attr -> Objects.nonNull(attr.getSource()))
            /*
             * condition3, remove self reference to avoid memory out
             * This condition is critical because of Memory Out Issue of deadLock in reference
             * Current model identifier must not be `source` because it will trigger
             * Self deal lock here. To avoid this kind of situation, filtered this item.
             */
            .filter(attr -> !modelRef.identifier().equals(attr.getSource()))
            // condition4, type = REFERENCE
            // .filter(attr -> AttributeType.REFERENCE.name().equals(attr.getType()))
            // Processing workflow on result.
            .forEach(attribute -> {
                /*
                 *  Hash Map `references` calculation
                 *      - source = RQuote
                 *          - condition1 = RDao
                 *          - condition2 = RDao
                 *  Based on DataAtom reference to create
                 */
                final AttributeType type = Ut.toEnum(attribute::getType, AttributeType.class, AttributeType.INTERNAL);

                if (AttributeType.REFERENCE == type) {
                    /*
                     * Reference Initializing
                     */
                    final HAttribute aoAttr = modelRef.attribute(attribute.getName());
                    final KReference reference = new KReference();
                    reference.name(attribute.getName());
                    reference
                        .source(attribute.getSource())
                        .sourceField(attribute.getSourceField())
                        .sourceReference(attribute.getSourceReference());
                    /*
                     * Call Parent Method
                     */
                    this.initializeReference(reference, aoAttr);
                }
            });
    }

    @Override
    protected HDao toDao(final HAtom atom) {
        return Ao.toDao(atom);
    }

    @Override
    protected HAtom toAtom(final String identifier) {
        return Ao.toAtom(this.app.name(), identifier);
    }
}
