package io.vertx.up.uca.job.plan;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class JobYearly extends AbstractJobAt {

    @Override
    public String format() {
        return "'Years'=yyyy,'Months'=MM,'Days'=dd,'Time='HH:mm:ss.SSS";
    }
}
