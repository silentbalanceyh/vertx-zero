package cn.originx.uca.hed;

import io.vertx.up.util.Ut;

public class KeyEncrypt {
    public static void main(final String[] args) {
        final String encrypt = Ut.encryptRSAP("pl,okm123YH");
        System.out.println(encrypt);
    }
}
