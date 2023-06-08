package io.vertx.up.backbone.config;

import io.aeon.refine.AeonBridge;
import io.vertx.up.annotations.Address;
import io.vertx.up.atom.worker.Receipt;
import io.vertx.up.backbone.Extractor;
import io.vertx.up.fn.Fn;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Scanned @Queue clazz to build Receipt metadata
 */
public class ReceiptExtractor implements Extractor<Set<Receipt>> {

    @Override
    public Set<Receipt> extract(final Class<?> clazz) {
        return Fn.runOr(new HashSet<>(), () -> {
            // 1. Class verify
            Verifier.noArg(clazz, this.getClass());
            Verifier.modifier(clazz, this.getClass());
            // 2. Scan method to find @Address
            final Set<Receipt> receipts = new HashSet<>();
            final Method[] methods = clazz.getDeclaredMethods();
            Arrays.stream(methods)
                .filter(MethodResolver::isValid)
                .filter(method -> method.isAnnotationPresent(Address.class))
                /*
                 * New workflow of @QaS / @Queue bridge
                 * -- @Queue / Zero Container Worker
                 * -- @QaS   / Aeon Container Worker
                 */
                .map(AeonBridge::receipt)
                .forEach(receipts::add);
            return receipts;
        }, clazz);
    }
}
