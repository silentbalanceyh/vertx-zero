package io.vertx.up.uca.job.plan;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class JobMonthly extends AbstractJobAt {

    @Override
    public String format() {
        return "'Days'=dd,'Time='HH:mm:ss.SSS";
    }
}
