package cn.vertxup.cache;

import cn.vertxup.jet.domain.tables.pojos.IApi;
import cn.vertxup.jet.domain.tables.pojos.IJob;
import cn.vertxup.jet.domain.tables.pojos.IService;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/*
 * Interface here for
 * 1) Update job by `JtJob`
 * -- ServiceEnvironment updating
 *
 */
public interface AmbientStub {
    /*
     * Job information updating by `AmbientStub`
     */
    Future<JsonObject> updateJob(IJob job, IService service);

    /*
     * Uri information updating by `AmbientStub`
     */
    Future<JsonObject> updateUri(IApi api, IService service);
}
