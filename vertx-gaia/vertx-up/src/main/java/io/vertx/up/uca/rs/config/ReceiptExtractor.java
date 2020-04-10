package io.vertx.up.uca.rs.config;

import io.reactivex.Observable;
import io.vertx.up.annotations.Address;
import io.vertx.up.atom.worker.Receipt;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.rs.Extractor;
import io.vertx.zero.exception.AddressWrongException;
import io.vertx.up.util.Ut;
import io.vertx.up.fn.Fn;
import io.vertx.up.runtime.Anno;
import io.vertx.up.runtime.ZeroAnno;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 * Scanned @Queue clazz to build Receipt metadata
 */
public class ReceiptExtractor implements Extractor<Set<Receipt>> {

    private static final Annal LOGGER = Annal.get(ReceiptExtractor.class);

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
                        .filter(Objects::nonNull)
                        // 4. Hit address
                        .subscribe(address -> ADDRESS.add(address.toString()))
                        .dispose())
                .dispose();
        /* 5.Log out address report **/
        LOGGER.info(Info.ADDRESS_IN, ADDRESS.size());
        ADDRESS.forEach(item -> LOGGER.info(Info.ADDRESS_ITEM, item));
    }

    @Override
    public Set<Receipt> extract(final Class<?> clazz) {
        return Fn.getNull(new HashSet<>(), () -> {
            // 1. Class verify
            Verifier.noArg(clazz, this.getClass());
            Verifier.modifier(clazz, this.getClass());
            // 2. Scan method to find @Address
            final Set<Receipt> receipts = new HashSet<>();
            final Method[] methods = clazz.getDeclaredMethods();
            Observable.fromArray(methods)
                    .filter(MethodResolver::isValid)
                    .filter(method -> method.isAnnotationPresent(Address.class))
                    .map(this::extract)
                    .filter(Objects::nonNull)
                    .subscribe(receipts::add)
                    .dispose();
            return receipts;
        }, clazz);
    }

    private Receipt extract(final Method method) {
        // 1. Scan whole Endpoints
        final Class<?> clazz = method.getDeclaringClass();
        final Annotation annotation = method.getDeclaredAnnotation(Address.class);
        final String address = Ut.invoke(annotation, "value");
        // 2. Ensure address incoming.
        Fn.outUp(!ADDRESS.contains(address), LOGGER,
                AddressWrongException.class,
                this.getClass(), address, clazz, method);

        final Receipt receipt = new Receipt();
        receipt.setMethod(method);
        receipt.setAddress(address);

        // Fix: Instance class for proxy
        final Object proxy = Ut.singleton(clazz);
        receipt.setProxy(proxy);
        return receipt;
    }
}
