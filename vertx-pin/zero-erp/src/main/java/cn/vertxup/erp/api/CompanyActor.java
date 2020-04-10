package cn.vertxup.erp.api;

import cn.vertxup.erp.service.CompanyStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.erp.cv.Addr;
import io.vertx.tp.erp.cv.ErpMsg;
import io.vertx.tp.erp.refine.Er;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.log.Annal;

import javax.inject.Inject;

@Queue
public class CompanyActor {

    private static final Annal LOGGER = Annal.get(CompanyActor.class);

    @Inject
    private transient CompanyStub stub;

    @Address(Addr.Company.INFORMATION)
    public Future<JsonObject> company(final String employeeId) {
        Er.infoWorker(LOGGER, ErpMsg.COMPANY_INFO, employeeId);
        return this.stub.fetchByEmployee(employeeId);
    }
}
