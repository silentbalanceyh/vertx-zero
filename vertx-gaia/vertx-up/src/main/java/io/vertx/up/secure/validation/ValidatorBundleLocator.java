package io.vertx.up.secure.validation;

import io.horizon.uca.log.Annal;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.hibernate.validator.internal.util.Contracts;
import org.hibernate.validator.internal.util.logging.Messages;
import org.hibernate.validator.internal.util.privilegedactions.GetClassLoader;
import org.hibernate.validator.internal.util.privilegedactions.GetMethod;
import org.hibernate.validator.internal.util.privilegedactions.GetResources;
import org.hibernate.validator.spi.resourceloading.ResourceBundleLocator;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.PrivilegedAction;
import java.util.*;

@SuppressWarnings("all")
public class ValidatorBundleLocator implements ResourceBundleLocator {
    private static final Annal LOGGER = Annal.get(ValidatorBundleLocator.class);
    private static final boolean RESOURCE_BUNDLE_CONTROL_INSTANTIABLE = determineAvailabilityOfResourceBundleControl();
    private final String bundleName;
    private final ClassLoader classLoader;
    private final boolean aggregate;

    ValidatorBundleLocator(final String bundleName) {
        this(bundleName, (ClassLoader) null);
    }

    private ValidatorBundleLocator(final String bundleName, final ClassLoader classLoader) {
        this(bundleName, classLoader, false);
    }

    ValidatorBundleLocator(final String bundleName, final ClassLoader classLoader, final boolean aggregate) {
        Contracts.assertNotNull(bundleName, "bundleName");
        this.bundleName = bundleName;
        this.classLoader = classLoader;
        this.aggregate = aggregate && RESOURCE_BUNDLE_CONTROL_INSTANTIABLE;
    }

    private static <T> T run(final PrivilegedAction<T> action) {
        return action.run();
        // Old
        // return System.getSecurityManager() != null ? AccessController.doPrivileged(action) : action.run();
    }

    private static boolean determineAvailabilityOfResourceBundleControl() {
        try {
            final ResourceBundle.Control dummyControl = ValidatorBundleLocator.AggregateResourceBundle.CONTROL;
            if (dummyControl == null) {
                return false;
            } else {
                final Method getModule = (Method) run(GetMethod.action(Class.class, "getModule"));
                if (getModule == null) {
                    return true;
                } else {
                    final Object module = getModule.invoke(ValidatorBundleLocator.class);
                    final Method isNamedMethod = (Method) run(GetMethod.action(module.getClass(), "isNamed"));
                    final boolean isNamed = (Boolean) isNamedMethod.invoke(module);
                    return !isNamed;
                }
            }
        } catch (final Throwable var5) {
            LOGGER.info(Messages.MESSAGES.unableToUseResourceBundleAggregation());
            return false;
        }
    }

    @Override
    public ResourceBundle getResourceBundle(final Locale locale) {
        ResourceBundle rb = null;
        if (this.classLoader != null) {
            rb = this.loadBundle(this.classLoader, locale, this.bundleName + " not found by user-provided classloader");
        }

        ClassLoader classLoader;
        if (rb == null) {
            classLoader = (ClassLoader) run(GetClassLoader.fromContext());
            if (classLoader != null) {
                rb = this.loadBundle(classLoader, locale, this.bundleName + " not found by thread context classloader");
            }
        }

        if (rb == null) {
            classLoader = (ClassLoader) run(GetClassLoader.fromClass(ValidatorBundleLocator.class));
            rb = this.loadBundle(classLoader, locale, this.bundleName + " not found by validator classloader");
        }

        if (rb != null) {
            LOGGER.debug(INFO.BUNDLE_FOUND, this.bundleName);
        } else {
            LOGGER.debug(INFO.BUNDLE_NOT_FOUND, this.bundleName);
        }

        return rb;
    }

    private ResourceBundle loadBundle(final ClassLoader classLoader, final Locale locale, final String message) {
        ResourceBundle rb = null;

        try {
            if (this.aggregate) {
                rb = ResourceBundle.getBundle(this.bundleName, locale, classLoader, ValidatorBundleLocator.AggregateResourceBundle.CONTROL);
            } else {
                rb = ResourceBundle.getBundle(this.bundleName, locale, classLoader);
            }
        } catch (final MissingResourceException var6) {
            LOGGER.debug(var6.getMessage());
        }
        return rb;
    }

    private static class AggregateResourceBundleControl extends ResourceBundle.Control {
        private AggregateResourceBundleControl() {
        }

        @Override
        public ResourceBundle newBundle(final String baseName, final Locale locale, final String format, final ClassLoader loader, final boolean reload) throws IllegalAccessException, InstantiationException, IOException {
            if (!"java.properties".equals(format)) {
                return super.newBundle(baseName, locale, format, loader, reload);
            } else {
                final String resourceName = this.toBundleName(baseName, locale) + ".properties";
                final Properties properties = this.load(resourceName, loader);
                return properties.size() == 0 ? null : new ValidatorBundleLocator.AggregateResourceBundle(properties);
            }
        }

        private Properties load(final String resourceName, final ClassLoader loader) throws IOException {
            final Properties aggregatedProperties = new Properties();
            final Enumeration urls = (Enumeration) ValidatorBundleLocator.run(GetResources.action(loader, resourceName));

            while (urls.hasMoreElements()) {
                final URL url = (URL) urls.nextElement();
                final Properties properties = new Properties();
                properties.load(url.openStream());
                aggregatedProperties.putAll(properties);
            }
            return aggregatedProperties;
        }
    }

    private static class AggregateResourceBundle extends ResourceBundle {
        protected static final Control CONTROL = new ValidatorBundleLocator.AggregateResourceBundleControl();
        private final Properties properties;

        protected AggregateResourceBundle(final Properties properties) {
            this.properties = properties;
        }

        @Override
        protected Object handleGetObject(final String key) {
            return this.properties.get(key);
        }

        @Override
        public Enumeration<String> getKeys() {
            final Set<String> keySet = CollectionHelper.newHashSet();
            keySet.addAll(this.properties.stringPropertyNames());
            if (this.parent != null) {
                keySet.addAll(Collections.list(this.parent.getKeys()));
            }

            return Collections.enumeration(keySet);
        }
    }
}
