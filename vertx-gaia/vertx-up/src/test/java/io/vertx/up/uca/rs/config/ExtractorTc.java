package io.vertx.up.uca.rs.config;

import io.vertx.core.DeploymentOptions;
import io.vertx.ext.unit.TestContext;
import io.vertx.quiz.UpBase;
import io.vertx.quiz.example.User;
import io.vertx.up.eon.Constants;
import io.vertx.up.uca.rs.Extractor;
import io.vertx.up.util.Ut;
import io.vertx.up.verticle.ZeroHttpAgent;
import io.vertx.zero.exception.EventSourceException;
import org.junit.Test;

public class ExtractorTc extends UpBase {

    @Test
    public void testExtractAgent(final TestContext context) {
        final Extractor<DeploymentOptions> extractor =
            Ut.singleton(AgentExtractor.class);
        final DeploymentOptions options =
            extractor.extract(ZeroHttpAgent.class);
        // context.assertEquals(Constants.DEFAULT_GROUP, options.getIsolationGroup());
        context.assertEquals(Constants.DEFAULT_HA, options.isHa());
        context.assertEquals(Constants.DEFAULT_INSTANCES, options.getInstances());
    }

    @Test(expected = EventSourceException.class)
    public void testExtractEndpoint() {
        this.extractor().extract(this.getClass());
    }

    @Test
    public void testEvent1() {
        this.extractor().extract(User.class);
    }
}
