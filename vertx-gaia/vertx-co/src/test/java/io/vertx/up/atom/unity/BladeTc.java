package io.vertx.up.atom.unity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ClassDeserializer;
import com.fasterxml.jackson.databind.ClassSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.quiz.ZeroBase;
import io.vertx.up.util.Ut;
import org.junit.Test;

public class BladeTc extends ZeroBase {

    @Test
    public void testBlade() {
        final JsonObject data = new JsonObject().put("email", "lang.yu@hpe.com");
        final UObject blade = Ut.deserialize(data, UObject.class);
        System.out.println(blade);
    }

    @Test
    public void testClass() {
        final JsonObject data = new JsonObject().put("name", "io.vertx.up.atom.unity.TestObject");
        final TestObject result = Ut.deserialize(data, TestObject.class);
        System.out.println(result.getClazz());
    }

    @Test
    public void testUson() {
        final JsonObject data = Ut.ioJObject(this.ioString("Uson.json"));
        final JsonObject result = UObject.create(data).convert("_id", "key").to();
        System.out.println(result.encodePrettily());
    }

    @Test
    public void testArray() {
        final JsonArray target = Ut.ioJArray(this.ioString("From.json"));
        final JsonArray source = Ut.ioJArray(this.ioString("To.json"));
        final JsonArray zip = Dual.zip(target, source, "name", "name1");
        System.out.println(zip);
        final JsonArray arr = UArray.create(target).zip(source, "name", "name1").to();
        System.out.println(arr);
    }
}

class TestObject {

    @JsonSerialize(using = ClassSerializer.class)
    @JsonDeserialize(using = ClassDeserializer.class)
    @JsonProperty("name")
    private Class<?> clazz;

    public Class<?> getClazz() {
        return this.clazz;
    }

    public void setClazz(final Class<?> clazz) {
        this.clazz = clazz;
    }
}
