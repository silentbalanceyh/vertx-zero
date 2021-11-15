package cn.zeroup.macrocosm.api;

import cn.zeroup.macrocosm.cv.HighWay;
import cn.zeroup.macrocosm.cv.em.TodoStatus;
import cn.zeroup.macrocosm.service.FlowStub;
import cn.zeroup.macrocosm.service.TaskStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.config.XHeader;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.unity.Ux;

import javax.inject.Inject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@Queue
public class QueueActor {
    @Inject
    private transient FlowStub flowStub;
    @Inject
    private transient TaskStub taskStub;

    @Address(HighWay.Queue.TASK_QUEUE)
    public Future<JsonObject> fetchMyCreated(final JsonObject qr, final Boolean request,
                                             final User user) {
        final JsonObject condition = new JsonObject();
        final String userId = Ux.keyUser(user);
        if (request) {
            /*
             * Request Queue
             * 1.status = DRAFT / PENDING
             * 2.createdBy = userId
             */
            condition.put(KName.OWNER, userId);
            condition.put(KName.STATUS + ",i", new JsonArray()
                .add(TodoStatus.DRAFT.name())
                .add(TodoStatus.PENDING.name())
            );
            condition.put(Strings.EMPTY, Boolean.TRUE);
        } else {
            /*
             * Approval Queue
             * 1.status = ACCEPTED / PENDING
             * 2.owner <> userId
             */
            condition.put(KName.OWNER + ",<>", userId);
            condition.put(KName.STATUS + ",i", new JsonArray()
                .add(TodoStatus.PENDING.name())
                .add(TodoStatus.ACCEPTED.name())
            );
            condition.put(Strings.EMPTY, Boolean.TRUE);
        }
        final JsonObject qrCombine = Ux.whereQrA(qr, "$IN$", condition);
        Wf.Log.initQueue(this.getClass(), "Queue condition: {0}", qrCombine.encode());
        return this.taskStub.fetchQueue(qrCombine);
    }

    @Address(HighWay.Flow.BY_CODE)
    public Future<JsonObject> fetchFlow(final String code, final XHeader header) {
        final String sigma = header.getSigma();
        return this.flowStub.fetchFlow(code, sigma);
    }

    /*
     * Response:
     * {
     *      "form": {
     *
     *      },
     *      "workflow": {
     *
     *      }
     * }
     */
    @Address(HighWay.Queue.TASK_FORM)
    public Future<JsonObject> fetchForm(final JsonObject data,
                                        final Boolean isPre, final XHeader header) {
        final String definitionId = data.getString(KName.Flow.DEFINITION_ID);
        if (isPre) {
            // Start Point
            return this.flowStub.fetchFirst(definitionId, header.getSigma());
        } else {
            return Ux.future();
        }
    }
}
