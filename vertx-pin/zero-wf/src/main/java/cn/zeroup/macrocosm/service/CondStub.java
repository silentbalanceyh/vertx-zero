package cn.zeroup.macrocosm.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/**
 * Condition for querying here, when you call task service,
 * pre-process to build condition
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface CondStub {
    /*
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
     */
    Future<JsonObject> qrQueue(JsonObject qr, String user);


    /*
     * History Queue based on WTicket
     * - flowEnd = true
     * - WTicket is ok to display in the done queue
     */
    Future<JsonObject> qrHistory(JsonObject qr, String user);
}
