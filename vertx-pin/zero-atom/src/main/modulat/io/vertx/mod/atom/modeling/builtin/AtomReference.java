package io.vertx.mod.atom.modeling.builtin;

import cn.vertxup.atom.domain.tables.pojos.MAttribute;
import io.aeon.experiment.shape.HAtomReference;
import io.macrocosm.specification.app.HApp;
import io.macrocosm.specification.program.HArk;
import io.modello.atom.normalize.RReference;
import io.modello.eon.em.EmAttribute;
import io.modello.specification.action.HDao;
import io.modello.specification.atom.HAtom;
import io.modello.specification.atom.HAttribute;
import io.vertx.mod.atom.modeling.Model;
import io.vertx.mod.atom.refine.Ao;
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
     * @param modelRef {@link io.vertx.mod.atom.modeling.Model} Input `M_MODEL` definition.
     * @param ark      {@link HArk} The application name.
     */
    public AtomReference(final Model modelRef, final HArk ark) {
        super(ark);
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
            // .filter(attr -> Type.REFERENCE.name().equals(attr.getType()))
            // Processing workflow on result.
            .forEach(attribute -> {
                /*
                 *  Hash Map `references` calculation
                 *      - source = RQuote
                 *          - condition1 = RDao
                 *          - condition2 = RDao
                 *  Based on DataAtom reference to create
                 */
                final EmAttribute.Type type = Ut.toEnum(attribute::getType, EmAttribute.Type.class, EmAttribute.Type.INTERNAL);

                if (EmAttribute.Type.REFERENCE == type) {
                    /*
                     * Reference Initializing
                     */
                    final HAttribute aoAttr = modelRef.attribute(attribute.getName());
                    final RReference reference = new RReference();
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
        final HApp app = this.ark.app();
        return Ao.toAtom(app.name(), identifier);
    }
}
