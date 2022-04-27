package io.vertx.up.experiment.modeling;

import java.io.Serializable;

/**
 * M/U means modal unit, its format is following, please refer the related definition.yml file and definition.json
 * file for more details of KClass structure.
 *
 * KClass will be shared data structure ( JsonObject Part ) with KModule in up framework
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class KClass implements Serializable {
    private String identifier;
    private KHybrid hybrid;
}
