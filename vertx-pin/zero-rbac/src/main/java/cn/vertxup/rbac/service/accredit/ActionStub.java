package cn.vertxup.rbac.service.accredit;

import cn.vertxup.rbac.domain.tables.pojos.SAction;
import cn.vertxup.rbac.domain.tables.pojos.SResource;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;

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
}
