package io.vertx.up.runtime;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import io.horizon.eon.VPath;
import io.horizon.uca.log.Annal;
import io.vertx.core.impl.ConcurrentHashSet;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;
import org.junit.runner.RunWith;

import java.lang.reflect.Modifier;
import java.util.*;
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
        final JsonObject filter = Ut.ioJObject(VPath.SERVER.INTERNAL_PACKAGE);
        if (filter.containsKey("skip")) {
            final JsonArray skiped = filter.getJsonArray("skip");
            if (Objects.nonNull(skiped)) {
                LOGGER.info(Info.IGNORES, skiped.encodePrettily());
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
            CLASSES.addAll(scanned.stream().parallel()
                //                .filter(type -> !type.isAnonymousClass())                      // Ko Anonymous
                //                .filter(type -> !type.isAnnotation())                          // Ko Annotation
                //                .filter(type -> !type.isEnum())                                // Ko Enum
                //                .filter(type -> Modifier.isPublic(type.getModifiers()))        // Ko non-public
                //                // Ko abstract class, because interface is abstract, single condition is invalid
                //                .filter(type -> !(Modifier.isAbstract(type.getModifiers()) && !type.isInterface()))
                //                // .filter(type -> !Modifier.isAbstract(type.getModifiers()))  // Because interface is abstract
                //                .filter(type -> !Modifier.isStatic(type.getModifiers()))       // Ko Static
                //                .filter(type -> !Throwable.class.isAssignableFrom(type))       // Ko Exception
                //                .filter(type -> !type.isAnnotationPresent(RunWith.class))      // Ko Test Class
                .filter(ZeroPack::validType)
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

    private static boolean validType(final Class<?> type) {
        return !type.isAnonymousClass()                             // Ko Anonymous
            && !type.isAnnotation()                                 // Ko Annotation
            && !type.isEnum()                                       // Ko Enum
            && Modifier.isPublic(type.getModifiers())               // Ko non-public
            // Ko abstract class, because interface is abstract, single condition is invalid
            && !(Modifier.isAbstract(type.getModifiers()) && !type.isInterface())
            && !Modifier.isStatic(type.getModifiers())              // Ko Static
            && !Throwable.class.isAssignableFrom(type)              // Ko Exception
            && !type.isAnnotationPresent(RunWith.class)             // Ko Test Class
            && ZeroPack.validMember(type);                          // Ko `Method/Field`
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
        // 保证线程安全
        final Set<Class<?>> classSet = Collections.synchronizedSet(new HashSet<>());
        Fn.jvmAt(() -> {
            final ClassPath cp = ClassPath.from(Thread.currentThread().getContextClassLoader());
            final ImmutableSet<ClassPath.ClassInfo> set = cp.getTopLevelClasses();
            final ConcurrentMap<String, Set<String>> packageMap = new ConcurrentHashMap<>();
            // 性能提高一倍，并行流处理更合理，暂时没发现明显问题
            set.parallelStream().forEach(cls -> {
                final String packageName = cls.getPackageName();
                final boolean skip = FILTERS.stream().anyMatch(packageName::startsWith);
                if (!skip) {
                    try {
                        classSet.add(Thread.currentThread().getContextClassLoader().loadClass(cls.getName()));
                    } catch (Throwable ex) {

                    }
                }
            });
        });
        return classSet;
    }
}
