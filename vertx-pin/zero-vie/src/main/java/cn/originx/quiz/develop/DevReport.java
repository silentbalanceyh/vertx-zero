package cn.originx.quiz.develop;

import io.aeon.experiment.mu.KReference;
import io.aeon.experiment.rule.RuleUnique;
import io.horizon.eon.VString;
import io.horizon.eon.em.modeler.ModelType;
import io.horizon.specification.modeler.HAtom;
import io.horizon.specification.modeler.HAttribute;
import io.horizon.specification.modeler.HModel;
import io.horizon.specification.modeler.HReference;
import io.modello.atom.normalize.KMarkAttribute;
import io.modello.atom.normalize.RRule;
import io.modello.specification.meta.HMetaAtom;
import io.modello.specification.meta.HMetaField;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import static cn.originx.refine.Ox.LOG;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class DevReport {
    // Report for Uniform Data Structure `HAtom` Here
    static void outAtom(final HAtom atom) {
        Objects.requireNonNull(atom);
        // Report for atom here
        final StringBuilder builder = new StringBuilder();

        final HModel model = atom.model();
        // Atom Basic Information
        builder.append("「Atom」--------------------------------------------").append(VString.NEW_LINE);
        builder.append("identifier: ").append(atom.identifier()).append(VString.NEW_LINE);
        builder.append("sigma: ").append(atom.sigma()).append(VString.NEW_LINE);
        builder.append("language: ").append(atom.language()).append(VString.NEW_LINE);
        final HMetaAtom metaAtom = atom.shape();
        builder.append("Complex? ").append(metaAtom.isComplex()).append(VString.NEW_LINE);

        // Atom -> Model
        if (Objects.nonNull(model)) {
            builder.append("「Model」-------------------------------------------").append(VString.NEW_LINE);
            builder.append("namespace: ").append(model.namespace()).append(VString.NEW_LINE);
            builder.append("identifier: ").append(model.identifier()).append(" --> Matched: ")
                .append(atom.identifier().equals(model.identifier())).append(VString.NEW_LINE);
            // Atom -> HAttribute / HReference / HMarker
            final ModelType type = model.type();
            builder.append("type: ").append(type).append(VString.NEW_LINE);
        }

        // Atom Unique Rule
        builder.append("「Unique Rule」---------------------------------------").append(VString.NEW_LINE);
        builder.append("Unique Rule: ");
        final RuleUnique rule = atom.ruleAtom();
        if (Objects.nonNull(rule)) {
            builder.append(rule).append(VString.NEW_LINE);
        }
        builder.append("Channel Rule: ").append(VString.NEW_LINE);
        final RuleUnique ruleChannel = atom.rule();
        if (Objects.nonNull(ruleChannel)) {
            builder.append(ruleChannel).append(VString.NEW_LINE);
        }
        builder.append("Smart Looking For: ").append(atom.ruleSmart()).append(VString.NEW_LINE);

        builder.append("「Attribute」---------------------------------------").append(VString.NEW_LINE);
        // Attribute Information
        final Set<String> attributes = atom.attribute();
        final HReference reference = atom.reference();
        final Set<String> treeSet = new TreeSet<>(attributes);
        treeSet.stream().filter(name -> Objects.nonNull(atom.attribute(name))).forEach(name -> {
            // Each Data for Attribute
            final HAttribute attribute = atom.attribute(name);
            final KReference refData;
            if (Objects.isNull(reference)) {
                refData = null;
            } else {
                refData = reference.refData(name);
            }
            builder.append(outAttribute(attribute, refData));
        });
        builder.append("Attribute Size = ").append(treeSet.size()).append(VString.NEW_LINE);
        // #NEW_LOG
        LOG.Atom.info(DevReport.class, "\n" + builder);
    }

    private static String outAttribute(final HAttribute attribute, final KReference reference) {
        final StringBuilder builder = new StringBuilder();
        final HMetaField attr = attribute.field();

        final KMarkAttribute tag = attribute.marker();

        final RRule refRule = attribute.referenceRule();
        // Basic Line
        builder.append(":::" + VString.LEFT_BRACKET).append(attr.alias())
            .append(",").append(attr.name()).append(VString.RIGHT_BRACKET).append(VString.COMMA);
        builder.append(" type=").append(attr.type()).append(VString.COMMA);
        builder.append(" format=").append(attribute.format()).append(VString.NEW_LINE);

        // Tag
        if (Objects.nonNull(tag)) {
            builder.append("\t").append(tag).append(VString.NEW_LINE);
        }

        // Complex Line
        if (Objects.nonNull(refRule)) {
            builder.append("\t").append(refRule).append(VString.NEW_LINE);
        }
        if (attr.isComplex()) {
            builder.append("\t").append("isComplex = ").append(attr.isComplex()).append(" children = ").append(VString.NEW_LINE);
            final List<HMetaField> children = attr.children();
            children.forEach(field -> {
                builder.append("\t\t").append(VString.LEFT_BRACKET).append(field.name())
                    .append(",").append(field.alias()).append(VString.RIGHT_BRACKET).append(VString.COMMA);
                builder.append(" type=").append(attr.type()).append(VString.NEW_LINE);
            });
        }

        // Reference
        if (Objects.nonNull(reference)) {
            builder.append("\t").append(reference).append(VString.NEW_LINE);
        }
        builder.append(":::----------------" + VString.NEW_LINE);
        return builder.toString();
    }
}
