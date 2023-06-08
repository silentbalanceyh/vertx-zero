package io.vertx.up.unity;

import com.fasterxml.jackson.core.type.TypeReference;
import io.vertx.core.json.JsonArray;
import io.vertx.quiz.EpicBase;
import io.vertx.up.util.Ut;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class D10051Tc extends EpicBase {

    @Test
    public void testToJarray() {
        final JsonArray data = this.ioJArray("d10051.json");
        final List<D10051Obj> obj = Ut.deserialize(data, new TypeReference<List<D10051Obj>>() {
        });
        // Convert
        final JsonArray ret = Ux.toJson(obj, "d10051");
        System.err.println(ret.encode());
        Assert.assertEquals(2, ret.size());
    }
}
