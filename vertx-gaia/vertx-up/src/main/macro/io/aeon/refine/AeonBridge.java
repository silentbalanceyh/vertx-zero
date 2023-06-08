package io.aeon.refine;

import io.horizon.uca.log.Annal;
import io.reactivex.rxjava3.core.Observable;
import io.vertx.up.annotations.Address;
import io.vertx.up.atom.worker.Receipt;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.runtime.Anno;
import io.vertx.up.runtime.ZeroAnno;
import io.vertx.up.util.Ut;
import io.vertx.zero.exception.AddressWrongException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 * Aeon 桥，用于桥接 Zero / Aeon 容器
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AeonBridge {

    private static final Annal LOGGER = Annal.get(AeonBridge.class);
    private static final Set<String> ADDRESS = new TreeSet<>();

    static {
        /* 1. Get all endpoints **/
        final Set<Class<?>> endpoints = ZeroAnno.getEndpoints();

        /* 2. Scan for @Address to matching **/
        Observable.fromIterable(endpoints)
            .map(queue -> Anno.query(queue, Address.class))
            // 3. Scan annotations
            .subscribe(annotations -> Observable.fromArray(annotations)
                .map(addressAnno -> Ut.invoke(addressAnno, "value"))
                // 4. Hit address
                .subscribe(address -> ADDRESS.add(address.toString()))
                .dispose())
            .dispose();
        /* 5.Log out address report **/
        LOGGER.info(INFO.ADDRESS_IN, ADDRESS.size());
        ADDRESS.forEach(item -> LOGGER.info(INFO.ADDRESS_ITEM, item));
    }

    /*
     * Aeon / Zero @Address 处理
     * 1. Method 转换成 Receipt 对象
     */
    public static Receipt receipt(final Method method) {
        // 1. Scan whole Endpoints
        final Class<?> clazz = method.getDeclaringClass();
        final Annotation annotation = method.getDeclaredAnnotation(Address.class);
        final String address = Ut.invoke(annotation, KName.VALUE);
        // 2. Ensure address incoming.
        Fn.outBoot(!ADDRESS.contains(address), LOGGER,
            AddressWrongException.class,
            AeonBridge.class, address, clazz, method);

        final Method replaced = ZeroAnno.getQaS(address);
        final Receipt receipt = new Receipt();
        receipt.setAddress(address);
        if (Objects.isNull(replaced)) {
            // Zero Workflow
            receipt.setMethod(method);
            //            final Object proxy = PLUGIN.createComponent(clazz);
            receipt.setProxy(clazz);
        } else {
            // Aeon Workflow
            receipt.setMethod(replaced);
            //            final Object proxy = PLUGIN.createComponent(replaced.getDeclaringClass());
            receipt.setProxy(replaced.getDeclaringClass());
        }
        return receipt;
    }
}
