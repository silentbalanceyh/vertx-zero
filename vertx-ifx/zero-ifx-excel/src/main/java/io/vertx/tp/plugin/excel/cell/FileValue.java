package io.vertx.tp.plugin.excel.cell;

import io.horizon.eon.VString;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
public class FileValue implements ExValue {
    private static final Annal LOGGER = Annal.get(FileValue.class);

    @Override
    public String to(final Object value) {
        final String[] pathArr = value.toString().split(VString.COLON);
        String literal = value.toString();
        if (2 == pathArr.length) {
            final String path = pathArr[1];
            if (Ut.isNotNil(path)) {
                final JsonObject valueJ = new JsonObject();
                valueJ.put(Literal.K_TYPE, Literal.Prefix.FILE);

                final JsonObject content = new JsonObject();
                content.put(KName.App.PATH, path);
                valueJ.put(Literal.K_CONTENT, content);
                literal = valueJ.encodePrettily();
                LOGGER.info("[ Έξοδος ] （ExJson）File = {0}, File Value built `{1}`", path, literal);
            }
        }
        return literal;
    }
}
