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

    /*
     * The basic qr contains three view:
     * 1. The `owner` is userId
     * 2. The `supervisor` is userId
     * 3. The `openBy` is userId
     *
     * The input qr should be following:
     * {
     *     "criteria": {
     *
     *     }
     * }
     */
    @Address(HighWay.Queue.TASK_QUEUE)
    public Future<JsonObject> fetchQueue(final JsonObject qr, final User user) {
        System.out.println(qr.encodePrettily());
        final String userId = Ux.keyUser(user);
        // My Queue
        final JsonObject queueCreate = Ux.whereAnd();
        queueCreate.put(KName.CREATED_BY, userId);
        queueCreate.put(KName.STATUS + ",i",
            new JsonArray()
                .add(TodoStatus.PENDING.name())
                .add(TodoStatus.ACCEPTED.name())
                .add(TodoStatus.DRAFT.name())
        );
        // Other Queue
        final JsonObject queueApprove = Ux.whereAnd();
        queueApprove.put("toUser", userId);
        queueApprove.put(KName.STATUS + ",i",
            new JsonArray()
                .add(TodoStatus.PENDING.name())
        );
        final JsonObject combine = Ux.whereOr();
        combine.put("$MY$", queueCreate);
        combine.put("$AP$", queueApprove);
        final JsonObject qrCombine = Ux.whereQrA(qr, "$Q$", combine);
        Wf.Log.initQueue(this.getClass(), "Queue condition: {0}", qrCombine.encode());
        return this.taskStub.fetchQueue(qrCombine);
    }


    @Address(HighWay.Queue.TASK_HISTORY)
    public Future<JsonObject> fetchHistory(final JsonObject qr) {
        final JsonObject qrCombine = Ux.whereQrA(qr, KName.STATUS + ",i",
            new JsonArray()
                .add(TodoStatus.FINISHED.name())
                .add(TodoStatus.REJECTED.name())
                .add(TodoStatus.CANCELED.name())
        );
        final JsonObject qrFinal = Ux.whereQrA(qrCombine, "traceEnd", Boolean.TRUE);
        Wf.Log.initQueue(this.getClass(), "History condition: {0}", qrCombine.encode());
        return this.taskStub.fetchQueue(qrFinal);
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
        if (isPre) {
            // Start Form
            final String definitionId = data.getString(KName.Flow.DEFINITION_ID);
            return Wf.processById(definitionId).compose(definition ->
                this.flowStub.fetchFormStart(definition, header.getSigma()));
        } else {
            // Single Task
            final String instanceId = data.getString(KName.Flow.INSTANCE_ID);
            return Wf.instance(instanceId).compose(process -> {
                if (process.isRunning()) {
                    // Running Form
                    return this.flowStub.fetchForm(process.definition(), process.instance(), header.getSigma());
                } else {
                    // History Form
                    return this.flowStub.fetchFormEnd(process.definition(), process.instanceFinished(), header.getSigma());
                }
            });
        }
    }


    @Address(HighWay.Flow.BY_TODO)
    public Future<JsonObject> fetchTodo(final String key, final User user) {
        final String userId = Ux.keyUser(user);
        return this.taskStub.fetchTodo(key, userId);
    }

    @Address(HighWay.Flow.BY_HISTORY)
    public Future<JsonObject> fetchHistory(final String key) {
        return this.taskStub.fetchFinished(key);
    }
}
