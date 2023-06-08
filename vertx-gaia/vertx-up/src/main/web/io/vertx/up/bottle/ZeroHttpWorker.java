package io.vertx.up.bottle;

import io.horizon.eon.VValue;
import io.horizon.uca.log.Annal;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.up.annotations.Ipc;
import io.vertx.up.annotations.Worker;
import io.vertx.up.atom.worker.Receipt;
import io.vertx.up.backbone.invoker.Invoker;
import io.vertx.up.backbone.invoker.InvokerUtil;
import io.vertx.up.backbone.invoker.JetSelector;
import io.vertx.up.commune.Envelop;
import io.vertx.up.fn.Fn;
import io.vertx.up.runtime.ZeroAnno;
import io.vertx.up.util.Ut;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Default Http Server worker for different handler.
 * Recommend: Do not modify any workers that vertx zero provided.
 */
@Worker
public class ZeroHttpWorker extends AbstractVerticle {

    private static final Annal LOGGER = Annal.get(ZeroHttpWorker.class);

    private static final Set<Receipt> RECEIPTS = ZeroAnno.getReceipts();

    private static final ConcurrentMap<Integer, Invoker> INVOKER_MAP =
        new ConcurrentHashMap<>();

    private static final AtomicBoolean LOGGED = new AtomicBoolean(Boolean.FALSE);

    @Override
    public void start() {
        // 1. Get event bus
        final EventBus bus = this.vertx.eventBus();
        // 2. Consume address
        for (final Receipt receipt : RECEIPTS) {
            // 3. Deploy for each type
            final String address = receipt.getAddress();

            // 4. Get target reference and method
            final Method method = receipt.getMethod();
            // final Object reference = receipt.getProxy();
            /*
             * In new version, there is not needed to verify
             * signature of length in arguments, because the new version
             * will support multi arguments in Worker component
             * Parameter length must be > 0.
             */
            InvokerUtil.verifyArgs(method, this.getClass());

            // length = 1
            final Class<?>[] params = method.getParameterTypes();
            final Class<?> returnType = method.getReturnType();
            final Class<?> paramCls = params[VValue.IDX];

            // 6. Invoker select
            final Invoker invoker = JetSelector.invoker(returnType, paramCls);
            invoker.ensure(returnType, paramCls);
            // 7. Record for different invokers
            INVOKER_MAP.put(receipt.hashCode(), invoker);

            Fn.runAt(() -> bus.<Envelop>consumer(address, message -> {
                /*
                 * 延迟初始化，在调用的时候再执行 Proxy 的提取
                 * 新版引入了注入结构，而 getProxy 具有延迟加载的效果，所以此处将
                 * reference 的提取放到 consumer 之后，前置绑定 class，此处初始化调用生成
                 * instance 的完整结构，防止 Actor 中实现 JSR-299 的注入问题
                 **/
                final Object reference = receipt.getProxy();

                if (method.isAnnotationPresent(Ipc.class)) {
                    // Rpc continue replying
                    invoker.next(reference, method, message, this.vertx);
                } else {
                    try {
                        /*
                         * Standard mode: Direct replying
                         * This mode is non micro-service and could call in most of our
                         * situations, instead we catch `blocked thread` issue in
                         * Invoker ( AsyncInvoker ) for future extend design here.
                         */
                        invoker.invoke(reference, method, message);
                    } catch (final Throwable ex) {
                        /*
                         * Error Occurs and fire message
                         */
                        ex.printStackTrace();
                        message.reply(Envelop.failure(ex));
                        // message.fail(0, ex.getMessage());
                    }
                }
            }), address, method);
        }
        // Record all the information;
        if (!LOGGED.getAndSet(Boolean.TRUE)) {
            final ConcurrentMap<Class<?>, Set<Integer>> outputMap = new ConcurrentHashMap<>();
            INVOKER_MAP.forEach((key, value) -> {
                if (outputMap.containsKey(value.getClass())) {
                    outputMap.get(value.getClass()).add(key);
                } else {
                    outputMap.put(value.getClass(), new HashSet<>());
                }
            });
            outputMap.forEach((key, value) -> LOGGER.info(INFO.ZeroHttpWorker.MSG_INVOKER, key, Ut.toString(value), String.valueOf(value.size())));
        }
    }
}
