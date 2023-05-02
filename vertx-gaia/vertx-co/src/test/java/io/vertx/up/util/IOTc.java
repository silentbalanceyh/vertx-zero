package io.vertx.up.util;

import io.horizon.util.HUt;
import io.vertx.ext.unit.TestContext;
import io.vertx.quiz.ZeroBase;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;

public class IOTc extends ZeroBase {
    @Test
    public void testProp(final TestContext context) {
        context.assertNotNull(HUt.ioProperties(this.ioString("in.properties")));
    }

    @Test
    public void testYaml(final TestContext context) {
        context.assertNotNull(HUt.ioYaml(this.ioString("test.yaml")));
    }

    @SuppressWarnings("all")
    public void testBigFile(final TestContext context) throws Exception {
        final File file = new File("target.sql");
        if (!file.exists()) {
            file.createNewFile();
        }
        final FileOutputStream out = new FileOutputStream(file);
        final long start = System.nanoTime();
        Ut.ioOut(this.ioString("data.sql"), out);
        final long end = System.nanoTime();
        System.err.println((end - start) + "ns");
    }
}
