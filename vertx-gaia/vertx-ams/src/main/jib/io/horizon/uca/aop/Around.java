package io.horizon.uca.aop;

import io.horizon.eon.em.typed.ChangeFlag;

import java.util.Set;

/**
 * Validation for plug-in api, you can configure the validation
 * component for api validation in zero-crud module instead of `codex` ( Development )
 * 1. It's only for crud part instead of `Before/After`
 * 2. It will be called
 * -- Before Actor in zero-crud
 * -- After Actor in zero-crud
 * Before component for
 * -- 1. Validation
 * -- 2. Filter
 * -- 3. Transform
 * After component for
 * -- 1. Start new Job
 * -- 2. Callback
 * -- 3. Notification
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Around extends Before, After {

    @Override
    Set<ChangeFlag> types();
}
