package io.vertx.tp.jet.refine;

import cn.vertxup.jet.domain.tables.pojos.IApi;
import io.horizon.spi.jet.JtConsumer;
import io.vertx.core.AbstractVerticle;
import io.vertx.tp.error._500ConsumerSpecException;
import io.vertx.tp.error._500WorkerSpecException;
import io.vertx.tp.jet.atom.JtWorker;
import io.vertx.tp.jet.cv.JtConstant;
import io.vertx.tp.jet.cv.em.WorkerType;
import io.vertx.tp.jet.uca.tunnel.AdaptorChannel;
import io.vertx.up.eon.em.container.ChannelType;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.util.function.Supplier;

class JtType {

    private static Class<?> toWorker(final Supplier<String> supplier) {
        final String workerStr = supplier.get();
        final Class<?> clazz = Ut.clazz(workerStr, JtConstant.COMPONENT_DEFAULT_WORKER);
        Fn.out(AbstractVerticle.class != clazz.getSuperclass(), _500WorkerSpecException.class, JtRoute.class, clazz);
        return clazz;
    }

    private static Class<?> toConsumer(final Supplier<String> supplier) {
        final String consumerStr = supplier.get();
        final Class<?> clazz = Ut.clazz(consumerStr, JtConstant.COMPONENT_DEFAULT_CONSUMER);
        Fn.out(!Ut.isImplement(clazz, JtConsumer.class), _500ConsumerSpecException.class, JtRoute.class, clazz);
        return clazz;
    }


    static JtWorker toWorker(final IApi api) {
        final JtWorker worker = new JtWorker();

        /*
         * Worker object instance in current uri here.
         */
        worker.setWorkerAddress(api.getWorkerAddress());
        worker.setWorkerJs(api.getWorkerJs());
        worker.setWorkerType(Ut.toEnum(api::getWorkerType, WorkerType.class, WorkerType.STD));
        worker.setWorkerClass(toWorker(api::getWorkerClass));
        worker.setWorkerConsumer(JtType.toConsumer(api::getWorkerConsumer));
        return worker;
    }

    static Class<?> toChannel(final Supplier<String> supplier, final ChannelType type) {
        final Class<?> clazz;
        if (ChannelType.DEFINE == type) {
            /*
             * User defined channel class
             *  */
            final String channelClass = supplier.get();
            if (Ut.isNil(channelClass)) {
                /*
                 * Adaptor as default channel
                 *  */
                clazz = Pool.CHANNELS.get(ChannelType.ADAPTOR);
            } else {
                /*
                 * User defined channel as selected channel
                 *  */
                clazz = Ut.clazz(channelClass);
            }
        } else {
            /*
             * Here the type should be not "DEFINE", it used `Standard` channel
             * */
            clazz = Pool.CHANNELS.getOrDefault(type, AdaptorChannel.class);
        }
        return clazz;
    }
}
