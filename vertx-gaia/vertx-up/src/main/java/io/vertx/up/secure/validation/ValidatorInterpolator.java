package io.vertx.up.secure.validation;

import io.horizon.uca.log.Annal;
import jakarta.el.ELManager;
import jakarta.el.ExpressionFactory;
import org.hibernate.validator.internal.engine.messageinterpolation.InterpolationTerm;
import org.hibernate.validator.internal.util.privilegedactions.GetClassLoader;
import org.hibernate.validator.internal.util.privilegedactions.SetContextClassLoader;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.spi.resourceloading.ResourceBundleLocator;

import java.security.PrivilegedAction;
import java.util.Locale;

@SuppressWarnings("all")
public class ValidatorInterpolator extends ValidatorMessager {
    private static final Annal LOGGER = Annal.get(ValidatorInterpolator.class);
    private final ExpressionFactory expressionFactory;

    public ValidatorInterpolator() {
        this.expressionFactory = buildExpressionFactory();
    }

    public ValidatorInterpolator(final ResourceBundleLocator userResourceBundleLocator) {
        super(userResourceBundleLocator);
        this.expressionFactory = buildExpressionFactory();
    }

    public ValidatorInterpolator(final ResourceBundleLocator userResourceBundleLocator, final ResourceBundleLocator contributorResourceBundleLocator) {
        super(userResourceBundleLocator, contributorResourceBundleLocator);
        this.expressionFactory = buildExpressionFactory();
    }

    public ValidatorInterpolator(final ResourceBundleLocator userResourceBundleLocator, final ResourceBundleLocator contributorResourceBundleLocator, final boolean cachingEnabled) {
        super(userResourceBundleLocator, contributorResourceBundleLocator, cachingEnabled);
        this.expressionFactory = buildExpressionFactory();
    }

    public ValidatorInterpolator(final ResourceBundleLocator userResourceBundleLocator, final boolean cachingEnabled) {
        super(userResourceBundleLocator, (ResourceBundleLocator) null, cachingEnabled);
        this.expressionFactory = buildExpressionFactory();
    }

    public ValidatorInterpolator(final ResourceBundleLocator userResourceBundleLocator, final boolean cachingEnabled, final ExpressionFactory expressionFactory) {
        super(userResourceBundleLocator, (ResourceBundleLocator) null, cachingEnabled);
        this.expressionFactory = expressionFactory;
    }

    private static ExpressionFactory buildExpressionFactory() {
        if (canLoadExpressionFactory()) {
            final ExpressionFactory expressionFactory = ELManager.getExpressionFactory();
            LOGGER.debug("Loaded expression factory via original TCCL");
            return expressionFactory;
        } else {
            final ClassLoader originalContextClassLoader = (ClassLoader) run(GetClassLoader.fromContext());

            try {
                run(SetContextClassLoader.action(ResourceBundleMessageInterpolator.class.getClassLoader()));
                final ExpressionFactory expressionFactory;
                final ExpressionFactory var2;
                if (canLoadExpressionFactory()) {
                    expressionFactory = ELManager.getExpressionFactory();
                    LOGGER.debug("Loaded expression factory via HV classloader");
                    var2 = expressionFactory;
                    return var2;
                }

                run(SetContextClassLoader.action(ELManager.class.getClassLoader()));
                if (canLoadExpressionFactory()) {
                    expressionFactory = ELManager.getExpressionFactory();
                    LOGGER.debug("Loaded expression factory via EL classloader");
                    var2 = expressionFactory;
                    return var2;
                }
            } catch (final Throwable var6) {
                //throw LOG.getUnableToInitializeELExpressionFactoryException(var6);
                LOGGER.fatal(var6);
                throw var6;
            } finally {
                run(SetContextClassLoader.action(originalContextClassLoader));
            }
            throw new RuntimeException("Expression Factory error.");
            //throw LOG.getUnableToInitializeELExpressionFactoryException((Throwable) null);
        }
    }

    private static boolean canLoadExpressionFactory() {
        try {
            ExpressionFactory.newInstance();
            return true;
        } catch (final Throwable var1) {
            return false;
        }
    }

    private static <T> T run(final PrivilegedAction<T> action) {
        return action.run();
        // Old
        // return System.getSecurityManager() != null ? AccessController.doPrivileged(action) : action.run();
    }

    @Override
    public String interpolate(final Context context, final Locale locale, final String term) {
        final InterpolationTerm expression = new InterpolationTerm(term, locale, this.expressionFactory);
        return expression.interpolate(context);
    }
}
