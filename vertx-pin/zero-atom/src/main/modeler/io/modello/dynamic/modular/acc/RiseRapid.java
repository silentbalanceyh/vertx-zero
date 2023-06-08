package io.modello.dynamic.modular.acc;

import cn.vertxup.atom.domain.tables.daos.MAccDao;
import cn.vertxup.atom.domain.tables.pojos.MAcc;
import io.horizon.eon.em.typed.ChangeFlag;
import io.macrocosm.specification.program.HArk;
import io.modello.eon.em.EmAttribute;
import io.modello.specification.action.HDao;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.atom.modeling.builtin.DataAtom;
import io.vertx.mod.atom.refine.Ao;
import io.vertx.up.commune.config.Database;
import io.vertx.up.commune.record.Apt;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class RiseRapid implements Rise {
    private transient Database database;

    @Override
    public Rise bind(final Database database) {
        this.database = database;
        return this;
    }

    @Override
    public Future<Apt> fetchBatch(final JsonObject criteria, final DataAtom atom) {
        return Fn.combineT(
            this.inputData(criteria, atom),
            this.inputAcc(criteria, atom),
            (data, acc) -> this.combineAcc(data, acc, atom)
        );
    }

    private Future<Apt> combineAcc(final JsonArray data, final JsonArray acc, final DataAtom atom) {
        final Apt apt = Apt.create(acc, data);
        final ConcurrentMap<ChangeFlag, JsonArray> compared = Ao.diffPure(acc, data, atom, atom.marker().disabled(EmAttribute.Marker.syncOut));
        return apt.comparedAsync(compared);
    }

    private Future<JsonArray> inputData(final JsonObject criteria, final DataAtom atom) {
        final HDao dao = Ao.toDao(atom, this.database);
        return dao.fetchAsync(criteria)
            .compose(records -> Ux.future(Ut.toJArray(records)));
    }

    @Override
    public Future<Boolean> writeData(final String key, final JsonArray data, final DataAtom atom) {
        return this.fetchAcc(key, atom).compose(queried -> {
            if (Objects.isNull(queried)) {
                // Add
                final MAcc acc = new MAcc();
                acc.setKey(UUID.randomUUID().toString());
                acc.setModelId(atom.identifier());
                acc.setModelKey(key);

                acc.setRecordJson(data.encode());
                acc.setActive(Boolean.TRUE);

                final HArk ark = atom.ark();
                acc.setLanguage(ark.language());
                acc.setSigma(ark.sigma());

                acc.setCreatedAt(LocalDateTime.now());
                acc.setUpdatedAt(LocalDateTime.now());
                return Ux.Jooq.on(MAccDao.class).insertAsync(acc);
            } else {
                // Update
                queried.setRecordJson(data.encode());
                queried.setUpdatedAt(LocalDateTime.now());
                return Ux.Jooq.on(MAccDao.class).updateAsync(queried);
            }
        }).compose(nil -> Ux.future(Boolean.TRUE));
    }

    private Future<JsonArray> inputAcc(final JsonObject criteria, final DataAtom atom) {
        final String modelKey = Ut.keyAtom(atom, criteria);
        return this.fetchAcc(modelKey, atom).compose(acc -> {
            if (Objects.isNull(acc)) {
                return Ux.futureA();
            } else {
                final JsonArray data = Ut.toJArray(acc.getRecordJson());
                return Ux.future(data);
            }
        });
    }

    private Future<MAcc> fetchAcc(final String modelKey, final DataAtom atom) {
        final JsonObject condition = new JsonObject();
        condition.put(KName.MODEL_KEY, modelKey);
        final HArk ark = atom.ark();
        condition.put(KName.SIGMA, ark.sigma());
        return Ux.Jooq.on(MAccDao.class).fetchOneAsync(condition);
    }
}
