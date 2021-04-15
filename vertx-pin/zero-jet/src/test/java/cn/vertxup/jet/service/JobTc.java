package cn.vertxup.jet.service;

import cn.vertxup.jet.domain.tables.daos.IJobDao;
import io.vertx.ext.unit.TestContext;
import io.vertx.quiz.JooqBase;
import io.vertx.tp.plugin.jooq.JooqInfix;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@Ignore
public class JobTc extends JooqBase {
    static {
        JooqInfix.init(VERTX);
    }

    @Test
    public void testJooq(final TestContext context) {
        this.async(context, this.getDao().countByAsync(null, "group"), actual -> {

        });
    }

    @Override
    public UxJooq getDao() {
        return Ux.Jooq.on(IJobDao.class);
    }
}
