package io.aeon.runtime.channel;

import io.aeon.runtime.CRunning;
import io.horizon.eon.VValue;
import io.horizon.util.HUt;
import io.vertx.up.util.Ut;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AeonService {
    private static final String PREFIX = "META-INF/services/aeon/";
    private final Class<?> serviceCls;
    private final ClassLoader loader;
    private final Set<String> providerSet = new HashSet<>();
    private Enumeration<URL> configs;

    private AeonService(final Class<?> serviceCls, final ClassLoader loader) {
        this.serviceCls = serviceCls;
        this.loader = loader;
    }

    private static void fail(final Class<?> service, final String msg, final Throwable cause)
        throws ServiceConfigurationError {
        throw new ServiceConfigurationError(service.getName() + ": " + msg,
            cause);
    }

    private static void fail(final Class<?> service, final String msg)
        throws ServiceConfigurationError {
        throw new ServiceConfigurationError(service.getName() + ": " + msg);
    }

    private static void fail(final Class<?> service, final URL u, final int line, final String msg)
        throws ServiceConfigurationError {
        fail(service, u + ":" + line + ": " + msg);
    }

    @SuppressWarnings("unchecked")
    public static <T> T load(final Class<?> interfaceCls, final ClassLoader loader) {
        if (Objects.isNull(interfaceCls) || !interfaceCls.isInterface()) {
            return null;
        }
        return (T) CRunning.CC_SPI.pick(() -> {
            // AeonService instance
            final AeonService service = new AeonService(interfaceCls, loader);
            return service.service();
        }, interfaceCls);
    }

    @SuppressWarnings("unchecked")
    private <T> T service() {
        // Configuration
        this.aeonConfigure();
        // Load: /META-INF/services/aeon
        final Class<?> implCls = this.aeonSearch();
        if (Objects.nonNull(implCls)) {
            return Ut.instance(implCls);
        }
        // Load: /META-INF/services
        T reference = HUt.service((Class<T>) this.serviceCls, this.loader);
        if (Objects.isNull(reference)) {
            // Fix Liquibase Issue:
            /*
             * [ HED ] Missed `HED` component in service loader: META-INF/services/io.vertx.up.experiment.mixture.HED
             * This issue happened only when run `mvn liquibase:update`, because the class runtime is standalone
             */
            reference = HUt.service((Class<T>) this.serviceCls, this.serviceCls.getClassLoader());
        }
        return reference;
    }

    //    private <T> T service(final Class<T> serviceCls, final ClassLoader classLoader) {
    //        /*
    //         * Service Loader for lookup input interface implementation
    //         * This configuration must be configured in
    //         * META-INF/services/<interfaceCls Name> file
    //         */
    //        final ServiceLoader<T> loader = ServiceLoader.load(serviceCls, classLoader);
    //        /*
    //         * New data structure to put interface class into LEXEME_MAP
    //         * In current version, it support one to one only
    //         *
    //         * 1) The key is interface class name
    //         * 2) The found class is implementation name
    //         */
    //        T reference = null;
    //        for (final T t : loader) {
    //            reference = t;
    //            break;
    //        }
    //        return reference;
    //    }

    private void aeonConfigure() {
        try {
            final String fullName = PREFIX + this.serviceCls.getName();
            if (null == this.loader) {
                this.configs = ClassLoader.getSystemResources(fullName);
                /*
                 * Ignore following because of internal jdk
                 * else if (loader == ClassLoaders.platformClassLoader()) {
                            // The platform classloader doesn't have a class path,
                            // but the boot loader might.
                            if (BootLoader.hasClassPath()) {
                                configs = BootLoader.findResources(fullName);
                            } else {
                                configs = Collections.emptyEnumeration();
                            }
                        }
                 */
            } else {
                this.configs = this.loader.getResources(fullName);
            }
        } catch (final IOException ex) {
            fail(this.serviceCls, "Configuration Error", ex);
        }
    }

    private Class<?> aeonSearch() {
        if (!this.configs.hasMoreElements()) {
            return null;
        }
        Iterator<String> pending = null;
        while ((pending == null) || !pending.hasNext()) {
            pending = this.aeonParse(this.configs.nextElement());
        }
        final String className = pending.next();
        return Ut.clazz(className, null);
    }

    private int aeonParseLine(final URL u, final BufferedReader r, final int lc, final Set<String> names)
        throws IOException {
        String ln = r.readLine();
        if (ln == null) {
            return -1;
        }
        final int ci = ln.indexOf('#');
        if (ci >= 0) {
            ln = ln.substring(0, ci);
        }
        ln = ln.trim();
        final int n = ln.length();
        if (n != 0) {
            if ((ln.indexOf(' ') >= 0) || (ln.indexOf('\t') >= 0)) {
                fail(this.serviceCls, u, lc, "Illegal configuration-file syntax");
            }
            int cp = ln.codePointAt(0);
            if (!Character.isJavaIdentifierStart(cp)) {
                fail(this.serviceCls, u, lc, "Illegal provider-class name: " + ln);
            }
            final int start = Character.charCount(cp);
            for (int i = start; i < n; i += Character.charCount(cp)) {
                cp = ln.codePointAt(i);
                if (!Character.isJavaIdentifierPart(cp) && (cp != '.')) {
                    fail(this.serviceCls, u, lc, "Illegal provider-class name: " + ln);
                }
            }
            if (this.providerSet.add(ln)) {
                names.add(ln);
            }
        }
        return lc + 1;
    }

    @SuppressWarnings("all")
    private Iterator<String> aeonParse(final URL u) {
        final Set<String> names = new LinkedHashSet<>(); // preserve insertion order
        try {
            final URLConnection uc = u.openConnection();
            uc.setUseCaches(false);
            try (final InputStream in = uc.getInputStream();
                 final BufferedReader r = new BufferedReader(new InputStreamReader(in, VValue.DFT.CHARSET))) {
                int lc = 1;
                while ((lc = this.aeonParseLine(u, r, lc, names)) >= 0) {
                    ;
                }
            }
        } catch (final IOException x) {
            fail(this.serviceCls, "Error accessing configuration file", x);
        }
        return names.iterator();
    }
}
