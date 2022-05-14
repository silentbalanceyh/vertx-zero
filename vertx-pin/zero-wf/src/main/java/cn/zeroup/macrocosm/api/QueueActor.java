package cn.zeroup.macrocosm.api;

import cn.vertxup.workflow.domain.tables.daos.WTicketDao;
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
     *
     * OLD COMMENT:
     * /*
     * Get condition of query running
     * W_TICKET JOINED W_TODO
     * Here are situations
     * 1. Default:
     * -- toUser is my ( For Approval )
     * -- openBy is my ( For Draft )
     *
     * 2. Provide condition
     *   2.1. User
     * -- owner
     * -- supervisor
     * -- openBy
     * -- toUser
     *   2.2. Assignment
     * -- toDept
     * -- toTeam
     * -- toRole
     * -- toGroup
     *   2.3. Range Search
     * -- owner -> Me
     * -- supervisor -> Me
     * -- openBy -> Me
     * -- toUser -> Me
     * -- toDept -> Employee -> Me ( Nid )
     * -- toTeam -> Employee -> Me ( Nid )
     * -- toRole -> Role -> Me ( Nid )
     * -- toGroup -> Group -> Me ( Nid )
     *   2.4. Basic Search
     * -- name + Me
     * -- code + Me
     * All the condition could visit to Me, it means that it's not needed to add
     * assignment person to condition here, but the system should still add condition
     * of `status = PENDING | ACCEPTED | DRAFT` here
     *
     * 3. Basic Condition
     * -- WTicket, flowEnd = false ( Is Running )
     * -- WTodo, status
     *    -- When opened ( PENDING, ACCEPTED, DRAFT )
     *    -- When approved ( PENDING, ACCEPTED )
     *
     * The code logical is as following
     * 1) When condition provided, DEFAULT
     * 2) When condition contains value ( Not Empty ), User/Assignment
     * History Queue based on WTicket
     * - flowEnd = true
     * - WTicket is ok to display in the done queue
     */
    @Address(HighWay.Queue.TASK_QUEUE)
    public Future<JsonObject> fetchQueue(final JsonObject qr) {
        Wf.Log.initQueue(this.getClass(), "Qr Queue Input: {0}", qr.encode());
        // Status Must be in following
        // -- PENDING
        // -- DRAFT
        // -- ACCEPTED
        final JsonObject qrStatus = Ux.whereAnd();
        qrStatus.put(KName.STATUS + ",i", new JsonArray()
            .add(TodoStatus.PENDING.name())
            .add(TodoStatus.REDO.name())        // Redo,   Send the ticket back
            .add(TodoStatus.ACCEPTED.name())    // Reject, Reject the ticket for future
            .add(TodoStatus.DRAFT.name())       // Draft,  Edit the draft for redo submitting
        );
        final JsonObject qrCombine = Ux.whereQrA(qr, "$Q$", qrStatus);
        Wf.Log.initQueue(this.getClass(), "Qr Queue Combined: {0}", qrCombine.encode());
        return this.taskStub.fetchQueue(qrCombine); // this.condStub.qrQueue(qr, userId)
    }


    @Address(HighWay.Queue.TICKET_HISTORY)
    public Future<JsonObject> fetchHistory(final JsonObject qr) {
        return this.taskStub.fetchHistory(qr);
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
            return Wf.definitionById(definitionId).compose(definition ->
                this.flowStub.fetchFormStart(definition, header.getSigma()));
        } else {
            // Single Task
            final String instanceId = data.getString(KName.Flow.INSTANCE_ID);
            return Wf.definition(instanceId).compose(process -> {
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
