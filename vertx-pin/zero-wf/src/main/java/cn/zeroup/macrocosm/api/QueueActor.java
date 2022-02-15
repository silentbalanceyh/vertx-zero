package cn.zeroup.macrocosm.api;

import cn.vertxup.workflow.domain.tables.daos.WTicketDao;
import cn.zeroup.macrocosm.cv.HighWay;
import cn.zeroup.macrocosm.service.CondStub;
import cn.zeroup.macrocosm.service.FlowStub;
import cn.zeroup.macrocosm.service.TaskStub;
import io.vertx.core.Future;
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
    @Inject
    private transient CondStub condStub;

    /*
     * The basic qr contains three view:
     * 1. The `owner` is userId
     * 2. The `supervisor` is userId
     * 3. The `openBy` is userId
     *
     * The input qr should be following:
     * {
     *     "criteria": {
     *     }
     * }
     * The criteria fields = [
     *      "owner, W_TICKET, I'm the ticket owner.",
     *      "supervisor, W_TICKET, I'm the ticket supervisor.",
     *      "openBy, W_TICKET, I'm the openBy."
     * ]
     *
     * W_TICKET Join W_TODO by `traceId`,
     * renamed field:
     * - W_TODO status -> statusT
     * - W_TODO serial -> serialT
     */
    @Address(HighWay.Queue.TASK_QUEUE)
    public Future<JsonObject> fetchQueue(final JsonObject qr, final User user) {
        final String userId = Ux.keyUser(user);
        return this.condStub.qrQueue(qr, userId)
            .compose(this.taskStub::fetchQueue);
    }


    @Address(HighWay.Queue.TICKET_HISTORY)
    public Future<JsonObject> fetchHistory(final JsonObject qr, final User user) {
        final String userId = Ux.keyUser(user);
        return this.condStub.qrHistory(qr, userId)
            .compose(this.taskStub::fetchHistory);
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
        return this.taskStub.readPending(key, userId);
    }

    @Address(HighWay.Flow.BY_HISTORY)
    public Future<JsonObject> fetchHistory(final String key) {
        return this.taskStub.readFinished(key);
    }

    @Address(HighWay.Queue.TICKET_LINKAGE)
    public Future<JsonObject> searchTicket(final JsonObject query) {
        return Ux.Jooq.on(WTicketDao.class).searchAsync(query);
    }
}
