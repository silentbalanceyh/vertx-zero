package io.aeon.experiment.mixture;

import io.aeon.experiment.mu.KReference;
import io.aeon.experiment.reference.RQuery;
import io.aeon.experiment.reference.RQuote;
import io.aeon.experiment.reference.RResult;

import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HReference {

    KReference refData(String name);

    ConcurrentMap<String, RQuote> refInput();

    ConcurrentMap<String, RQuery> refQr();

    ConcurrentMap<String, RResult> refOutput();
}
