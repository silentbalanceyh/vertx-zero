package io.vertx.up.util;

import io.vertx.ext.unit.TestContext;
import io.vertx.quiz.StoreBase;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;

public class IOTc extends StoreBase {
    @Test
    public void testProp(final TestContext context) {
        context.assertNotNull(IO.getProp(this.ioFile("in.properties")));
    }

    @Test
    public void testYaml(final TestContext context) {
        context.assertNotNull(IO.getYaml(this.ioFile("test.yaml")));
    }

    public void testBigFile(final TestContext context) throws Exception {
        final File file = new File("target.sql");
        if (!file.exists()) {
            file.createNewFile();
        }
        final FileOutputStream out = new FileOutputStream(file);
        final long start = System.nanoTime();
        Ut.ioOut(this.ioFile("data.sql"), out);
        final long end = System.nanoTime();
        System.err.println((end - start) + "ns");
    }
}
