package cn.vertxup.erp.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public interface CompanyStub {
    /*
     * Get company information by `employeeId`
     * This interface is standalone and out of scope of CRUD module
     * For common usage, the interface could be found in CRUD, in this situation
     * It's not needed to define new api for that
     *
     * This api is exception situation and you could find company information by
     * employee id.
     */
    Future<JsonObject> fetchByEmployee(String employeeId);
}
