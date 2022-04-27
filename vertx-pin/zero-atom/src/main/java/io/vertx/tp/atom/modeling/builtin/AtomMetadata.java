package io.vertx.tp.atom.modeling.builtin;

import cn.vertxup.atom.domain.tables.pojos.MAttribute;
import cn.vertxup.atom.domain.tables.pojos.MModel;
import io.vertx.tp.atom.modeling.Model;
import io.vertx.up.experiment.mixture.HTField;
import io.vertx.up.experiment.shape.atom.HAtomMetadata;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 * 模型基本信息
 */
class AtomMetadata extends HAtomMetadata {

    AtomMetadata(final Model modelRef) {
        super(modelRef);
    }

    @Override
    public HTField toField(final String name) {
        final MAttribute attribute = this.model().dbAttribute(name);
        return HTField.create(name, attribute.getAlias());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Model model() {
        return super.model();
    }

    // ====================== Children Method ======================
    /* 返回统一标识符 */
    String sigma() {
        return this.sure(MModel::getSigma);
    }

    String language() {
        return this.sure(MModel::getLanguage);
    }

    private <T> T sure(final Function<MModel, T> function) {
        final MModel model = this.model().dbModel();
        if (Objects.isNull(model)) {
            return null;
        } else {
            return function.apply(model);
        }
    }
}
