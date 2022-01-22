package io.vertx.tp.optic.business;

import cn.vertxup.ambient.service.DatumService;
import cn.vertxup.ambient.service.DatumStub;
import io.vertx.core.Future;
import io.vertx.up.eon.Values;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.List;

public class ExSerialEpic implements ExSerial {

    private final transient DatumStub stub = Ut.singleton(DatumService.class);

    @Override
    public Future<String> serial(final String sigma, final String code) {
        return this.stub.numberSigma(sigma, code, 1)
            .compose(generate -> Ux.future(generate.getString(Values.IDX)));
    }

    @Override
    public Future<Boolean> reset(final String sigma, final String code, final Long defaultValue) {
        return this.stub.numberSigmaR(sigma, code, defaultValue);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Future<List<String>> serial(final String sigma, final String code, final Integer counter) {
        return this.stub.numberSigma(sigma, code, counter)
            .compose(generate -> Ux.future(generate.getList()));
    }
}
