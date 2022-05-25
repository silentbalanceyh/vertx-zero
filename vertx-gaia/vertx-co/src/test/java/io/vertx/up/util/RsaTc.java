package io.vertx.up.util;

import io.vertx.quiz.ZeroBase;
import org.junit.Ignore;
import org.junit.Test;

public class RsaTc extends ZeroBase {

    @Ignore
    @Test
    public void testRsa() {
        final String result = Rsa.encryptP("test plain text", "rsa_public_key.pem");
        System.out.println("Rsa encrypt result:" + result);
    }
}
