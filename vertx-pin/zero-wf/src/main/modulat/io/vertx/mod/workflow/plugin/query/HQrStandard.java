package io.vertx.mod.workflow.plugin.query;

import io.horizon.eon.VString;
import io.horizon.specification.action.HQR;
import io.horizon.spi.business.ExUser;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

/**
 * 本组专用查询组件
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class HQrStandard implements HQR {
    /*
     * openGroup 包含本组
     * acceptedGroup 中可匹配本组
     */
    @Override
    public Future<JsonObject> compile(final JsonObject data, final JsonObject qr) {
        final String userKey = Ut.valueString(data, KName.USER);
        // 默认条件:  openGroup is null AND acceptedGroup is null
        final JsonObject defaultQr = Ux.whereAnd()
            .put("openGroup,n", VString.EMPTY)
            .put("acceptedGroup,n", VString.EMPTY);

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
