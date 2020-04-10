package io.vertx.up.unity;

import io.reactivex.Observable;
import io.vertx.ext.unit.TestContext;
import io.vertx.quiz.StoreBase;
import org.junit.Test;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RxJavaTc extends StoreBase {

    private static final ConcurrentMap<String, String> MAP =
            new ConcurrentHashMap<String, String>() {{
                put("key1", "Windows1");
                put("key2", "Windows2");
                put("key3", "Windows3");
                put("field1", "Mac1");
                put("field2", "Mac2");
                put("field3", "Mac3");
            }};

    private static final Set<String> SETS = new HashSet<String>() {{
        add("key1");
        add("key2");
        add(null);
        add("field1");
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
                        getLogger().info("Contains");
                        item.subscribe(value -> getLogger().info(value));
                    } else {
                        getLogger().info("None");
                        item.subscribe(value -> getLogger().info(value));
                    }
                });

    }
}
