package io.vertx.tp.workflow.refine;

import cn.zeroup.macrocosm.cv.WfCv;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class WfForm {

    static JsonObject argsForm(final JsonObject form, final String sigma) {
        final String definition = form.getString(KName.Flow.DEFINITION_KEY);
        final JsonObject parameters = new JsonObject();
        final String code = form.getString(KName.CODE);
        final String configFile = WfCv.ROOT_FOLDER + "/" + definition + "/" + code + ".json";
        // Dynamic Processing
        if (Ut.ioExist(configFile)) {
            parameters.put(KName.DYNAMIC, Boolean.FALSE);
            parameters.put(KName.CODE, configFile);
        } else {
            parameters.put(KName.DYNAMIC, Boolean.TRUE);
            parameters.put(KName.CODE, code);
            parameters.put(KName.SIGMA, sigma);
        }
        return parameters;
    }
}
