package io.vertx.up.log;

import io.horizon.uca.log.Annal;
import io.horizon.uca.log.Log;
import io.vertx.quiz.ZeroBase;
import org.junit.Test;

public class AnnalTk extends ZeroBase {

    @Test
    public void testAnnal() {
        final Annal logger = Annal.get(AnnalTk.class);
        logger.info("Logger with No");
        logger.info("Logger with Args: {0}", "Hello");

        Log.info(AnnalTk.class, "Log.info : {0}", "Hello");
    }
}
