package cn.vertxup.ui.api;

import cn.vertxup.ui.service.FieldStub;
import cn.vertxup.ui.service.FormStub;
import cn.vertxup.ui.service.OptionStub;
import io.horizon.uca.log.Annal;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.ui.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.unity.Ux;
import jakarta.inject.Inject;

import static io.vertx.mod.ui.refine.Ui.LOG;

@Queue
public class FormActor {

    private static final Annal LOGGER = Annal.get(FormActor.class);
    final private String FIELD_FIELDS = "fields";
    final private String FIELD_OPS = "ops";
    @Inject
    private transient FormStub formStub;
    @Inject
    private transient FieldStub fieldStub;
    @Inject
    private transient OptionStub optionStub;

    @Address(Addr.Control.PUT_FORM_CASCADE)
    public Future<JsonObject> putFormCascade(final String key, final JsonObject body) {
        final JsonArray fields = body.getJsonArray(this.FIELD_FIELDS);
        final JsonArray ops = body.getJsonArray(this.FIELD_OPS);

        LOG.Ui.info(LOGGER, "putFormCascade updating data: {0}", body.encodePrettily());
        return this.formStub.update(key, body)
            .compose(updatedForm -> this.fieldStub.updateA(key, fields))
            .compose(updatedFields -> {
                // return with updated fields
                body.put(this.FIELD_FIELDS, updatedFields);
                return this.optionStub.updateA(key, ops);
            })
            .compose(updatedOps -> {
                // return with updated ops
                body.put(this.FIELD_OPS, updatedOps);
                return Ux.future(body);
            });
    }

    @Address(Addr.Control.DELETE_FORM)
    public Future<Boolean> deleteForm(final String key) {
        return this.optionStub.deleteByControlId(key)
            .compose(result -> this.fieldStub.deleteByControlId(key))
            .compose(result -> this.formStub.delete(key));
    }
}
