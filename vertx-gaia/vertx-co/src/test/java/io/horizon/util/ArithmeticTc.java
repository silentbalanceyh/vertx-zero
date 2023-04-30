package io.horizon.util;

import io.vertx.ext.unit.TestContext;
import io.vertx.quiz.ZeroBase;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ArithmeticTc extends ZeroBase {
    @Test
    public void testIntersect(final TestContext context) {
        // final Set<String> left = new HashSet<>(Arrays.asList("1","23"));
        // final Set<String> right = new HashSet<>(Arrays.asList("1","2"));
        // final Set<String> exp = new HashSet<>(Arrays.asList("1","2"));

        final Set<String> left = new HashSet<String>() {
            {
                this.add("1");
                this.add("2");
            }
        };
        final Set<String> right = new HashSet<String>() {
            {
                this.add("3");
                this.add("4");
                this.add("1");
            }
        };
        final Set<String> exp = new HashSet<String>() {
            {
                this.add("1");
            }
        };
        context.assertEquals(exp, CArithmetic.intersect(left, right));
    }

    @Test
    public void testUnion(final TestContext context) {
        final Set<String> left = new HashSet<>(Arrays.asList("1"));
        final Set<String> right = new HashSet<>(Arrays.asList("1", "2"));
        final Set<String> exp = new HashSet<>(Arrays.asList("1", "2"));

        context.assertEquals(exp, CArithmetic.union(left, right));
    }

    @Test
    public void testDiff(final TestContext context) {
        final Set<String> subtrahend = new HashSet<>(Arrays.asList("1", "23"));
        final Set<String> minuend = new HashSet<>(Arrays.asList("1", "2"));
        final Set<String> exp = new HashSet<>(Arrays.asList("23"));

        context.assertEquals(exp, CArithmetic.diff(subtrahend, minuend));

    }
}
