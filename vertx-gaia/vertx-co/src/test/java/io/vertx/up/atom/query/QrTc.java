package io.vertx.up.atom.query;

import io.vertx.core.json.JsonObject;
import io.vertx.quiz.ZeroBase;
import org.junit.Test;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class QrTc extends ZeroBase {
    private Criteria criteria(final String fileJson) {
        final JsonObject json = this.ioJObject(fileJson);
        return Criteria.create(json);
    }

    @Test
    public void testDelete() {
        // Simple Removed
        Criteria criteria = this.criteria("cond-simple.json");

        criteria.removeBy("field1");
        System.out.println(criteria.toJson().encodePrettily());

        criteria.remove("field1");
        System.out.println(criteria.toJson().encodePrettily());

        // Simple Removed ( Tree )
        criteria = this.criteria("cond-complex.json");
        criteria.remove("field1");
        System.out.println(criteria.toJson().encodePrettily());

        // Removed ( Tree )
        criteria = this.criteria("cond-complex.json");
        criteria.removeBy("field1,=");
        System.out.println(criteria.toJson().encodePrettily());
    }
}
