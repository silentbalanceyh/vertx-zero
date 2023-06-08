package io.vertx.up.secure.validation;

import io.horizon.uca.log.Annal;
import jakarta.validation.MessageInterpolator;
import jakarta.validation.ValidationException;
import org.hibernate.validator.internal.engine.messageinterpolation.InterpolationTermType;
import org.hibernate.validator.internal.engine.messageinterpolation.LocalizedMessage;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.MessageDescriptorFormatException;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.Token;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.TokenCollector;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.TokenIterator;
import org.hibernate.validator.internal.util.ConcurrentReferenceHashMap;
import org.hibernate.validator.spi.resourceloading.ResourceBundleLocator;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("all")
public abstract class ValidatorMessager implements MessageInterpolator {
    public static final String USER_VALIDATION_MESSAGES = "vertx-validation";
    public static final String CONTRIBUTOR_VALIDATION_MESSAGES = "ContributorValidationMessages";
    private static final Annal LOGGER = Annal.get(ValidatorMessager.class);
    private static final int DEFAULT_INITIAL_CAPACITY = 100;
    private static final float DEFAULT_LOAD_FACTOR = 0.75F;
    private static final int DEFAULT_CONCURRENCY_LEVEL = 16;
    private static final String DEFAULT_VALIDATION_MESSAGES = "org.hibernate.validator.ValidationMessages";
    private static final Pattern LEFT_BRACE = Pattern.compile("\\{", 16);
    private static final Pattern RIGHT_BRACE = Pattern.compile("\\}", 16);
    private static final Pattern SLASH = Pattern.compile("\\\\", 16);
    private static final Pattern DOLLAR = Pattern.compile("\\$", 16);
    private final Locale defaultLocale;
    private final ResourceBundleLocator userResourceBundleLocator;
    private final ResourceBundleLocator defaultResourceBundleLocator;
    private final ResourceBundleLocator contributorResourceBundleLocator;
    private final ConcurrentReferenceHashMap<LocalizedMessage, String> resolvedMessages;
    private final ConcurrentReferenceHashMap<String, List<Token>> tokenizedParameterMessages;
    private final ConcurrentReferenceHashMap<String, List<Token>> tokenizedELMessages;
    private final boolean cachingEnabled;

    public ValidatorMessager() {
        this((ResourceBundleLocator) null);
    }

    public ValidatorMessager(final ResourceBundleLocator userResourceBundleLocator) {
        this(userResourceBundleLocator, (ResourceBundleLocator) null);
    }

    public ValidatorMessager(final ResourceBundleLocator userResourceBundleLocator, final ResourceBundleLocator contributorResourceBundleLocator) {
        this(userResourceBundleLocator, contributorResourceBundleLocator, true);
    }

    public ValidatorMessager(final ResourceBundleLocator userResourceBundleLocator, final ResourceBundleLocator contributorResourceBundleLocator, final boolean cacheMessages) {
        this.defaultLocale = Locale.getDefault();
        if (userResourceBundleLocator == null) {
            this.userResourceBundleLocator = new ValidatorBundleLocator(USER_VALIDATION_MESSAGES);
        } else {
            this.userResourceBundleLocator = userResourceBundleLocator;
        }

        if (contributorResourceBundleLocator == null) {
            this.contributorResourceBundleLocator = new ValidatorBundleLocator("ContributorValidationMessages", (ClassLoader) null, true);
        } else {
            this.contributorResourceBundleLocator = contributorResourceBundleLocator;
        }

        this.defaultResourceBundleLocator = new ValidatorBundleLocator("org.hibernate.validator.ValidationMessages");
        this.cachingEnabled = cacheMessages;
        if (this.cachingEnabled) {
            this.resolvedMessages = new ConcurrentReferenceHashMap(100, 0.75F, 16, ConcurrentReferenceHashMap.ReferenceType.SOFT, ConcurrentReferenceHashMap.ReferenceType.SOFT, EnumSet.noneOf(ConcurrentReferenceHashMap.Option.class));
            this.tokenizedParameterMessages = new ConcurrentReferenceHashMap(100, 0.75F, 16, ConcurrentReferenceHashMap.ReferenceType.SOFT, ConcurrentReferenceHashMap.ReferenceType.SOFT, EnumSet.noneOf(ConcurrentReferenceHashMap.Option.class));
            this.tokenizedELMessages = new ConcurrentReferenceHashMap(100, 0.75F, 16, ConcurrentReferenceHashMap.ReferenceType.SOFT, ConcurrentReferenceHashMap.ReferenceType.SOFT, EnumSet.noneOf(ConcurrentReferenceHashMap.Option.class));
        } else {
            this.resolvedMessages = null;
            this.tokenizedParameterMessages = null;
            this.tokenizedELMessages = null;
        }

    }

    @Override
    public String interpolate(final String message, final Context context) {
        String interpolatedMessage = message;

        try {
            interpolatedMessage = this.interpolateMessage(message, context, this.defaultLocale);
        } catch (final MessageDescriptorFormatException var5) {
            LOGGER.warn(var5.getMessage());
        }

        return interpolatedMessage;
    }

    @Override
    public String interpolate(final String message, final Context context, final Locale locale) {
        String interpolatedMessage = message;

        try {
            interpolatedMessage = this.interpolateMessage(message, context, locale);
        } catch (final ValidationException var6) {
            LOGGER.warn(var6.getMessage());
        }

        return interpolatedMessage;
    }

    private String interpolateMessage(final String message, final Context context, final Locale locale) throws MessageDescriptorFormatException {
        String resolvedMessage = null;
        if (this.cachingEnabled) {
            resolvedMessage = (String) this.resolvedMessages.computeIfAbsent(new LocalizedMessage(message, locale), (lm) -> {
                return this.resolveMessage(message, locale);
            });
        } else {
            resolvedMessage = this.resolveMessage(message, locale);
        }

        if (resolvedMessage.indexOf(123) > -1) {
            resolvedMessage = this.interpolateExpression(new TokenIterator(this.getParameterTokens(resolvedMessage, this.tokenizedParameterMessages, InterpolationTermType.PARAMETER)), context, locale);
            resolvedMessage = this.interpolateExpression(new TokenIterator(this.getParameterTokens(resolvedMessage, this.tokenizedELMessages, InterpolationTermType.EL)), context, locale);
        }

        resolvedMessage = this.replaceEscapedLiterals(resolvedMessage);
        return resolvedMessage;
    }

    private List<Token> getParameterTokens(final String resolvedMessage, final ConcurrentReferenceHashMap<String, List<Token>> cache, final InterpolationTermType termType) {
        return this.cachingEnabled ? (List) cache.computeIfAbsent(resolvedMessage, (rm) -> {
            return (new TokenCollector(resolvedMessage, termType)).getTokenList();
        }) : (new TokenCollector(resolvedMessage, termType)).getTokenList();
    }

    private String resolveMessage(final String message, final Locale locale) {
        if (message.indexOf(123) < 0) {
            return message;
        } else {
            String resolvedMessage = message;
            final ResourceBundle userResourceBundle = this.userResourceBundleLocator.getResourceBundle(locale);
            final ResourceBundle constraintContributorResourceBundle = this.contributorResourceBundleLocator.getResourceBundle(locale);
            final ResourceBundle defaultResourceBundle = this.defaultResourceBundleLocator.getResourceBundle(locale);
            boolean evaluatedDefaultBundleOnce = false;

            while (true) {
                String userBundleResolvedMessage = this.interpolateBundleMessage(resolvedMessage, userResourceBundle, locale, true);
                if (!this.hasReplacementTakenPlace(userBundleResolvedMessage, resolvedMessage)) {
                    userBundleResolvedMessage = this.interpolateBundleMessage(resolvedMessage, constraintContributorResourceBundle, locale, true);
                }

                if (evaluatedDefaultBundleOnce && !this.hasReplacementTakenPlace(userBundleResolvedMessage, resolvedMessage)) {
                    return resolvedMessage;
                }

                resolvedMessage = this.interpolateBundleMessage(userBundleResolvedMessage, defaultResourceBundle, locale, false);
                evaluatedDefaultBundleOnce = true;
            }
        }
    }

    private String replaceEscapedLiterals(String resolvedMessage) {
        if (resolvedMessage.indexOf(92) > -1) {
            resolvedMessage = LEFT_BRACE.matcher(resolvedMessage).replaceAll("{");
            resolvedMessage = RIGHT_BRACE.matcher(resolvedMessage).replaceAll("}");
            resolvedMessage = SLASH.matcher(resolvedMessage).replaceAll(Matcher.quoteReplacement("\\"));
            resolvedMessage = DOLLAR.matcher(resolvedMessage).replaceAll(Matcher.quoteReplacement("$"));
        }

        return resolvedMessage;
    }

    private boolean hasReplacementTakenPlace(final String origMessage, final String newMessage) {
        return !origMessage.equals(newMessage);
    }

    private String interpolateBundleMessage(final String message, final ResourceBundle bundle, final Locale locale, final boolean recursive) throws MessageDescriptorFormatException {
        final TokenCollector tokenCollector = new TokenCollector(message, InterpolationTermType.PARAMETER);
        final TokenIterator tokenIterator = new TokenIterator(tokenCollector.getTokenList());

        while (tokenIterator.hasMoreInterpolationTerms()) {
            final String term = tokenIterator.nextInterpolationTerm();
            final String resolvedParameterValue = this.resolveParameter(term, bundle, locale, recursive);
            tokenIterator.replaceCurrentInterpolationTerm(resolvedParameterValue);
        }

        return tokenIterator.getInterpolatedMessage();
    }

    private String interpolateExpression(final TokenIterator tokenIterator, final Context context, final Locale locale) throws MessageDescriptorFormatException {
        while (tokenIterator.hasMoreInterpolationTerms()) {
            final String term = tokenIterator.nextInterpolationTerm();
            final String resolvedExpression = this.interpolate(context, locale, term);
            tokenIterator.replaceCurrentInterpolationTerm(resolvedExpression);
        }

        return tokenIterator.getInterpolatedMessage();
    }

    public abstract String interpolate(Context var1, Locale var2, String var3);

    private String resolveParameter(final String parameterName, final ResourceBundle bundle, final Locale locale, final boolean recursive) throws MessageDescriptorFormatException {
        String parameterValue;
        try {
            if (bundle != null) {
                parameterValue = bundle.getString(this.removeCurlyBraces(parameterName));
                if (recursive) {
                    parameterValue = this.interpolateBundleMessage(parameterValue, bundle, locale, recursive);
                }
            } else {
                parameterValue = parameterName;
            }
        } catch (final MissingResourceException var7) {
            parameterValue = parameterName;
        }

        return parameterValue;
    }

    private String removeCurlyBraces(final String parameter) {
        return parameter.substring(1, parameter.length() - 1);
    }
}
