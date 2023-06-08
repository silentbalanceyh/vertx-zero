package io.aeon.uca.web.origin;

import io.aeon.annotations.QaS;
import io.horizon.eon.VMessage;
import io.horizon.runtime.Runner;
import io.horizon.uca.log.Annal;
import io.vertx.up.annotations.Address;
import io.vertx.up.boot.origin.Inquirer;
import io.vertx.up.util.Ut;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class HQaSInquirer implements Inquirer<ConcurrentMap<String, Method>> {
    private static final Annal LOGGER = Annal.get(HQaSInquirer.class);

    @Override
    public ConcurrentMap<String, Method> scan(final Set<Class<?>> clazzes) {
        final Set<Class<?>> qas = clazzes.stream()
            .filter((item) -> item.isAnnotationPresent(QaS.class))
            .collect(Collectors.toSet());
        // address = Method
        final ConcurrentMap<String, Method> result = new ConcurrentHashMap<>();
        Runner.run(qas, clazz -> result.putAll(this.scan(clazz)));
        LOGGER.info(VMessage.Inquirer.HQAS, qas.size(), result.keySet());
        return result;
    }

    private ConcurrentMap<String, Method> scan(final Class<?> clazz) {
        final ConcurrentMap<String, Method> result = new ConcurrentHashMap<>();

        final Method[] methods = clazz.getDeclaredMethods();
        Arrays.stream(methods)
            .filter(this::isValid)
            .filter(method -> method.isAnnotationPresent(Address.class))
            .forEach(method -> {
                final Annotation address = method.getAnnotation(Address.class);
                final String value = Ut.invoke(address, "value");
                result.put(value, method);
            });
        return result;
    }

    private boolean isValid(final Method method) {
        final int modifiers = method.getModifiers();
        return Modifier.isPublic(modifiers) && !Modifier.isStatic(modifiers) && !Modifier.isNative(modifiers);
    }
}
