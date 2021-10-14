package cn.originx.quiz;

import io.vertx.up.eon.em.Environment;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractMigration extends AbstractPlatform {
    public AbstractMigration(final Environment environment) {
        super(environment);
    }
}
