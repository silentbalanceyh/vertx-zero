package io.vertx.aeon.specification.app;

import io.vertx.aeon.atom.secure.Hoi;
import io.vertx.aeon.eon.em.ModeApp;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.experiment.specification.power.KApp;
import io.vertx.up.experiment.specification.power.KTenant;
import io.vertx.up.util.Ut;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractHET implements HET {
    @Override
    public Hoi configure(final JsonObject input) {
        final String tenantId = Ut.valueString(input, KName.Tenant.ID);
        final Hoi hoi;
        if (Ut.isNil(tenantId)) {
            // 独立模式
            hoi = Hoi.create(ModeApp.CUBE);
        } else {
            // 多租户 -> 子类可切换成多层
            hoi = Hoi.create(ModeApp.SPACE);
        }

        // 租户配置流程
        final KTenant tenant = new KTenant(tenantId);
        hoi.bind(tenant);

        // KApp 搜索流程
        final String sigma = Ut.valueString(input, KName.SIGMA);
        final String appId = Ut.valueString(input, KName.APP_ID);
        KApp app = HES.connect(sigma, appId);
        if (Objects.isNull(app)) {
            app = HES.connect();
        }
        final String language = Ut.valueString(input, KName.LANGUAGE);
        if (Objects.nonNull(app)) {
            /*
             * Fix issue: java.lang.NullPointerException
             * io.vertx.aeon.specification.app.AbstractHET.configure(AbstractHET.java:41)
             */
            app.bind(sigma, language);
        }
        return hoi.bind(app);
    }
}
