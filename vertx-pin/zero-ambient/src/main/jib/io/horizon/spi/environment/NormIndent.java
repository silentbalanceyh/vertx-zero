package io.horizon.spi.environment;

import cn.vertxup.ambient.service.DatumService;
import cn.vertxup.ambient.service.DatumStub;
import io.horizon.eon.VValue;
import io.horizon.spi.modeler.Indent;
import io.vertx.core.Future;
import io.vertx.up.exception.web._400SigmaMissingException;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class NormIndent implements Indent {

    private static final DatumStub stub = Ut.singleton(DatumService.class);

    @Override
    public Future<String> indent(final String code, final String sigma) {
        if (Ut.isNil(sigma)) {
            return Fn.outWeb(_400SigmaMissingException.class, this.getClass());
        }
        return stub.numberSigma(sigma, code, 1).compose(item -> {
            if (item.isEmpty()) {
                return Ux.future(null);
            } else {
                return Ux.future(item.getString(VValue.IDX));
            }
        });
    }

    @Override
    public Future<Boolean> reset(final String code, final String sigma, final Long defaultValue) {
        if (Ut.isNil(sigma)) {
            return Fn.outWeb(_400SigmaMissingException.class, this.getClass());
        }
        return stub.numberSigmaR(sigma, code, defaultValue);
    }

    @Override
    @SuppressWarnings("all")
    public Future<Queue<String>> indent(final String code, final String sigma, final int size) {
        if (Ut.isNil(sigma)) {
            return Fn.outWeb(_400SigmaMissingException.class, getClass());
        }
        return stub.numberSigma(sigma, code, size).compose(item -> {
            if (item.isEmpty()) {
                return Ux.future(new ConcurrentLinkedQueue<>());
            } else {
                return Ux.future(new ConcurrentLinkedQueue<>(item.getList()));
            }
        });
    }
}
