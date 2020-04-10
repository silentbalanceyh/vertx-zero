package io.vertx.up.util;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.core.json.JsonArray;
import io.vertx.quiz.StoreBase;
import io.vertx.up.eon.Values;
import io.vertx.up.eon.em.CompressLevel;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.io.*;
import java.time.Instant;

import static org.junit.Assert.*;

/*
Author: chunmei deng
 */

public class OutTc extends StoreBase {

    @Test
    public void testWriteAll(@NotNull final TestContext context) throws IOException {
        String strInput="1qaz2wsx"+Instant.EPOCH;
        String strPath="12345";
        Out.write(strPath,strInput);

        File file =new File(strPath);
        if(file.exists()){
            InputStream input=new FileInputStream(file);
            byte[] data=new byte[1024];
            int len=input.read(data);
            String strResult=new String(data,0,len);
            context.assertEquals(strInput,strResult);
            file.delete();
        }

    }



    public enum EnumTest {
        MON, TUE, WED, THU, FRI, SAT, SUN;
    }

    public char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    @Test
    public void testWriteObject(final TestContext context) throws IOException {

        JsonObject JInput = new JsonObject();
        JInput.put("Name","Emma");
        JInput.put("Long",1000000l);
        JInput.put("Age",28);
        JInput.put("Mon",EnumTest.MON);
        JInput.put("Sun",EnumTest.SUN);
        JInput.put("Double",0.98);
        JInput.put("Float",0.9f);
        JInput.put("bool",true);
        JInput.put("byte",HEX_CHAR[6]);
        JInput.put("instant_epoch", Instant.EPOCH);
        JInput.put("instant_min", Instant.MIN);
        JInput.put("instant_max", Instant.MAX);
        String strPath="12345";
        Out.write(strPath,JInput);

        File file =new File(strPath);
        if(file.exists()){
            InputStream input=new FileInputStream(file);
            byte[] data=new byte[1024];
            int len=input.read(data);
            String strResult=new String(data,0,len);
            String strInput = null == JInput ? "{}" : JInput.encodePrettily();
            context.assertEquals(strInput,strResult);
            file.delete();
        }
    }

    @Test
    public void testWriteArray(final TestContext context) throws IOException {
        JsonArray JInput=new JsonArray();
        JInput.add(EnumTest.FRI);
        JInput.add(EnumTest.TUE);
        JInput.add(898877l);
        JInput.add(0.6f);
        JInput.add(90.678);
        JInput.add(false);
        JInput.add(9);
        JInput.add(Instant.MAX);
        JInput.add(HEX_CHAR[9]);
        JInput.add("hello");
        String strPath="12345";
        Out.write(strPath,JInput);

        File file =new File(strPath);
        if(file.exists()){
            InputStream input=new FileInputStream(file);
            byte[] data=new byte[1024];
            int len=input.read(data);
            String strResult=new String(data,0,len);
            String strInput = null == JInput ? "[]" : JInput.encodePrettily();
            context.assertEquals(strInput,strResult);
            file.delete();
        }
    }

    @Test
    public void testmake(final TestContext context) {
        String strPath="12345";
        Out.make(strPath);
        File file=new File(strPath);
        context.assertTrue(file.exists());
        file.delete();
    }

    @Test
    public void testwriteCompressAll(final TestContext context) throws IOException {
        String strInput="1qaz2wsx"+Instant.EPOCH;
        String strPath="12345";
        Out.writeCompress(strPath,strInput);

        File file =new File(strPath);
        if(file.exists()){
            InputStream input=new FileInputStream(file);
            byte[] data=new byte[1024];
            int len=input.read(data);
            final byte[] strResulttemp = Compressor.decompress(data);
            String strResult=new String(strResulttemp,Values.DEFAULT_CHARSET);
            context.assertEquals(strInput,strResult);
            file.delete();
        }
    }

    @Test
    public void testWriteCompressObject(final TestContext context) throws IOException {
        JsonObject JInput=new JsonObject();
        String strPath="12345";
        JInput.put("Name","Emma");
        JInput.put("Long",1000000l);
        JInput.put("Age",28);
        JInput.put("Mon",EnumTest.MON);
        JInput.put("Sun",EnumTest.SUN);
        JInput.put("Double",0.98);
        JInput.put("Float",0.9f);
        JInput.put("bool",true);
        JInput.put("byte",HEX_CHAR[6]);
        JInput.put("instant_epoch", Instant.EPOCH);
        JInput.put("instant_min", Instant.MIN);
        JInput.put("instant_max", Instant.MAX);
        Out.writeCompress(strPath,JInput);

        File file =new File(strPath);
        if(file.exists()){
            InputStream input=new FileInputStream(file);
            byte[] data=new byte[1024];
            int len=input.read(data);
            final byte[] strResulttemp = Compressor.decompress(data);
            String strResult=new String(strResulttemp,Values.DEFAULT_CHARSET);
            final String strInput = null == JInput ? "{}" : JInput.encode();
            context.assertEquals(strInput,strResult);
            file.delete();
        }
    }

    @Test
    public void testWriteCompressArray(final TestContext context) throws IOException {
        JsonArray JInput=new JsonArray();
        JInput.add(EnumTest.WED);
        JInput.add(EnumTest.THU);
        JInput.add(EnumTest.SAT);
        JInput.add(898877l);
        JInput.add(0.6f);
        JInput.add(90.678);
        JInput.add(false);
        JInput.add(9);
        JInput.add(Instant.MIN);
        JInput.add(HEX_CHAR[4]);
        JInput.add("world");
        String strPath="12345";
        Out.writeCompress(strPath,JInput);

        File file =new File(strPath);
        if(file.exists()){
            InputStream input=new FileInputStream(file);
            byte[] data=new byte[1024];
            int len=input.read(data);
            final byte[] strResulttemp = Compressor.decompress(data);
            String strResult=new String(strResulttemp,Values.DEFAULT_CHARSET);
            final String strInput = null == JInput ? "[]" : JInput.encode();
            context.assertEquals(strInput,strResult);
            file.delete();
        }
    }
}