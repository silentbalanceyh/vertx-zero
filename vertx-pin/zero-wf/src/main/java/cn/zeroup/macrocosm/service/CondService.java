package cn.zeroup.macrocosm.service;

import cn.zeroup.macrocosm.cv.em.TodoStatus;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.up.atom.query.engine.Qr;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.unity.Ux;

import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class CondService implements CondStub {

    private static final Set<String> SKIP_QUEUE_SET = new HashSet<>() {
        {
            this.add(KName.Flow.Auditor.OWNER);
            this.add(KName.Flow.Auditor.SUPERVISOR);
            this.add(KName.Flow.Auditor.OPEN_BY);
            this.add(KName.Flow.Auditor.TO_USER);
            this.add(KName.Flow.Auditor.TO_ROLE);
            this.add(KName.Flow.Auditor.TO_DEPT);
            this.add(KName.Flow.Auditor.TO_TEAM);
            this.add(KName.Flow.Auditor.TO_GROUP);
        }
    };

    private static final Set<String> SKIP_HISTORY_SET = new HashSet<>() {
        {
            this.add(KName.Flow.Auditor.OWNER);
            this.add(KName.Flow.Auditor.SUPERVISOR);
            this.add(KName.Flow.Auditor.OPEN_BY);
            this.add(KName.Flow.Auditor.CANCEL_BY);
            this.add(KName.Flow.Auditor.CLOSE_BY);
        }
    };

    @Override
    public Future<JsonObject> qrQueue(final JsonObject qr, final String user) {
        if (this.skipDefault(qr, SKIP_QUEUE_SET)) {
            Wf.Log.initQueue(this.getClass(), "Qr Skip: {0}", qr.encode());
            // Status Must be in following
            // -- PENDING
            // -- DRAFT
            // -- ACCEPTED
            final JsonObject qrStatus = Ux.whereAnd();
            qrStatus.put(KName.STATUS + ",i", new JsonArray()
                .add(TodoStatus.PENDING.name())
                .add(TodoStatus.ACCEPTED.name())
                .add(TodoStatus.DRAFT.name())
            );
            final JsonObject qrCombine = Ux.whereQrA(qr, "$Q$", qrStatus);
            Wf.Log.initQueue(this.getClass(), "Qr Queue ( Skip ): {0}", qrCombine.encode());
            return Ux.future(qrCombine);
        } else {
            final JsonObject qrQueue = Ux.whereOr();
            // Open By Me
            final JsonObject qrCreate = Ux.whereAnd(KName.Flow.Auditor.OPEN_BY, user)
                .put(KName.STATUS + ",i", new JsonArray()
                    .add(TodoStatus.PENDING.name())
                    .add(TodoStatus.ACCEPTED.name())
                    .add(TodoStatus.DRAFT.name())
                );
            qrQueue.put("$QrC$", qrCreate);
            // Approved By Me
            final JsonObject qrApprove = Ux.whereAnd(KName.Flow.Auditor.TO_USER, user)
                .put(KName.STATUS + ",i", new JsonArray()
                    .add(TodoStatus.PENDING.name())
                );
            qrQueue.put("$QrA", qrApprove);

            // Divide to AND / OR
            final JsonObject qrCombine = Ux.whereQrA(qr, "$Q$", qrQueue);
            Wf.Log.initQueue(this.getClass(), "Qr Queue: {0}", qrCombine.encode());
            return Ux.future(qrCombine);
        }
    }

    @Override
    public Future<JsonObject> qrHistory(final JsonObject qr, final String user) {
        if (this.skipDefault(qr, SKIP_HISTORY_SET)) {
            Wf.Log.initQueue(this.getClass(), "Qr Skip: {0}", qr.encode());
            // Status Must be in following
            // -- PENDING
            // -- DRAFT
            // -- ACCEPTED
            return Ux.future(qr);
        } else {
            // Open By Me, Owner Is Me
            final JsonObject qrHistory =
                Ux.whereOr(KName.Flow.Auditor.OPEN_BY, user);
            qrHistory.put(KName.OWNER, user);

            // Divide to AND / OR
            final JsonObject qrCombine = Ux.whereQrA(qr, "$Q$", qrHistory);
            Wf.Log.initQueue(this.getClass(), "Qr History: {0}", qrCombine.encode());
            return Ux.future(qrCombine);
        }
    }

    private boolean skipDefault(final JsonObject qr, final Set<String> skipSet) {
        final JsonObject criteria = qr.getJsonObject(Qr.KEY_CRITERIA, new JsonObject());
        final long counter = criteria.fieldNames().stream().map(field -> {
            if (field.contains(Strings.COMMA)) {
                return field.split(Strings.COMMA)[0];
            } else {
                return field;
            }
        }).filter(skipSet::contains).count();
        return 0 < counter;
    }
}
