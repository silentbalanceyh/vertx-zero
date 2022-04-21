package io.vertx.up.commune.wffs;

import io.vertx.core.json.JsonObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class Regulation implements Serializable {
    /*
     * Rule Extract Condition
     */
    private final JsonObject criteria = new JsonObject();
    /*
     * Rule Parameters for
     * - Tpl
     * - Message
     *
     * Clone for each
     */
    private final JsonObject params = new JsonObject();

    private final List<Formula> rules = new ArrayList<>();
}
