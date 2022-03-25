package cn.vertxup.battery.api;

import cn.vertxup.battery.service.BagArgStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.battery.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Me;
import io.vertx.up.annotations.Queue;

import javax.inject.Inject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@Queue
public class BagArgActor {

    @Inject
    private transient BagArgStub bagArgStub;

    @Address(Addr.Argument.BAG_ARGUMENT)
    public Future<JsonObject> fetchBag(final String bagId) {
        return this.bagArgStub.fetchBagConfig(bagId);
    }

    @Address(Addr.Argument.BAG_ARGUMENT_VALUE)
    public Future<JsonObject> fetchBagData(final String bagId) {
        return this.bagArgStub.fetchBag(bagId);
    }

    @Address(Addr.Argument.BAG_CONFIGURE)
    @Me
    public Future<JsonObject> saveBag(final String bagId, final JsonObject config) {
        return this.bagArgStub.saveBag(bagId, config);
    }
}
