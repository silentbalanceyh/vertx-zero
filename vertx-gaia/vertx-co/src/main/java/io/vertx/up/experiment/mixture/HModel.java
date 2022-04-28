package io.vertx.up.experiment.mixture;

import io.vertx.up.eon.em.atom.ModelType;
import io.vertx.up.experiment.mu.KMarker;
import io.vertx.up.experiment.rule.RuleUnique;

import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HModel extends HApp {
    /*
     * Rule that is stored into model table or model object
     * it's related to build RuleUnique object/reference instead of
     */
    RuleUnique rule();

    /*
     * The type of model here
     * - DIRECT
     * - VIEW
     * - JOINED
     *
     * There are situations as following:
     * 1) Direct: mapped to database metadata stored. ( Zero Extension Model )
     * 2) View: mapped to database metadata view stored. ( Zero Extension Model )
     * 3) Joined: mapped to joined metadata ( Code Logical Extension or Other )
     */
    ModelType type();

    /*
     * The attribute names hash set here
     *
     * Attribute name building
     */
    Set<String> attribute();

    /*
     * The attribute extracting method by `name`
     */
    HAttribute attribute(String name);

    KMarker tag();

    HReference reference();
}
