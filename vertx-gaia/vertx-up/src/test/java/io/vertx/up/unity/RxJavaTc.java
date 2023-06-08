package io.vertx.up.unity;

import io.reactivex.rxjava3.core.Observable;
import io.vertx.ext.unit.TestContext;
import io.vertx.quiz.ZeroBase;
import org.junit.Test;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RxJavaTc extends ZeroBase {

    private static final ConcurrentMap<String, String> MAP =
        new ConcurrentHashMap<String, String>() {{
            this.put("key1", "Windows1");
            this.put("key2", "Windows2");
            this.put("key3", "Windows3");
            this.put("field1", "Mac1");
            this.put("field2", "Mac2");
            this.put("field3", "Mac3");
        }};

    private static final Set<String> SETS = new HashSet<String>() {{
        this.add("key1");
        this.add("key2");
        this.add(null);
        this.add("field1");
    }};

    @Test
    public void testRxSet(final TestContext context) {
        final ConcurrentMap<String, String> result =
            new ConcurrentHashMap<>();
        Observable.fromIterable(MAP.keySet())
            .filter(Objects::nonNull)
            .groupBy(SETS::contains)
            .subscribe(item -> {
                if (item.getKey()) {
                    this.logger().info("Contains");
                    item.subscribe(value -> this.logger().info(value));
                } else {
                    this.logger().info("None");
                    item.subscribe(value -> this.logger().info(value));
                }
            });

    }
}
