package io.mature.extension.uca.hed;

import io.vertx.up.util.Ut;

public class KeyEncrypt {
    public static void main(final String[] args) {
        final String encrypt = Ut.encryptRSAP("xxxxxx123YH");
        System.out.println(encrypt);
    }
}
