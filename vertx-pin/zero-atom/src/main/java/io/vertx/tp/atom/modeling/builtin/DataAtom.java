package io.vertx.tp.atom.modeling.builtin;

import cn.vertxup.atom.domain.tables.pojos.MModel;
import io.aeon.experiment.shape.AbstractHAtom;
import io.modello.atom.app.KApp;
import io.vertx.tp.atom.modeling.Model;
import io.vertx.tp.atom.refine.Ao;

/**
 * 内部使用的元数据分析工具，提供
 * 当前 DataRecord的专用 辅助工具，核心元数据处理工厂
 */
public class DataAtom extends AbstractHAtom {

    public DataAtom(final KApp app, final Model model) {
        super(app, model);
        {
            // sigma / language / namespace re-bind
            final MModel modelRef = model.dbModel();
            this.app.bind(modelRef.getSigma(), modelRef.getLanguage());
        }
    }

    @Override
    public DataAtom atom(final String identifier) {
        final String appName = this.app.name();
        return Ao.toAtom(appName, identifier);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Model model() {
        return super.model();
    }
}
