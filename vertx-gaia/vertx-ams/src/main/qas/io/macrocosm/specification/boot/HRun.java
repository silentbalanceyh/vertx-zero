package io.macrocosm.specification.boot;

import io.horizon.specification.typed.TCommand;
import io.macrocosm.specification.config.HConfig;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HRun<T extends HConfig> extends TCommand<T, Boolean> {
}
