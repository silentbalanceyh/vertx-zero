package io.vertx.tp.plugin.excel.cell;

import io.horizon.eon.VString;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.log.Annal;
import io.vertx.up.log.DevEnv;
import io.vertx.up.util.Ut;

/*
 * Fix issue of excel length: 32767 characters
 */
@SuppressWarnings("all")
public class JsonValue implements ExValue {
    private static final Annal LOGGER = Annal.get(JsonValue.class);

    @Override
    public String to(final Object value) {
        final String[] pathArr = value.toString().split(VString.COLON);
        String literal = value.toString();
        if (2 == pathArr.length) {
            final String path = pathArr[1];
            if (Ut.notNil(path)) {
                final String content = Ut.ioString(path.trim());
                if (Ut.notNil(content)) {
                    // 日志级别调整
                    if (DevEnv.devExcelRange()) {
                        LOGGER.info("[ Έξοδος ] （ExJson）File = {0}, Json Value captured `{1}`",
                            path, content);
                    }
                    if (Ut.isJArray(content)) {
                        final JsonArray normalized = Ut.toJArray(content);
                        literal = normalized.encodePrettily();
                    } else if (Ut.isJObject(content)) {
                        final JsonObject normalized = Ut.toJObject(content);
                        literal = normalized.encodePrettily();
                    }
                } else {
                    LOGGER.warn("[ Έξοδος ] （ExJson) File = {0} met error!!", path);
                }
            }
        }
        return literal;
    }
}
