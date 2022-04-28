package io.vertx.tp.atom.modeling.builtin;

import cn.vertxup.atom.domain.tables.pojos.MModel;
import io.vertx.tp.atom.modeling.Model;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.up.experiment.mixture.HModel;
import io.vertx.up.experiment.shape.AbstractHAtom;
import io.vertx.up.experiment.shape.HAtomReference;
import io.vertx.up.experiment.specification.KApp;

/**
 * 内部使用的元数据分析工具，提供
 * 当前 DataRecord的专用 辅助工具，核心元数据处理工厂
 */
public class DataAtom extends AbstractHAtom {

    public DataAtom(final Model model, final KApp app) {
        super(model, app);
        {
            // sigma / language / namespace re-bind
            final MModel modelRef = model.dbModel();
            this.app.sigma(modelRef.getSigma());
            this.app.language(modelRef.getLanguage());
        }
    }

    @Override
    protected <T extends HModel> HAtomReference newReference(final T model) {
        final String appName = this.app.appName();
        return new AtomReference((Model) model, appName);
    }

    @Override
    public DataAtom atom(final String identifier) {
        final String appName = this.app.appName();
        return Ao.toAtom(appName, identifier);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Model model() {
        return super.model();
    }
}
