package io.vertx.mod.ambient.refine;

import cn.vertxup.ambient.domain.tables.pojos.XActivity;
import cn.vertxup.ambient.domain.tables.pojos.XActivityChange;
import io.horizon.eon.em.typed.ChangeFlag;
import io.horizon.uca.compare.Vs;
import io.modello.atom.normalize.KMarkAtom;
import io.modello.eon.em.EmAttribute;
import io.modello.specification.atom.HAtom;
import io.modello.specification.atom.HAttribute;
import io.modello.specification.meta.HMetaField;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.ambient.cv.em.ActivityStatus;
import io.vertx.mod.ke.refine.Ke;
import io.vertx.up.util.Ut;

import java.util.*;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class AtDiffer {

    static List<XActivityChange> diff(final List<XActivityChange> changes, final XActivity activity) {
        Objects.requireNonNull(activity);
        if (!changes.isEmpty()) {
            changes.forEach(change -> {
                change.setActivityId(activity.getKey());
                change.setStatus(ActivityStatus.CONFIRMED.name());
                Ke.umCreated(change, activity);
            });
        }
        return changes;
    }

    /*
     * 标准化比对逻辑
     */
    static List<XActivityChange> diff(final JsonObject recordO, final JsonObject recordN,
                                      final HAtom atom) {
        final ChangeFlag flag = Ut.aiFlag(recordN, recordO);
        final List<XActivityChange> changes = new ArrayList<>();
        if (ChangeFlag.NONE != flag) {
            final KMarkAtom marker = atom.marker();
            final Set<String> fieldTrack = marker.enabled(EmAttribute.Marker.track);
            fieldTrack.stream().filter(field -> {
                // Must contain value for checking.
                final Object valueN = recordN.getValue(field);
                final Object valueO = recordO.getValue(field);
                return (Objects.nonNull(valueN) || Objects.nonNull(valueO));
            }).forEach(field -> {
                final HAttribute attribute = atom.attribute(field);
                final XActivityChange change = createChange(attribute);

                final Object valueN = recordN.getValue(field);
                final Object valueO = recordO.getValue(field);

                if (ChangeFlag.ADD == flag) {
                    // ADD Operation
                    change.setType(flag.name());
                    change.setValueNew(Objects.isNull(valueN) ? null : valueN.toString());
                    changes.add(change);
                } else if (ChangeFlag.DELETE == flag) {
                    // Delete Operation
                    change.setType(flag.name());
                    change.setValueOld(Objects.isNull(valueO) ? null : valueO.toString());
                    changes.add(change);
                } else if (ChangeFlag.UPDATE == flag) {
                    if (Objects.isNull(valueO)) {
                        // ADD Attribute
                        change.setType(ChangeFlag.ADD.name());
                        change.setValueNew(valueN.toString());
                        changes.add(change);
                    } else if (Objects.isNull(valueN)) {
                        // Delete Attribute
                        change.setType(ChangeFlag.DELETE.name());
                        change.setValueOld(valueO.toString());
                        changes.add(change);
                    } else {
                        // Update Attribute
                        final Vs vs = atom.vs();
                        if (vs.isChange(valueO, valueN, field)) {
                            change.setType(ChangeFlag.UPDATE.name());
                            change.setValueNew(valueN.toString());
                            change.setValueOld(valueO.toString());
                            changes.add(change);
                        }
                    }
                }
            });
        }
        return changes;
    }

    private static XActivityChange createChange(final HAttribute attribute) {
        final XActivityChange change = new XActivityChange();
        change.setKey(UUID.randomUUID().toString());
        final HMetaField field = attribute.field();
        Objects.requireNonNull(field);
        change.setFieldName(field.name());
        change.setFieldAlias(field.alias());
        change.setFieldType(field.type().getName());
        return change;
    }
}
