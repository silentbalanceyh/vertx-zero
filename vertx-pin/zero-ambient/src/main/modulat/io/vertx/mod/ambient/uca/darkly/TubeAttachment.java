package io.vertx.mod.ambient.uca.darkly;

import cn.vertxup.ambient.domain.tables.pojos.XActivityRule;
import io.horizon.eon.VString;
import io.horizon.eon.em.typed.ChangeFlag;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.ambient.cv.em.TubeType;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static io.vertx.mod.ambient.refine.At.LOG;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class TubeAttachment extends AbstractTube {
    @Override
    public Future<JsonObject> traceAsync(final JsonObject data, final XActivityRule rule) {
        final JsonObject dataN = Ut.aiDataN(data);
        final JsonObject dataO = Ut.aiDataO(data);
        final JsonArray fileN = Ut.valueJArray(dataN, rule.getRuleField());
        final JsonArray fileO = Ut.valueJArray(dataO, rule.getRuleField());
        /*
         * 附件对比只对比两个字段
         * 1. fileKey
         * 2. fileUrl
         */
        final Set<String> fieldSet = new HashSet<>() {
            {
                this.add(KName.FILE_KEY);
                this.add(KName.Attachment.FILE_URL);
            }
        };
        final boolean isDiff = Ut.isDiff(fileN, fileO, fieldSet);
        LOG.Tabb.info(this.getClass(), "附件检查：{0}", isDiff);
        if (isDiff) {
            /*
             * data 中特殊构造 __message
             */
            final JsonObject ruleConfig = Ut.toJObject(rule.getRuleTpl());
            final ConcurrentMap<ChangeFlag, JsonArray> diffMap = this.diff(fileN, fileO, fieldSet);
            final JsonObject dataIn = data.copy();
            dataIn.put(KName.__.MESSAGE, this.message(diffMap, ruleConfig));

            final Tube tube = Tube.instance(TubeType.EXPRESSION);
            return tube.traceAsync(dataIn, rule);
        } else {
            return Ux.future(data);
        }
    }

    private String message(final ConcurrentMap<ChangeFlag, JsonArray> diffMap,
                           final JsonObject ruleConfig) {
        final JsonArray added = diffMap.get(ChangeFlag.ADD);
        final StringBuilder message = new StringBuilder();
        if (Ut.isNotNil(added)) {
            final String prefix = Ut.valueString(ruleConfig, ChangeFlag.ADD.name());
            this.messageFormat(message, added, prefix);
        }
        message.append(VString.COMMA);
        final JsonArray deleted = diffMap.get(ChangeFlag.DELETE);
        if (Ut.isNotNil(deleted)) {
            final String prefix = Ut.valueString(ruleConfig, ChangeFlag.DELETE.name());
            this.messageFormat(message, deleted, prefix);
        }
        return message.toString();
    }

    private void messageFormat(final StringBuilder message,
                               final JsonArray queue, final String prefix) {
        if (Ut.isNotNil(queue)) {
            final Set<String> files = new HashSet<>();
            Ut.itJArray(queue).forEach(json -> {
                final String filename = Ut.valueString(json, KName.NAME);
                if (Ut.isNotNil(filename)) {
                    files.add(filename);
                }
            });
            if (!files.isEmpty()) {
                message.append(prefix).append(VString.COLON).append(Ut.fromJoin(files, VString.COMMA));
            }
        }
    }

    private ConcurrentMap<ChangeFlag, JsonArray> diff(final JsonArray fileN,
                                                      final JsonArray fileO,
                                                      final Set<String> fields) {
        final JsonArray added = new JsonArray();
        final JsonArray deleted = new JsonArray();
        Ut.itJArray(fileN).forEach(jsonN -> {
            final JsonObject subsetJ = Ut.elementSubset(jsonN, fields);
            final JsonObject ifOld = Ut.elementFind(fileO, subsetJ);
            if (Ut.isNil(ifOld)) {
                // Old removed from New
                added.add(jsonN);
            }
        });
        Ut.itJArray(fileO).forEach(jsonO -> {
            final JsonObject subsetJ = Ut.elementSubset(jsonO, fields);
            final JsonObject ifNew = Ut.elementFind(fileN, subsetJ);
            if (Ut.isNil(ifNew)) {
                // New removed from Old
                deleted.add(jsonO);
            }
        });
        return new ConcurrentHashMap<>() {
            {
                this.put(ChangeFlag.ADD, added);
                this.put(ChangeFlag.DELETE, deleted);
            }
        };
    }
}
