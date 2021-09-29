package io.vertx.tp.plugin.mongo;

import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.Ruler;
import io.vertx.up.atom.secure.Aegis;
import io.vertx.up.eon.em.WallType;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.marshal.Transformer;

public class MongoWall implements Transformer<Aegis> {

    private static final Annal LOGGER = Annal.get(MongoWall.class);

    @Override
    public Aegis transform(final JsonObject input) {
        Fn.outUp(() -> Ruler.verify("wall-mongo", input), LOGGER);
        final Aegis aegis = new Aegis();
        aegis.setType(WallType.MONGO);
        aegis.setConfig(input.getJsonObject("config"));
        return aegis;
    }
}
