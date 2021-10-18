/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.jet.domain;


import cn.vertxup.jet.domain.tables.IApi;
import cn.vertxup.jet.domain.tables.IJob;
import cn.vertxup.jet.domain.tables.IService;
import cn.vertxup.jet.domain.tables.records.IApiRecord;
import cn.vertxup.jet.domain.tables.records.IJobRecord;
import cn.vertxup.jet.domain.tables.records.IServiceRecord;

import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables in
 * DB_ETERNAL.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<IApiRecord> KEY_I_API_PRIMARY = Internal.createUniqueKey(IApi.I_API, DSL.name("KEY_I_API_PRIMARY"), new TableField[] { IApi.I_API.KEY }, true);
    public static final UniqueKey<IApiRecord> KEY_I_API_URI = Internal.createUniqueKey(IApi.I_API, DSL.name("KEY_I_API_URI"), new TableField[] { IApi.I_API.URI, IApi.I_API.METHOD, IApi.I_API.SIGMA }, true);
    public static final UniqueKey<IJobRecord> KEY_I_JOB_NAMESPACE = Internal.createUniqueKey(IJob.I_JOB, DSL.name("KEY_I_JOB_NAMESPACE"), new TableField[] { IJob.I_JOB.NAMESPACE, IJob.I_JOB.NAME }, true);
    public static final UniqueKey<IJobRecord> KEY_I_JOB_PRIMARY = Internal.createUniqueKey(IJob.I_JOB, DSL.name("KEY_I_JOB_PRIMARY"), new TableField[] { IJob.I_JOB.KEY }, true);
    public static final UniqueKey<IJobRecord> KEY_I_JOB_SIGMA = Internal.createUniqueKey(IJob.I_JOB, DSL.name("KEY_I_JOB_SIGMA"), new TableField[] { IJob.I_JOB.SIGMA, IJob.I_JOB.CODE }, true);
    public static final UniqueKey<IJobRecord> KEY_I_JOB_SIGMA_2 = Internal.createUniqueKey(IJob.I_JOB, DSL.name("KEY_I_JOB_SIGMA_2"), new TableField[] { IJob.I_JOB.SIGMA, IJob.I_JOB.NAME }, true);
    public static final UniqueKey<IServiceRecord> KEY_I_SERVICE_NAME = Internal.createUniqueKey(IService.I_SERVICE, DSL.name("KEY_I_SERVICE_NAME"), new TableField[] { IService.I_SERVICE.NAME, IService.I_SERVICE.NAMESPACE }, true);
    public static final UniqueKey<IServiceRecord> KEY_I_SERVICE_PRIMARY = Internal.createUniqueKey(IService.I_SERVICE, DSL.name("KEY_I_SERVICE_PRIMARY"), new TableField[] { IService.I_SERVICE.KEY }, true);
}
