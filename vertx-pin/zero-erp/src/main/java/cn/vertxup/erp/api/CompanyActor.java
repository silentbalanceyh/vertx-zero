package cn.vertxup.erp.api;

import cn.vertxup.erp.service.CompanyStub;
import io.horizon.uca.log.Annal;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.erp.cv.Addr;
import io.vertx.mod.erp.cv.ErpMsg;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import jakarta.inject.Inject;

import static io.vertx.mod.erp.refine.Er.LOG;

@Queue
public class CompanyActor {

    private static final Annal LOGGER = Annal.get(CompanyActor.class);

    @Inject
    private transient CompanyStub stub;

    @Address(Addr.Company.INFORMATION)
    public Future<JsonObject> company(final String employeeId) {
        LOG.Worker.info(LOGGER, ErpMsg.COMPANY_INFO, employeeId);
        return this.stub.fetchByEmployee(employeeId);
    }
}
