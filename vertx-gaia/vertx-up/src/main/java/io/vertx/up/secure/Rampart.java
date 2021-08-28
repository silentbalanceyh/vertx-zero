package io.vertx.up.secure;

import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.secure.Cliff;
import io.vertx.up.eon.em.WallType;
import io.vertx.up.fn.Fn;
import io.vertx.up.secure.jwt.JwtWall;
import io.vertx.up.uca.marshal.Transformer;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Rampart implements Transformer<Cliff> {
    private static final ConcurrentMap<WallType, Transformer<Cliff>> WALL_TRANSFORMER =
        new ConcurrentHashMap<WallType, Transformer<Cliff>>() {
            {
                this.put(WallType.JWT, Ut.singleton(JwtWall.class));
            }
        };

    //    static {
    //        Ut.clazzIf(Plugins.Default.WALL_MONGO, clazz -> WALL_TRANSFORMER.put(WallType.MONGO, Ut.singleton(clazz)));
    //    }

    @Override
    public Cliff transform(final JsonObject input) {

        return Fn.getJvm(() -> {
            if (input.containsKey("type")) {
                // Standard
                final Transformer<Cliff> transformer =
                    WALL_TRANSFORMER.get(WallType.from(input.getString("type")));
                return transformer.transform(input);
            } else {
                // Non Standard
                // TODO: Custom building.
                return new Cliff();
            }
        }, input);
    }
}
