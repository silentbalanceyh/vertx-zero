package io.vertx.up.util;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.quiz.ZeroBase;
import io.vertx.up.eon.Values;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;

/*
Author: chunmei deng
 */

public class OutTc extends ZeroBase {

    public char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    @Test
    public void testWriteAll(final TestContext context) throws IOException {
        final String strInput = "1qaz2wsx" + Instant.EPOCH;
        final String strPath = "12345";
        Out.write(strPath, strInput);

        final File file = new File(strPath);
        if (file.exists()) {
            final InputStream input = new FileInputStream(file);
            final byte[] data = new byte[1024];
            final int len = input.read(data);
            final String strResult = new String(data, 0, len);
            context.assertEquals(strInput, strResult);
            context.assertTrue(file.delete());
        }

    }

    @Test
    public void testWriteObject(final TestContext context) throws IOException {

        final JsonObject JInput = new JsonObject();
        JInput.put("Name", "Emma");
        JInput.put("Long", 1000000l);
        JInput.put("Age", 28);
        JInput.put("Mon", EnumTest.MON);
        JInput.put("Sun", EnumTest.SUN);
        JInput.put("Double", 0.98);
        JInput.put("Float", 0.9f);
        JInput.put("bool", true);
        JInput.put("byte", this.HEX_CHAR[6]);
        JInput.put("instant_epoch", Instant.EPOCH);
        JInput.put("instant_min", Instant.MIN);
        JInput.put("instant_max", Instant.MAX);
        final String strPath = "12345";
        Out.write(strPath, JInput);

        final File file = new File(strPath);
        if (file.exists()) {
            final InputStream input = new FileInputStream(file);
            final byte[] data = new byte[1024];
            final int len = input.read(data);
            final String strResult = new String(data, 0, len);
            final String strInput = null == JInput ? "{}" : JInput.encodePrettily();
            context.assertEquals(strInput, strResult);
            file.delete();
        }
    }

    @Test
    public void testWriteArray(final TestContext context) throws IOException {
        final JsonArray JInput = new JsonArray();
        JInput.add(EnumTest.FRI);
        JInput.add(EnumTest.TUE);
        JInput.add(898877l);
        JInput.add(0.6f);
        JInput.add(90.678);
        JInput.add(false);
        JInput.add(9);
        JInput.add(Instant.MAX);
        JInput.add(this.HEX_CHAR[9]);
        JInput.add("hello");
        final String strPath = "12345";
        Out.write(strPath, JInput);

        final File file = new File(strPath);
        if (file.exists()) {
            final InputStream input = new FileInputStream(file);
            final byte[] data = new byte[1024];
            final int len = input.read(data);
            final String strResult = new String(data, 0, len);
            final String strInput = null == JInput ? "[]" : JInput.encodePrettily();
            context.assertEquals(strInput, strResult);
            file.delete();
        }
    }

    @Test
    public void testmake(final TestContext context) {
        final String strPath = "12345";
        Out.make(strPath);
        final File file = new File(strPath);
        context.assertTrue(file.exists());
        file.delete();
    }

    @Test
    public void testwriteCompressAll(final TestContext context) throws IOException {
        final String strInput = "1qaz2wsx" + Instant.EPOCH;
        final String strPath = "12345";
        Out.writeCompress(strPath, strInput);

        final File file = new File(strPath);
        if (file.exists()) {
            final InputStream input = new FileInputStream(file);
            final byte[] data = new byte[1024];
            final int len = input.read(data);
            final byte[] strResulttemp = Compressor.decompress(data);
            final String strResult = new String(strResulttemp, Values.DEFAULT_CHARSET);
            context.assertEquals(strInput, strResult);
            file.delete();
        }
    }

    @Test
    public void testWriteCompressObject(final TestContext context) throws IOException {
        final JsonObject JInput = new JsonObject();
        final String strPath = "12345";
        JInput.put("Name", "Emma");
        JInput.put("Long", 1000000l);
        JInput.put("Age", 28);
        JInput.put("Mon", EnumTest.MON);
        JInput.put("Sun", EnumTest.SUN);
        JInput.put("Double", 0.98);
        JInput.put("Float", 0.9f);
        JInput.put("bool", true);
        JInput.put("byte", this.HEX_CHAR[6]);
        JInput.put("instant_epoch", Instant.EPOCH);
        JInput.put("instant_min", Instant.MIN);
        JInput.put("instant_max", Instant.MAX);
        Out.writeCompress(strPath, JInput);

        final File file = new File(strPath);
        if (file.exists()) {
            final InputStream input = new FileInputStream(file);
            final byte[] data = new byte[1024];
            final int len = input.read(data);
            final byte[] strResulttemp = Compressor.decompress(data);
            final String strResult = new String(strResulttemp, Values.DEFAULT_CHARSET);
            final String strInput = null == JInput ? "{}" : JInput.encode();
            context.assertEquals(strInput, strResult);
            file.delete();
        }
    }

    @Test
    public void testWriteCompressArray(final TestContext context) throws IOException {
        final JsonArray JInput = new JsonArray();
        JInput.add(EnumTest.WED);
        JInput.add(EnumTest.THU);
        JInput.add(EnumTest.SAT);
        JInput.add(898877l);
        JInput.add(0.6f);
        JInput.add(90.678);
        JInput.add(false);
        JInput.add(9);
        JInput.add(Instant.MIN);
        JInput.add(this.HEX_CHAR[4]);
        JInput.add("world");
        final String strPath = "12345";
        Out.writeCompress(strPath, JInput);

        final File file = new File(strPath);
        if (file.exists()) {
            final InputStream input = new FileInputStream(file);
            final byte[] data = new byte[1024];
            final int len = input.read(data);
            final byte[] strResulttemp = Compressor.decompress(data);
            final String strResult = new String(strResulttemp, Values.DEFAULT_CHARSET);
            final String strInput = null == JInput ? "[]" : JInput.encode();
            context.assertEquals(strInput, strResult);
            file.delete();
        }
    }

    public enum EnumTest {
        MON, TUE, WED, THU, FRI, SAT, SUN;
    }
}