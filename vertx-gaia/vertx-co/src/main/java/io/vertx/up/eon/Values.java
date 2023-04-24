package io.vertx.up.eon;

import io.zero.cv.VValue;

/**
 * Value constants
 */
public interface Values extends VValue {

    String EMPTY_IDENTIFIER = "(`identifier` null)";

    String CONTENT_TYPE = "application/json;charset=UTF-8";

    String CONFIG_INTERNAL = "up";

    String CONFIG_INTERNAL_RULE = CONFIG_INTERNAL + "/rules/{0}.yml";
    String CONFIG_INTERNAL_FILE = CONFIG_INTERNAL + "/config/";
    String CONFIG_INTERNAL_PACKAGE = CONFIG_INTERNAL_FILE + "vertx-package-filter.json";
    // Aeon系统专用
    String CONFIG_INTERNAL_CLOUD = "aeon/contained/";

    interface VS {
        String OLD = "__OLD__";
        String NEW = "__NEW__";
    }
}
