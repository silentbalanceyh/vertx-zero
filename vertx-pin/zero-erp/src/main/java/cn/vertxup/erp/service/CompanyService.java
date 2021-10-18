package cn.vertxup.erp.service;

import cn.vertxup.erp.domain.tables.daos.ECompanyDao;
import cn.vertxup.erp.domain.tables.daos.EEmployeeDao;
import cn.vertxup.erp.domain.tables.pojos.EEmployee;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;

import java.util.Objects;

public class CompanyService implements CompanyStub {
    @Override
    public Future<JsonObject> fetchByEmployee(final String employeeId) {
        return Ux.Jooq.on(EEmployeeDao.class)
            .<EEmployee>fetchByIdAsync(employeeId)
            .compose(employee -> Ux.Jooq.on(ECompanyDao.class)
                .fetchByIdAsync(Objects.isNull(employee) ?
                    null : employee.getCompanyId()))
            .compose(Ux::futureJ);
    }
}
