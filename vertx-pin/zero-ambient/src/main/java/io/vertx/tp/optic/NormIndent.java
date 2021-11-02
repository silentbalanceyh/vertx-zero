package io.vertx.tp.optic;

import cn.vertxup.ambient.service.DatumService;
import cn.vertxup.ambient.service.DatumStub;
import io.vertx.core.Future;
import io.vertx.up.eon.Values;
import io.vertx.up.exception.web._400SigmaMissingException;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class NormIndent implements Indent {

    private static final DatumStub stub = Ut.singleton(DatumService.class);

    @Override
    public Future<String> indent(final String code, final String sigma) {
        if (Ut.isNil(sigma)) {
            return Ux.thenError(_400SigmaMissingException.class, this.getClass());
        }
        return stub.numbersBySigma(sigma, code, 1).compose(item -> {
            if (item.isEmpty()) {
                return Ux.future(null);
            } else {
                return Ux.future(item.getString(Values.IDX));
            }
        });
    }

    @Override
    @SuppressWarnings("all")
    public Future<Queue<String>> indent(final String code, final String sigma, final int size) {
        if (Ut.isNil(sigma)) {
            return Ux.thenError(_400SigmaMissingException.class, getClass());
        }
        return stub.numbersBySigma(sigma, code, size).compose(item -> {
            if (item.isEmpty()) {
                return Ux.future(new PriorityQueue<>());
            } else {
                return Ux.future(new PriorityQueue<>(item.getList()));
            }
        });
    }
}
