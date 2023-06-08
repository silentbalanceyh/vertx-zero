package io.vertx.up.uca.job.phase;

import io.horizon.atom.common.Refer;
import io.horizon.eon.VMessage;
import io.horizon.uca.log.Annal;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.up.atom.worker.Mission;
import io.vertx.up.commune.Envelop;
import io.vertx.up.exception.web._417JobMethodException;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class RunOn {
    private static final Annal LOGGER = Annal.get(RunOn.class);
    private transient final Vertx vertx;
    private transient final Refer underway = new Refer();

    RunOn(final Vertx vertx) {
        this.vertx = vertx;
    }

    RunOn bind(final Refer underway) {
        if (Objects.nonNull(underway)) {
            this.underway.add(underway.get());
        }
        return this;
    }

    Future<Envelop> invoke(final Envelop envelop, final Mission mission) {
        final Method method = mission.getOn();
        if (Objects.nonNull(method)) {
            Element.onceLog(mission, () -> LOGGER.info(VMessage.Job.PHASE.PHASE_3RD_JOB_RUN, mission.getCode(), method.getName()));
            return this.execute(envelop, method, mission);
        } else {
            return Ux.future(envelop);
        }
    }

    Future<Envelop> callback(final Envelop envelop, final Mission mission) {
        final Method method = mission.getOff();
        if (Objects.nonNull(method)) {
            Element.onceLog(mission, () -> LOGGER.info(VMessage.Job.PHASE.PHASE_6TH_JOB_CALLBACK, mission.getCode(), method.getName()));
            return this.execute(envelop, method, mission);
        } else {
            return Ux.future(envelop);
        }

    }

    private Future<Envelop> execute(final Envelop envelop, final Method method, final Mission mission) {
        if (envelop.valid()) {
            final Object proxy = mission.getProxy();
            try {
                final Object[] arguments = this.buildArgs(envelop, method, mission);
                return Ut.invokeAsync(proxy, method, arguments)
                    /* Normalizing data */
                    .compose(this::normalize);
            } catch (final Throwable ex) {
                ex.printStackTrace();
                return Future.failedFuture(ex);
            }
        } else {
            Element.onceLog(mission, () -> LOGGER.info(VMessage.Job.PHASE.ERROR_TERMINAL, mission.getCode(), envelop.error().getClass().getName()));
            return Ux.future(envelop);
        }
    }

    private <T> Future<Envelop> normalize(final T returnValue) {
        if (Objects.isNull(returnValue)) {
            // Return null
            return Ux.future(Envelop.okJson());
        } else {
            if (Envelop.class == returnValue.getClass()) {
                return Future.succeededFuture((Envelop) returnValue);
            } else {
                return Ux.future(Envelop.success(returnValue));
            }
        }
    }

    private Object[] buildArgs(final Envelop envelop, final Method method, final Mission mission) {
        /*
         * Available arguments:
         * -- Envelop
         * -- Mission
         * -- JsonObject -> Mission ( config )
         * */
        final Class<?>[] parameters = method.getParameterTypes();
        final List<Object> argsList = new ArrayList<>();
        if (0 < parameters.length) {
            for (final Class<?> parameterType : parameters) {
                // Old: TypedArgument.analyzeJob
                argsList.add(Ux.toParameter(envelop, parameterType, mission, this.underway));
            }
        } else {
            throw new _417JobMethodException(this.getClass(), mission.getCode());
        }
        return argsList.toArray();
    }
}
