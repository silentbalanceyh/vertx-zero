package io.vertx.up.runtime;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import io.vertx.core.impl.ConcurrentHashSet;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.Values;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;
import org.junit.runner.RunWith;

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * ZeroPack the package to extract classes.
 */
@SuppressWarnings("all")
public final class ZeroPack {

    private static final Annal LOGGER = Annal.get(ZeroPack.class);
    private static final Set<String> FILTERS = new TreeSet<>();
    private static final Set<Class<?>> CLASSES = new ConcurrentHashSet<>();

    static {
        /*
         * Read configuration to fill FILTERS;
         */
        final JsonObject filter = Ut.ioJObject(Values.CONFIG_INTERNAL_PACKAGE);
        if (filter.containsKey("skip")) {
            final JsonArray skiped = filter.getJsonArray("skip");
            if (Objects.nonNull(skiped)) {
                LOGGER.info(Info.IGNORES, skiped.encode());
                FILTERS.addAll(skiped.getList());
            }
        }
    }

    private ZeroPack() {
    }

    @SuppressWarnings("all")
    public static Set<Class<?>> getClasses() {
        /*
         * Get all packages that will be scanned.
         */
        if (CLASSES.isEmpty()) {
            // final Set<String> packageDirs = PackHunter.getPackages();
            // packageDirs.add(Strings.DOT);
            /*
             * Debug in file
             */
            /*
            final JsonArray debugPkg = new JsonArray();
            packageDirs.forEach(debugPkg::add);
            Ut.ioOut("/Users/lang/Out/out-package.json", debugPkg); */
            /*
             * Debug in package
             * Here I have tested package in total when development & production environment both.
             * The scanned package count are the same, it means that
             * here is no error when capture package here.
             * The left thing is that we should be sure class counter are the same as also.
             * 1) Current project classes
             * 2) For zero extension module, we also should add dependency classes into result.
             */

            final Set<Class<?>> scanned = getClassesInternal();
            // multiClasses(packageDirs.toArray(new String[]{}));
            CLASSES.addAll(scanned.stream()
                .filter(type -> !type.isAnonymousClass())                      // Ko Anonymous
                .filter(type -> !type.isAnnotation())                          // Ko Annotation
                .filter(type -> !type.isEnum())                                // Ko Enum
                .filter(type -> Modifier.isPublic(type.getModifiers()))        // Ko non-public
                // Ko abstract class, because interface is abstract, single condition is invalid
                .filter(type -> !(Modifier.isAbstract(type.getModifiers()) && !type.isInterface()))
                // .filter(type -> !Modifier.isAbstract(type.getModifiers()))     // Because interface is abstract
                .filter(type -> !Modifier.isStatic(type.getModifiers()))       // Ko Static
                .filter(type -> !Throwable.class.isAssignableFrom(type))       // Ko Exception
                .filter(type -> !type.isAnnotationPresent(RunWith.class))      // Ko Test Class
                .filter(ZeroPack::validMember)                                 // Ko `Method/Field`
                .collect(Collectors.toSet()));
            LOGGER.info(Info.CLASSES, String.valueOf(CLASSES.size()));
            /*
             * Debug in file
             */
            /*
            final Set<String> classSet = new TreeSet<>();
            CLASSES.forEach(clazz -> classSet.add(clazz.getName()));
            final JsonArray debugCls = new JsonArray();
            classSet.forEach(debugCls::add);
            Ut.ioOut("/Users/lang/Out/out.json", debugCls);*/
            // System.exit(0);
        }
        return CLASSES;
    }

    private static boolean validMember(final Class<?> type) {
        try {
            // Fix issue of Guice
            // java.lang.NoClassDefFoundError: camundajar/impl/scala/reflect/macros/blackbox/Context
            type.getDeclaredMethods();
            type.getDeclaredFields();
            return true;
        } catch (NoClassDefFoundError ex) {
            return false;
        }
    }

    @SuppressWarnings("all")
    private static Set<Class<?>> getClassesInternal() {
        final Set<Class<?>> classSet = new HashSet<>();
        Fn.safeJvm(() -> {
            final ClassPath cp = ClassPath.from(Thread.currentThread().getContextClassLoader());
            final ImmutableSet<ClassPath.ClassInfo> set = cp.getTopLevelClasses();
            final ConcurrentMap<String, Set<String>> packageMap = new ConcurrentHashMap<>();
            for (final ClassPath.ClassInfo cls : set) {
                final String packageName = cls.getPackageName();
                final boolean skip = FILTERS.stream().anyMatch(packageName::startsWith);
                if (!skip) {
                    try {
                        classSet.add(Thread.currentThread().getContextClassLoader().loadClass(cls.getName()));
                    } catch (Throwable ex) {

                    }
                }
            }
        });
        return classSet;
    }
}
