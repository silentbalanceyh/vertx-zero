package io.vertx.up.unity;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.quiz.ZeroBase;
import io.vertx.up.atom.typed.UArray;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class UxArrayTc extends ZeroBase {

    @Test
    public void testArray() {
        final JsonArray source = this.ioJArray("source.json");
        final JsonArray target = this.ioJArray("target.json");
        final JsonArray result =
            UArray.create(source).zip(target, "key", "roomId").to();
        for (int idx = 0; idx < result.size(); idx++) {
            final JsonObject item = result.getJsonObject(idx);
            Assert.assertNotNull(item);
        }
    }

    @Test
    public void testPojo() {
        final List<UserJson> user = new ArrayList<>();
        final UserJson json = new UserJson();
        json.setAge(13);
        json.setEmail("lang.yu@hpe.com");
        json.setName("Lang.Yu");
        user.add(json);
        Ux.futureA(user).onComplete(item -> {
            System.out.println(item.result());
        });
    }
}
