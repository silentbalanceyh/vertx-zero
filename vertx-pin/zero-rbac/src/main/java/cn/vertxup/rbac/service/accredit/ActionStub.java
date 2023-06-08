package cn.vertxup.rbac.service.accredit;

import cn.vertxup.rbac.domain.tables.pojos.SAction;
import cn.vertxup.rbac.domain.tables.pojos.SPermission;
import cn.vertxup.rbac.domain.tables.pojos.SResource;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.up.uca.soul.UriMeta;

import java.util.List;

/*
 * SAction means Backend event, it's in to SResource
 * Here are the relations between SAction & SResource
 * 1) The SAction could be identified by URI & Method, here URI means uri patterns, not request uri.
 * 2) The SResource could be triggered by multi SAction here, it means that front end events could send multi time.
 * Example:
 * UI buttons: btnSave, btnUpdate -> /save/1, /save/2
 * SActions:   /save/:id
 * SResource:  Abstract for SAction instead of real request URI
 */
public interface ActionStub {

    Future<SAction> fetchAction(String normalizedUri, HttpMethod method);

    Future<SAction> fetchAction(String normalizedUri, HttpMethod method, String sigma);

    Future<SResource> fetchResource(String key);

    /*
     * Action Sync based on permission
     * 1) Add
     * 2) Update
     * 3) Delete
     */
    Future<List<SAction>> saveAction(SPermission permission, JsonArray actionData);

    Future<List<SAction>> fetchAction(String permissionId);

    Future<Boolean> removeAction(String permissionId, String userKey);

    /*
     * Api selector for action to get all authorized apis here.
     * It will be used in following situations
     *
     * 1 - FormDesigner: form design tools usage.
     *
     * Workflow:
     *
     * 1) - Fetch Apis from `SEC_ACTION` directly
     * 2) - All the Apis have been authorized
     * 3) - This workflow is ( Step 1 ) workflow here.
     */
    Future<List<SAction>> searchAuthorized(String keyword, String sigma);

    /*
     * Api selector for permission/resource management.
     * All the apis could be extract here.
     *
     * 1 - I_API table stored
     * 2 - UriAeon that stored in memory
     *
     * Workflow:
     *
     * 1) - Fetch Apis from `UriAeon` class ( Memory Storage )
     * 2) - Fetch Apis from `I_API` ( By api channel )
     * 3) - Wrapper Apis with description ( The description came from two parts )
     *    -- 3.1. I_API ( comment first, then name )
     *    -- 3.2. UriAeon ( Normalized could be wrapper by frontend, others lefts )
     *
     */
    Future<List<UriMeta>> searchAll(String keyword, String sigma);
}
