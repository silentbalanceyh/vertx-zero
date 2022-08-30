package io.vertx.tp.workflow.plugin.query;

import io.vertx.aeon.specification.query.HCond;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.optic.business.ExUser;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

/**
 * 本组专用查询组件
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class HQrStandard implements HCond {
    /*
     * openGroup 包含本组
     * acceptedGroup 中可匹配本组
     */
    @Override
    public Future<JsonObject> compile(final JsonObject data, final JsonObject qr) {
        final String userKey = Ut.valueString(data, KName.USER);
        // 默认条件:  openGroup is null AND acceptedGroup is null
        final JsonObject defaultQr = Ux.whereAnd()
                .put("openGroup,n", Strings.EMPTY)
                .put("acceptedGroup,n", Strings.EMPTY);

        return Ux.channel(ExUser.class, () -> defaultQr, stub -> stub.userGroup(userKey).compose(groups -> {
            // groups information
            if (groups.isEmpty()) {
                return Ux.future(defaultQr);
            }
            final JsonObject combineQr = new JsonObject();
            combineQr.put("$DFT$", defaultQr);
            // openGroup
            combineQr.put("openGroup,i", groups);
            // acceptedGroup
            Ut.itJArray(groups, String.class, (group, index) -> combineQr.put("acceptedGroup,c", group));

            return Ux.future(combineQr);
        }));
    }
}
