package io.vertx.up.boot.origin;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.horizon.uca.log.Annal;
import io.vertx.up.boot.di.DiGuice;
import io.vertx.up.boot.di.DiGuiceConstructor;
import io.vertx.up.boot.di.DiGuiceField;
import io.vertx.up.boot.di.DiGuiceMethod;
import io.vertx.up.util.Ut;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
public class GuiceInquirer implements Inquirer<Injector> {
    private static final Annal LOGGER = Annal.get(GuiceInquirer.class);
    private static final DiGuice jsrField = Ut.singleton(DiGuiceField.class);
    private static final DiGuice jsrMethod = Ut.singleton(DiGuiceMethod.class);
    private static final DiGuice jsrCon = Ut.singleton(DiGuiceConstructor.class);

    @Override
    @SuppressWarnings("all")
    public Injector scan(final Set<Class<?>> clazzes) {
        LOGGER.info("[ DI ] The DI environment will be initialized! Total = `{0}`", String.valueOf(clazzes.size()));
        /*
         * Scan start points, the condition is as following:
         * - 1. Contains member that annotated with @Inject
         * - 2. Constructor that annotated with @Inject
         * - 3. Method that annotated with @Inject
         */

        // The class that contains @Inject
        final Set<Class<?>> queueField = new HashSet<>();
        final Set<Class<?>> queueCon = new HashSet<>();
        final Set<Class<?>> queueMethod = new HashSet<>();
        // All interface queue
        final ConcurrentMap<Class<?>, Set<Class<?>>> tree = new ConcurrentHashMap<>();
        final Set<Class<?>> flat = new HashSet<>();
        clazzes.stream().filter(this::isValid).forEach(clazz -> {
            this.buildTree(tree, flat, clazz);
            if (!clazz.isInterface()) {
                if (jsrField.success(clazz)) {
                    queueField.add(clazz);
                } else if (jsrMethod.success(clazz)) {
                    queueMethod.add(clazz);
                } else if (jsrCon.success(clazz)) {
                    queueCon.add(clazz);
                }
            }
        });
        LOGGER.info("[ DI ] 1st scanned, field = {0}, method = {1}, constructor = {2}",
            String.valueOf(queueField.size()), String.valueOf(queueMethod.size()), String.valueOf(queueCon.size()));

        // Implementation = Interface
        // Standalone

        return Guice.createInjector(
            this.jsrField.module(tree, flat),       // Field
            this.jsrCon.module(tree, flat),         // Constructor
            this.jsrMethod.module(tree, flat)       // Method
            // new JavaDi(implMap),        // Java Specification ( IMPL Mode )
            // new JsrDi(interfaceMap)     // Jsr Specification ( interface Map )
        );
    }

    private void buildTree(final ConcurrentMap<Class<?>, Set<Class<?>>> tree,
                           final Set<Class<?>> flatSet,
                           final Class<?> clazz) {
        final Consumer<Class<?>> consumer = (item) -> {
            if (!tree.containsKey(item)) {
                tree.put(item, new HashSet<>());
            }
        };
        if (clazz.isInterface()) {
            consumer.accept(clazz);
        } else {
            final Class<?>[] interfacesCls = clazz.getInterfaces();
            if (0 == interfacesCls.length) {
                flatSet.add(clazz);
            } else {
                Arrays.stream(interfacesCls).forEach(interfaceCls -> {
                    consumer.accept(interfaceCls);
                    tree.get(interfaceCls).add(clazz);
                });
            }
        }
    }

    private boolean isValid(final Class<?> clazz) {
        // java.lang.NoClassDefFoundError
        final Class<?> existing = Ut.clazz(clazz.getName(), null);
        if (Objects.isNull(existing)) {
            return false;
        }
        final int modifier = clazz.getModifiers();
        if (!Modifier.isPublic(modifier)) {
            return false;           // Ko Non-Public
        }
        if (Modifier.isAbstract(modifier) && !clazz.isInterface()) {
            return false;           // Ko Abstract Class
        }
        if (clazz.isAnonymousClass()) {
            return false;           // Ko AnonymousClass
        }
        return !clazz.isEnum();     // Ko Enum
    }
}
