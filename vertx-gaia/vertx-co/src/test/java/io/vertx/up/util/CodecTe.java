package io.vertx.up.util;

import io.vertx.ext.unit.TestContext;
import io.vertx.quiz.ZeroBase;
import io.vertx.up.eon.Values;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;

/**
 * Author: chunmei deng
 * Modified: 2020/03/22
 */

public class CodecTe extends ZeroBase {

    final static String input = "  哈哈&%¥!~@#$^*()-=+_[]{};:'\"/?.>,<`……|  ～0【】「」《》，。？、";

    public static String encodeBase64(final byte[] inputx) throws Exception {
        final Class clazz = Class
                .forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");
        final Method mainMethod = clazz.getMethod("encode", byte[].class);
        mainMethod.setAccessible(true);
        final Object retObj = mainMethod.invoke(null, new Object[]{inputx});
        return (String) retObj;
    }

    public static byte[] decodeBase64(final String inputx) throws Exception {
        final Class clazz = Class
                .forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");
        final Method mainMethod = clazz.getMethod("decode", String.class);
        mainMethod.setAccessible(true);
        final Object retObj = mainMethod.invoke(null, inputx);
        return (byte[]) retObj;
    }

    @Test
    public void md5(final TestContext context) {
        context.assertEquals(CodecTc.MD5EncodeUtf8(input), Codec.md5(input));
    }

    @Test
    public void sha256(final TestContext context) throws UnsupportedEncodingException {
        context.assertEquals(CodecTc.sha256code(input), Codec.sha256(input));
    }

    @Test
    public void sha512(final TestContext context) throws UnsupportedEncodingException {
        context.assertEquals(CodecTc.sha512code(input), Codec.sha512(input));
    }

    @Test
    public void base64(final TestContext context) throws Exception {

        context.assertEquals(encodeBase64(input.getBytes(Values.DEFAULT_CHARSET)), Codec.base64(input, true));

        final String strOut = Codec.base64(input, true);
        context.assertEquals(new String(decodeBase64(strOut)), Codec.base64(strOut, false));
    }

    @Test
    public void url(final TestContext context) throws UnsupportedEncodingException {
        final String StrOut = Codec.url(input, true);
        context.assertEquals(StrOut, CodecTc.urlcode(input, false));
    }


}