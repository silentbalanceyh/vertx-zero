package cn.originx.uca.log;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.Model;
import io.vertx.tp.atom.modeling.data.DataAtom;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

import java.time.Instant;
import java.util.UUID;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
abstract class AbstractAuditor implements Auditor {
    protected transient final JsonObject options = new JsonObject();
    protected transient DataAtom atom;

    public AbstractAuditor(final JsonObject options) {
        if (Ut.notNil(options)) {
            this.options.mergeIn(options, true);
        }
    }

    @Override
    public Auditor bind(final DataAtom atom) {
        this.atom = atom;
        return this;
    }

    protected JsonObject initialize(final JsonObject record) {
        final JsonObject data = new JsonObject();
        // 模型信息填充
        final Model model = this.atom.model();
        data.put(KName.KEY, UUID.randomUUID().toString());
        data.put(KName.MODEL_ID, model.identifier());
        final String modelKey = Ao.toKey(record, this.atom);
        data.put(KName.MODEL_KEY, modelKey);

        // 附加信息
        data.put(KName.ACTIVE, Boolean.TRUE);
        data.put(KName.SIGMA, this.atom.sigma());
        data.put(KName.LANGUAGE, model.dbModel().getLanguage());
        final Instant now = Instant.now();
        data.put(KName.CREATED_AT, now);
        data.put(KName.UPDATED_AT, now);
        data.put(KName.CREATED_BY, record.getValue(KName.CREATED_BY));
        data.put(KName.UPDATED_BY, record.getValue(KName.UPDATED_BY));
        return data;
    }
}
