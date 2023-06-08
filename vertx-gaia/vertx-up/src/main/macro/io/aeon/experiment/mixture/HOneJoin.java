package io.aeon.experiment.mixture;

import io.aeon.experiment.specification.KModule;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.shape.KJoin;
import io.vertx.up.atom.shape.KPoint;
import io.vertx.up.uca.jooq.UxJoin;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class HOneJoin implements HOne<UxJoin> {
    @Override
    public UxJoin combine(final KModule module, final KModule connect, final MultiMap headers) {
        Objects.requireNonNull(module);
        Objects.requireNonNull(connect);
        // 1. Build UxJoin Object
        final UxJoin dao = Ux.Join.on();

        // 2. Connect Extract
        final KJoin join = module.getConnect();
        final KPoint source = join.getSource();

        // 3. Module of Source
        final String keyJoin = source.getKeyJoin();
        if (Objects.isNull(keyJoin)) {
            // Joined by Primary Key
            dao.add(module.getDaoCls());
        } else {
            // Joined by keyJoin
            dao.add(module.getDaoCls(), keyJoin);
        }
        final String pojoS = module.getPojo();
        if (Ut.isNotNil(pojoS)) {
            dao.pojo(module.getDaoCls(), pojoS);
        }

        // 4. Connect
        final KPoint target = join.point(connect.identifier());
        Objects.requireNonNull(target);
        final Class<?> daoCls = connect.getDaoCls();
        dao.join(daoCls, target.getKeyJoin());

        // 5. Alias
        final JsonObject synonym = target.getSynonym();
        if (Ut.isNotNil(synonym)) {
            Ut.<String>itJObject(synonym, (aliasField, field) -> dao.alias(daoCls, field, aliasField));
        }

        // 6. Connect Joined pojo
        final String pojoT = connect.getPojo();
        if (Ut.isNotNil(pojoT)) {
            dao.pojo(connect.getDaoCls(), pojoT);
        }
        return dao;
    }
}
