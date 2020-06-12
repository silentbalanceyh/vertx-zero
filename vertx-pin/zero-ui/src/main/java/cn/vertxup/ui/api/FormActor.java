package cn.vertxup.ui.api;

import cn.vertxup.ui.service.FieldStub;
import cn.vertxup.ui.service.FormStub;
import cn.vertxup.ui.service.OptionStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ui.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.unity.Ux;

import javax.inject.Inject;

@Queue
public class FormActor {

    @Inject
    private transient FormStub formStub;

    @Inject
    private transient FieldStub fieldStub;

    @Inject
    private transient OptionStub optionStub;

    @Address(Addr.Control.PUT_FORM_CASCADE)
    public Future<JsonArray> putFormCascade(final String sigma, final String key, final JsonObject body) {

        return Ux.future(new JsonArray());
    }

    @Address(Addr.Control.DELETE_FORM)
    public Future<Boolean> deleteForm(final String sigma, final String key) {
        return optionStub.deleteByControlId(key)
                .compose(result -> fieldStub.deleteByControlId(key))
                .compose(result -> formStub.delete(key));
    }
}
