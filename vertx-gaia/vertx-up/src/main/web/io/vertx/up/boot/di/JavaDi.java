package io.vertx.up.boot.di;

import com.google.inject.AbstractModule;
import io.horizon.uca.log.Annal;
import io.vertx.up.util.Ut;

import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Prepared
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@Deprecated
public class JavaDi<T extends I, I> extends AbstractModule {
    private static final Annal LOGGER = Annal.get(JavaDi.class);
    private final transient ConcurrentMap<Class<T>, Set<Class<I>>> classes
        = new ConcurrentHashMap<>();

    public JavaDi(final ConcurrentMap<Class<T>, Set<Class<I>>> classes) {
        this.classes.putAll(classes);
    }

    @Override
    protected void configure() {
        LOGGER.info("[ DI ] Java Bind Start......");
        final Set<String> oneOne = new HashSet<>();
        this.classes.forEach((impl, interfaceSet) -> interfaceSet.forEach(interfaceCls -> {
            final Constructor<T> constructor = Ut.constructor(impl);
            if (this.isOk(impl, interfaceCls, constructor)) {
                if (impl == interfaceCls) {
                    // No Interface Mode
                    oneOne.add(impl.getName());
                    this.bind(impl).toConstructor(constructor).asEagerSingleton();
                } else {
                    // Interface Mode ( More than one interface )
                    if (1 == interfaceSet.size()) {
                        LOGGER.info("[ DI ] 1 --> 1, Interface clazz bind = {0}, interface = {1}",
                            impl, interfaceCls);
                    } else {
                        LOGGER.info("[ DI ] 1 --> N, Interface clazz bind = {0}, interface = {1}",
                            impl, interfaceCls);
                    }
                    this.bind(interfaceCls).to(impl).asEagerSingleton();
                }
            }
        }));
        LOGGER.info("[ DI ] 0 <-> 0, Size = {0}, Content = {1}",
            String.valueOf(oneOne.size()), Ut.fromJoin(oneOne));
    }

    private boolean isOk(final Class<?> impl, final Class<?> interfaceCls, final Constructor<T> constructor) {
        if (Objects.isNull(constructor) || !Modifier.isPublic(constructor.getModifiers())) {
            // Ko non-public constructor
            return false;
        }
        final Member[] members = impl.getDeclaredFields();
        final Method[] methods = impl.getDeclaredMethods();
        final long memberCounter = Arrays.stream(members)
            .filter(member -> !Modifier.isStatic(member.getModifiers()))    // ko static
            .count();
        final long methodCounter = Arrays.stream(methods)
            .filter(member -> !Modifier.isStatic(member.getModifiers()))    // ko static
            .count();
        if (0 == memberCounter && 0 == methodCounter) {
            // Ko all static only
            return false;
        }
        if (impl != interfaceCls) {
            final Method[] interfaceMethod = interfaceCls.getMethods();
            // Ko some interface definition
            return 0 != interfaceMethod.length;
        }
        return true;
    }
}
