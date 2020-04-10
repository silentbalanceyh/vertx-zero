package io.vertx.up.util;

import io.vertx.ext.unit.TestContext;
import io.vertx.quiz.StoreBase;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ArithmeticTc extends StoreBase {
    @Test
    public void testIntersect(final TestContext context){
       // final Set<String> left = new HashSet<>(Arrays.asList("1","23"));
       // final Set<String> right = new HashSet<>(Arrays.asList("1","2"));
       // final Set<String> exp = new HashSet<>(Arrays.asList("1","2"));

        final Set<String> left = new HashSet<String>(){{
                add("1");
                add("2");
            }
        };
        final Set<String> right = new HashSet<String>(){{
                add("3");
                add("4");
                add("1");
            }
        };
        final Set<String> exp = new HashSet<String>(){{
                add("1");
            }
        };
        context.assertEquals(exp,Arithmetic.intersect(left,right));
    }

    @Test
    public void testUnion(final TestContext context){
        final Set<String> left = new HashSet<>(Arrays.asList("1"));
        final Set<String> right = new HashSet<>(Arrays.asList("1","2"));
        final Set<String> exp = new HashSet<>(Arrays.asList("1","2"));

        context.assertEquals(exp,Arithmetic.union(left,right));
    }

    @Test
    public void testDiff(final TestContext context){
        final Set<String> subtrahend = new HashSet<>(Arrays.asList("1","23"));
        final Set<String> minuend = new HashSet<>(Arrays.asList("1","2"));
        final Set<String> exp = new HashSet<>(Arrays.asList("23"));

        context.assertEquals(exp,Arithmetic.diff(subtrahend,minuend));

    }
}
