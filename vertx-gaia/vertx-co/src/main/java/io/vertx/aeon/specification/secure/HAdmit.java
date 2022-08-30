package io.vertx.aeon.specification.secure;

import io.vertx.aeon.atom.secure.HPermit;
import io.vertx.aeon.eon.em.ScDim;
import io.vertx.aeon.eon.em.ScIn;
import io.vertx.aeon.runtime.H3H;
import io.vertx.aeon.specification.action.HEvent;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.em.run.ActPhase;
import io.vertx.up.experiment.mixture.HAtom;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HAdmit extends HEvent<HPermit, JsonObject> {
    /*
     *  Json -> HPermit
     *  该方法会在第二次请求中使用，根据输入的数据构造 HPermit，合并到第一流程中
     */
    static HPermit build(final JsonObject input) {
        // Build Cache Key Based on `sigma / code`
        final String sigma = Ut.valueString(input, KName.SIGMA);
        final String code = Ut.valueString(input, KName.CODE);
        Ut.notNull(sigma, code);
        // Store KApp information
        return H3H.CC_PERMIT.pick(() -> {

            // 绑定基础信息：code, sigma, name
            final HPermit permit = HPermit.create(code, sigma, Ut.valueString(input, KName.NAME));


            // 维度信息绑定
            final ScDim dmType = Ut.toEnum(() -> Ut.valueString(input, KName.DM_TYPE), ScDim.class, ScDim.NONE);
            permit.shape(dmType).shape(Ut.valueJObject(input, KName.MAPPING)).shape(
                Ut.valueJObject(input, KName.DM_CONDITION),
                Ut.valueJObject(input, KName.DM_CONFIG)
            );


            // 呈现信息绑定
            final ScIn uiType = Ut.toEnum(
                () -> Ut.valueString(input, KName.UI_TYPE), ScIn.class, ScIn.NONE);
            final ActPhase uiPhase = Ut.toEnum(
                () -> Ut.valueString(input, KName.PHASE), ActPhase.class, ActPhase.ERROR);

            permit.ui(uiType, uiPhase).ui(Ut.valueJObject(input, KName.UI_SURFACE)).ui(
                Ut.valueJObject(input, KName.UI_CONDITION),
                Ut.valueJObject(input, KName.UI_CONFIG)
            );
            return permit;

            // 缓存键值：<sigma> / <code>, code 是唯一的
        }, sigma + Strings.SLASH + code);
    }

    default HAdmit bind(final HAtom atom) {
        return this;
    }

    default HAdmit bind(final String sigma) {
        return this;
    }
}
