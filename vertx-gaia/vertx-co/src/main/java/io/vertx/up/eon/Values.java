package io.vertx.up.eon;

import java.nio.charset.Charset;

/**
 * Value constants
 */
public interface Values {
    /* Buffer size */
    int BUFFER_SIZE = 16;

    int CACHE_SIZE = 8 * 1024;

    String ENCODING = "UTF-8";

    String ENCODING_ISO_8859_1 = "ISO-8859-1";

    Charset DEFAULT_CHARSET = Charset.forName(ENCODING);

    int UNSET = -1;

    int CODECS = UNSET;

    int ZERO = 0;

    int IDX = 0;
    int IDX_1 = 1;
    int IDX_2 = 2;

    int RANGE = -1;

    int ONE = 1;

    int SINGLE = ONE;

    int TWO = 2;

    int THREE = 3;

    int FOUR = 4;

    String TRUE = "true";

    String FALSE = "false";

    String EMPTY_IDENTIFIER = "(`identifier` null)";

    String CONTENT_TYPE = "application/json;charset=UTF-8";

    String CONFIG_INTERNAL = "up";

    String CONFIG_INTERNAL_RULE = CONFIG_INTERNAL + "/rules/{0}.yml";
    String CONFIG_INTERNAL_FILE = CONFIG_INTERNAL + "/config/";
    String CONFIG_INTERNAL_PACKAGE = CONFIG_INTERNAL_FILE + "vertx-package-filter.json";
    // Aeon系统专用
    String CONFIG_INTERNAL_CLOUD = "aeon/contained/";
    char[] HEX_ARR = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    interface VS {
        String OLD = "__OLD__";
        String NEW = "__NEW__";
    }
}
