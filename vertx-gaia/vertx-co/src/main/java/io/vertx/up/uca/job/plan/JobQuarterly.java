package io.vertx.up.uca.job.plan;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class JobQuarterly extends AbstractJobAt {

    @Override
    public String format() {
        return "'Months'=MM,'Days'=dd,'Time='HH:mm:ss.SSS";
    }
}
