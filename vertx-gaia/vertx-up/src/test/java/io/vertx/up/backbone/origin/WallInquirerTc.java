package io.vertx.up.backbone.origin;

import io.vertx.quiz.example.WallKeeper2;
import io.vertx.up.boot.origin.Inquirer;
import io.vertx.up.boot.origin.WallInquirer;
import io.vertx.up.commune.secure.Aegis;
import io.vertx.up.exception.boot.WallDuplicatedException;
import io.vertx.up.runtime.ZeroPack;
import io.vertx.up.util.Ut;
import org.junit.Test;

import java.util.Set;

public class WallInquirerTc {

    private final Inquirer<Set<Aegis>> walls =
        Ut.singleton(WallInquirer.class);

    @Test(expected = WallDuplicatedException.class)
    public void testScan() {
        final Set<Class<?>> classes = ZeroPack.getClasses();
        this.walls.scan(classes);
    }

    public void testScanCorrect() {
        final Set<Class<?>> classes = ZeroPack.getClasses();
        classes.remove(WallKeeper2.class);
        final Set<Aegis> treeResult = this.walls.scan(classes);
        for (final Aegis instance : treeResult) {
            System.out.println(instance);
        }
    }
}
