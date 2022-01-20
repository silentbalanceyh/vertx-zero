package cn.originx.uca.code;

import cn.vertxup.ambient.domain.tables.daos.XNumberDao;
import cn.vertxup.ambient.domain.tables.pojos.XNumber;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.data.DataAtom;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Queue;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class SeqAtom extends AbstractSeq<DataAtom> {

    private final transient Seq<String> fixed;

    SeqAtom(final String sigma) {
        super(sigma);
        this.fixed = new SeqCode(sigma);
    }

    @Override
    public Future<Queue<String>> generate(final DataAtom atom, final Integer counter) {
        Objects.requireNonNull(atom);
        final String identifier = atom.identifier();
        /*
         * WHERE SIGMA = ? AND IDENTIFIER = ?
         */
        final JsonObject condition = new JsonObject();
        condition.put(KName.SIGMA, this.sigma());
        condition.put(KName.IDENTIFIER, identifier);
        return Ux.Jooq.on(XNumberDao.class).<XNumber>fetchOneAsync(condition)
            .compose(Ut.ifNil(() -> null, (number) -> Ux.future(number.getCode())))
            .compose(code -> this.fixed.generate(identifier, counter));
    }
}
