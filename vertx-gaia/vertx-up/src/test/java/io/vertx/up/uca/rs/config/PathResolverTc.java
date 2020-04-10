package io.vertx.up.uca.rs.config;

import io.vertx.ext.unit.TestContext;
import io.vertx.quiz.example.*;
import io.vertx.zero.exception.PathAnnoEmptyException;
import io.vertx.quiz.ZeroBase;
import io.vertx.up.runtime.ZeroHelper;
import org.junit.Test;

import javax.ws.rs.Path;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.TreeSet;

public class PathResolverTc extends ZeroBase {

    private String parseRoot(final Class<?> root) {
        final Path path = ZeroHelper.getPath(root);
        return PathResolver.resolve(path);
    }

    private Set<String> parseMethod(final Class<?> root) {
        final Set<String> pathes = new TreeSet<>();
        final Method[] methods = root.getDeclaredMethods();
        for (final Method method : methods) {
            final Path path = ZeroHelper.getPath(method);
            this.getLogger().info("[TEST] Parse info: {0}, method = {1}, Path = {2}.",
                    root.getName(),
                    method.getName(),
                    (null == path) ? null : path.value());
            if (null != path) {
                final String uri = PathResolver.resolve(
                        path, this.parseRoot(root));
                pathes.add(uri);
            }
        }
        this.getLogger().info("[TEST] Parsed :{0}", pathes);
        return pathes;
    }

    @Test(expected = PathAnnoEmptyException.class)
    public void testRte() {
        PathResolver.resolve(null);
    }

    @Test
    public void testRt1(final TestContext context) {
        context.assertEquals("/api", this.parseRoot(Root1.class));
    }

    @Test
    public void testRt2(final TestContext context) {
        context.assertEquals("/api", this.parseRoot(Root2.class));
    }

    @Test
    public void testRt3(final TestContext context) {
        context.assertEquals("/api", this.parseRoot(Root3.class));
    }

    @Test
    public void testMd1(final TestContext context) {
        final Set<String> pathes = this.parseMethod(Method1.class);
        final Set<String> expected = new TreeSet<String>() {
            {
                this.add("/api/test");
            }
        };
        context.assertEquals(expected, pathes);
    }

    @Test
    public void testMd2(final TestContext context) {
        final Set<String> pathes = this.parseMethod(Method2.class);
        final Set<String> expected = new TreeSet<String>() {
            {
                this.add("/api/test/:name");
            }
        };
        context.assertEquals(expected, pathes);
    }

    @Test
    public void testMd3(final TestContext context) {
        final Set<String> pathes = this.parseMethod(Method3.class);
        final Set<String> expected = new TreeSet<String>() {
            {
                this.add("/api/test/:name");
                this.add("/api/test/:id");
            }
        };
        context.assertEquals(expected, pathes);
    }

    @Test
    public void testMd4(final TestContext context) {
        final Set<String> pathes = this.parseMethod(Method4.class);
        final Set<String> expected = new TreeSet<String>() {
            {
                this.add("/test/:name");
                this.add("/test/:id");
            }
        };
        context.assertEquals(expected, pathes);
    }
}
