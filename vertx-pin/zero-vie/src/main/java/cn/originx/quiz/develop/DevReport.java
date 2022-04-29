package cn.originx.quiz.develop;

import cn.originx.refine.Ox;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.em.atom.ModelType;
import io.vertx.up.experiment.mixture.*;
import io.vertx.up.experiment.mu.KReference;
import io.vertx.up.experiment.mu.KTag;
import io.vertx.up.experiment.rule.RuleUnique;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

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
        builder.append("「Atom」--------------------------------------------").append(Strings.NEW_LINE);
        builder.append("identifier: ").append(atom.identifier()).append(Strings.NEW_LINE);
        builder.append("sigma: ").append(atom.sigma()).append(Strings.NEW_LINE);
        builder.append("language: ").append(atom.language()).append(Strings.NEW_LINE);
        final HTAtom htAtom = atom.shape();
        builder.append("Complex? ").append(htAtom.isComplex()).append(Strings.NEW_LINE);

        // Atom -> Model
        if (Objects.nonNull(model)) {
            builder.append("「Model」-------------------------------------------").append(Strings.NEW_LINE);
            builder.append("namespace: ").append(model.namespace()).append(Strings.NEW_LINE);
            builder.append("identifier: ").append(model.identifier()).append(" --> Matched: ")
                .append(atom.identifier().equals(model.identifier())).append(Strings.NEW_LINE);
            // Atom -> HAttribute / HReference / HMarker
            final ModelType type = model.type();
            builder.append("type: ").append(type).append(Strings.NEW_LINE);
        }

        // Atom Unique Rule
        builder.append("「Unique Rule」---------------------------------------").append(Strings.NEW_LINE);
        builder.append("Unique Rule: ");
        final RuleUnique rule = atom.ruleAtom();
        if (Objects.nonNull(rule)) {
            builder.append(rule).append(Strings.NEW_LINE);
        }
        builder.append("Channel Rule: ").append(Strings.NEW_LINE);
        final RuleUnique ruleChannel = atom.rule();
        if (Objects.nonNull(ruleChannel)) {
            builder.append(ruleChannel).append(Strings.NEW_LINE);
        }
        builder.append("Smart Looking For: ").append(atom.ruleSmart()).append(Strings.NEW_LINE);

        builder.append("「Attribute」---------------------------------------").append(Strings.NEW_LINE);
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
        Ox.Log.infoAtom(DevReport.class, "\n" + builder);
    }

    private static String outAttribute(final HAttribute attribute, final KReference reference) {
        final StringBuilder builder = new StringBuilder();
        final HTField attr = attribute.field();

        final KTag tag = attribute.tag();

        final HRule refRule = attribute.refRule();
        // Basic Line
        builder.append(":::" + Strings.LEFT_BRACKET).append(attr.alias())
            .append(",").append(attr.name()).append(Strings.RIGHT_BRACKET).append(Strings.COMMA);
        builder.append(" type=").append(attr.type()).append(Strings.COMMA);
        builder.append(" format=").append(attribute.format()).append(Strings.NEW_LINE);

        // Tag
        if (Objects.nonNull(tag)) {
            builder.append("\t").append(tag).append(Strings.NEW_LINE);
        }

        // Complex Line
        if (Objects.nonNull(refRule)) {
            builder.append("\t").append(refRule).append(Strings.NEW_LINE);
        }
        if (attr.isComplex()) {
            builder.append("\t").append("isComplex = ").append(attr.isComplex()).append(" children = ").append(Strings.NEW_LINE);
            final List<HTField> children = attr.children();
            children.forEach(field -> {
                builder.append("\t\t").append(Strings.LEFT_BRACKET).append(field.name())
                    .append(",").append(field.alias()).append(Strings.RIGHT_BRACKET).append(Strings.COMMA);
                builder.append(" type=").append(attr.type()).append(Strings.NEW_LINE);
            });
        }

        // Reference
        if (Objects.nonNull(reference)) {
            builder.append("\t").append(reference).append(Strings.NEW_LINE);
        }
        builder.append(":::----------------" + Strings.NEW_LINE);
        return builder.toString();
    }
}
