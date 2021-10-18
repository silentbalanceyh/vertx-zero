/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.jet.domain;


import cn.vertxup.jet.domain.tables.IApi;
import cn.vertxup.jet.domain.tables.IJob;
import cn.vertxup.jet.domain.tables.IService;
import io.vertx.tp.ke.refine.Ke;
import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;

import java.util.Arrays;
import java.util.List;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class Db extends SchemaImpl {

    /**
     * The reference instance of <code>DB_ETERNAL</code>
     */
    public static final Db DB_ETERNAL = new Db();
    private static final long serialVersionUID = 1L;
    /**
     * The table <code>DB_ETERNAL.I_API</code>.
     */
    public final IApi I_API = IApi.I_API;

    /**
     * The table <code>DB_ETERNAL.I_JOB</code>.
     */
    public final IJob I_JOB = IJob.I_JOB;

    /**
     * The table <code>DB_ETERNAL.I_SERVICE</code>.
     */
    public final IService I_SERVICE = IService.I_SERVICE;

    /**
     * No further instances allowed
     */
    private Db() {
        super(Ke.getDatabase(), null);
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        return Arrays.asList(
            IApi.I_API,
            IJob.I_JOB,
            IService.I_SERVICE
        );
    }
}
