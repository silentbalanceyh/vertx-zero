package io.vertx.quiz;

import io.vertx.core.*;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.impl.Helper;
import io.vertx.ext.unit.impl.TestContextImpl;
import org.junit.*;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ZeroUnitRunner extends BlockJUnit4ClassRunner {

    private static final ThreadLocal<ZeroUnitRunner> currentRunner = new ThreadLocal<>();
    private static final LinkedList<Context> contextStack = new LinkedList<>();
    private static final LinkedList<Long> timeoutStack = new LinkedList<>();
    private final TestClass testClass;
    private final Map<String, Object> classAttributes = new HashMap<>();
    private TestContextImpl testContext;

    public ZeroUnitRunner(final Class<?> testClass) throws InitializationError {
        super(testClass);
        this.testClass = new TestClass(testClass);
    }

    static void pushContext(final Context context) {
        contextStack.push(context);
    }

    static void popContext() {
        contextStack.pop();
    }

    static void pushTimeout(final long timeout) {
        timeoutStack.push(timeout);
    }

    static void popTimeout() {
        timeoutStack.pop();
    }

    @Override
    protected void validatePublicVoidNoArgMethods(final Class<? extends Annotation> annotation, final boolean isStatic, final List<Throwable> errors) {
        if (annotation == Test.class || annotation == Before.class || annotation == After.class ||
            annotation == BeforeClass.class || annotation == AfterClass.class) {
            final List<FrameworkMethod> fMethods = this.getTestClass().getAnnotatedMethods(annotation);
            for (final FrameworkMethod fMethod : fMethods) {
                fMethod.validatePublicVoid(isStatic, errors);
                try {
                    this.validateTestMethod(fMethod);
                } catch (final Exception e) {
                    errors.add(e);
                }
            }
        } else {
            super.validatePublicVoidNoArgMethods(annotation, isStatic, errors);
        }
    }

    protected void validateTestMethod(final FrameworkMethod fMethod) throws Exception {
        final Class<?>[] paramTypes = fMethod.getMethod().getParameterTypes();
        if (!(paramTypes.length == 0 || (paramTypes.length == 1 && paramTypes[0].equals(TestContext.class)))) {
            throw new Exception("Method " + fMethod.getName() + " should have no parameters or " +
                "the " + TestContext.class.getName() + " parameter");
        }
    }

    @Override
    protected Statement methodInvoker(final FrameworkMethod method, final Object test) {
        final TestContextImpl ctx = this.testContext;
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                final Handler<TestContext> callback = ZeroUnitRunner.this.callback(method, test, null);
                ZeroUnitRunner.this.invokeExplosively(ctx, method, callback);
            }
        };
    }

    protected Object invokeTestMethod(final FrameworkMethod fMethod, final Object test, final TestContext context) throws InvocationTargetException, IllegalAccessException {
        final Method method = fMethod.getMethod();
        final Class<?>[] paramTypes = method.getParameterTypes();
        if (paramTypes.length == 0) {
            return method.invoke(test);
        } else {
            return method.invoke(test, context);
        }
    }

    private long getTimeout(final FrameworkMethod fMethod) {
        long timeout = 2 * 60 * 1000L;
        if (timeoutStack.size() > 0) {
            timeout = timeoutStack.peekLast();
        }
        final Test annotation = fMethod.getAnnotation(Test.class);
        if (annotation != null && annotation.timeout() > 0) {
            timeout = annotation.timeout();
        }
        return timeout;
    }

    @SuppressWarnings("all")
    private Handler<TestContext> callback(final FrameworkMethod fMethod, final Object test, final Handler<AsyncResult> handler) {
        return context -> {
            try {
                final Object ret = this.invokeTestMethod(fMethod, test, context);
                if (Objects.nonNull(handler)) {
                    if (ret instanceof Future) {
                        handler.handle((Future) ret);
                    } else {
                        handler.handle(Future.succeededFuture(ret));
                    }
                }
            } catch (final InvocationTargetException e) {
                Helper.uncheckedThrow(e.getCause());
            } catch (final IllegalAccessException e) {
                Helper.uncheckedThrow(e);
            }
        };
    }

    private void invokeExplosively(final TestContextImpl testContext, final FrameworkMethod fMethod, final Handler<TestContext> callback) throws Throwable {
        final long timeout = this.getTimeout(fMethod);
        currentRunner.set(this);
        final Context ctx = contextStack.peekLast();
        final CompletableFuture<Throwable> future = new CompletableFuture<>();
        if (ctx != null) {
            ctx.runOnContext(v -> {
                testContext.run(null, timeout, callback, future::complete);
            });
        } else {
            testContext.run(null, timeout, callback, future::complete);
        }
        Throwable failure;
        try {
            failure = future.get();
        } catch (final InterruptedException e) {
            // Should we do something else ?
            Thread.currentThread().interrupt();
            throw e;
        } finally {
            currentRunner.set(null);
        }
        if (failure != null) {
            throw failure;
        }
    }

    @Override
    protected Statement methodBlock(final FrameworkMethod method) {
        this.testContext = new TestContextImpl(new HashMap<>(this.classAttributes), null);
        final Statement statement = super.methodBlock(method);
        this.testContext = null;
        return statement;
    }

    @Override
    protected Statement withBefores(final FrameworkMethod method, final Object target, final Statement statement) {
        return this.withBefores(this.testContext, this.getTestClass().getAnnotatedMethods(Before.class), target, statement);
    }

    @Override
    protected Statement withAfters(final FrameworkMethod method, final Object target, final Statement statement) {
        final List<FrameworkMethod> afters = this.getTestClass().getAnnotatedMethods(After.class);
        return this.withAfters(this.testContext, afters, target, statement);
    }

    @Override
    protected Statement withBeforeClasses(final Statement statement) {
        final List<FrameworkMethod> befores = this.testClass.getAnnotatedMethods(BeforeClass.class);
        return this.withBefores(new TestContextImpl(this.classAttributes, null), befores, null, statement);
    }

    @Override
    protected Statement withAfterClasses(final Statement statement) {
        final List<FrameworkMethod> afters = this.getTestClass().getAnnotatedMethods(AfterClass.class);
        return this.withAfters(new TestContextImpl(this.classAttributes, null), afters, null, statement);
    }

    @SuppressWarnings("all")
    private Statement withBefores(final TestContextImpl testContext, final List<FrameworkMethod> befores, final Object target, final Statement statement) {
        if (befores.isEmpty()) {
            return statement;
        } else {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    final List<Future> futures = new ArrayList<>();
                    for (final FrameworkMethod before : befores) {
                        futures.add(ZeroUnitRunner.this.wrapRunner(testContext, before, target));
                    }
                    CompositeFuture.all(futures).onSuccess(res -> {
                        try {
                            statement.evaluate();
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    });
                }
            };
        }
    }

    private Future<Object> wrapRunner(final TestContextImpl testContext, final FrameworkMethod method, final Object target) throws Throwable {
        final Promise<Object> promise = Promise.promise();
        // Sync -> Async
        final Handler<TestContext> handler = this.callback(method, target, res -> promise.complete(res.result()));
        this.invokeExplosively(testContext, method, handler);
        return promise.future();
    }

    private Statement withAfters(final TestContextImpl testContext, final List<FrameworkMethod> afters, final Object target, final Statement statement) {
        if (afters.isEmpty()) {
            return statement;
        } else {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    final List<Throwable> errors = new ArrayList<Throwable>();
                    try {
                        statement.evaluate();
                    } catch (final Throwable e) {
                        errors.add(e);
                    } finally {
                        for (final FrameworkMethod after : afters) {
                            try {
                                final Handler<TestContext> callback = ZeroUnitRunner.this.callback(after, target, null);
                                ZeroUnitRunner.this.invokeExplosively(testContext, after, callback);
                            } catch (final Throwable e) {
                                errors.add(e);
                            }
                        }
                    }
                    MultipleFailureException.assertEmpty(errors);
                }
            };
        }
    }
}
