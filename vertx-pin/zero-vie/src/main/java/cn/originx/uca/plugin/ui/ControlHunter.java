package cn.originx.uca.plugin.ui;

import cn.originx.uca.plugin.indent.KeyIndent;
import cn.vertxup.ui.domain.tables.daos.UiVisitorDao;
import cn.vertxup.ui.domain.tables.pojos.UiVisitor;
import io.horizon.spi.environment.Identifier;
import io.horizon.spi.ui.UiHunter;
import io.horizon.uca.log.Annal;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.unity.UData;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;

import static io.vertx.tp.ui.refine.Ui.LOG;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ControlHunter implements UiHunter {
    private static final Annal LOGGER = Annal.get(ControlHunter.class);
    private transient final Identifier indent = new KeyIndent();

    @Override
    public Future<String> seek(final UData data, final UiVisitor visitor) {
        final JsonObject normalized = new JsonObject();
        normalized.put(KName.DATA, data.dataJ());
        /*
         * normalized should be
         * {
         *      "data": {
         *          "categoryFirst": "??",
         *          "categorySecond": "??",
         *          "categoryThird": "??"
         *      }
         * }
         */
        return this.indent.resolve(normalized, data.config()).compose(identifier -> {
            /*
             * {
             *      "type": "组件类型：LIST | FORM",
             *      "sigma": "统一标识符",
             *      "page": "页面ID，对应 UI_PAGE 中的记录",
             *      "path": "三部分组成，前端自动计算的 view / position，配置中的 __ALIAS__ -> alias"
             * }
             *
             * identifier will be re-calculated based on resolution here
             */
            if (Ut.isNil(identifier)) {
                // null controlId returned
                return Ux.future();
            }
            final JsonObject criteria = new JsonObject();
            criteria.put(KName.IDENTIFIER, identifier);
            criteria.put(KName.TYPE, visitor.getType());
            criteria.put(KName.SIGMA, visitor.getSigma());
            criteria.put(KName.App.PATH, visitor.getPath());
            criteria.put(KName.Ui.PAGE, visitor.getPage());

            LOG.Ui.info(LOGGER, "Dynamic Control,  condition = `{0}`", criteria.encode());
            return Ux.Jooq.on(UiVisitorDao.class).<UiVisitor>fetchOneAsync(criteria);
        }).compose(searched -> {
            if (Objects.isNull(searched) || Ut.isNil(searched.getControlId())) {
                /*
                 * controlId = null
                 */
                return Ux.future();
            }
            return Ux.future(searched.getControlId());
        });
    }
}
