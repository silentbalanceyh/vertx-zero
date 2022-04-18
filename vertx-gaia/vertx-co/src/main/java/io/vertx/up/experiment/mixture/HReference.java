package io.vertx.up.experiment.mixture;

import io.vertx.up.experiment.reference.RQuery;
import io.vertx.up.experiment.reference.RQuote;
import io.vertx.up.experiment.reference.RResult;

import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HReference {

    ConcurrentMap<String, RQuote> refInput();

    ConcurrentMap<String, RQuery> refQr();

    ConcurrentMap<String, RResult> refOutput();
}
