package io.vertx.quiz.nova;

import io.vertx.core.json.JsonObject;
import io.vertx.quiz.cv.QzApi;
import io.vertx.up.eon.FileSuffix;
import io.vertx.up.eon.Strings;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

class QzIo {

    static String ioFile(final Class<?> clazz, final String input) {
        final String filename = clazz.getPackage().getName() + '/' + input;
        final Annal logger = Annal.get(clazz);
        logger.info("[ ZERO Qz ] Input File: {0}", filename);
        return filename + Strings.DOT + FileSuffix.JSON;
    }

    static QzApi getApi(final JsonObject input) {
        final String apiName = input.getString(QzApi.class.getSimpleName());
        return Fn.getNull(QzApi.DEFINED, () -> {
            final QzApi api = Ut.toEnum(QzApi.class, apiName);
            return Fn.getNull(QzApi.DEFINED, () -> api, api);
        }, apiName);
    }
}
