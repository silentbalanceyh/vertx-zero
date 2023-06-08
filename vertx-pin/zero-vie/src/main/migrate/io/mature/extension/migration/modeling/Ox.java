package io.mature.extension.migration.modeling;

import cn.vertxup.atom.domain.tables.daos.MAttributeDao;
import cn.vertxup.atom.domain.tables.daos.MEntityDao;
import cn.vertxup.atom.domain.tables.daos.MFieldDao;
import cn.vertxup.atom.domain.tables.daos.MModelDao;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Pool {
    ConcurrentMap<Class<?>, Revision> POOL = new ConcurrentHashMap<Class<?>, Revision>() {
        {
            this.put(MModelDao.class, Ut.instance(ModelRevision.class));
            this.put(MEntityDao.class, Ut.instance(EntityRevision.class));
            this.put(MFieldDao.class, Ut.instance(FieldRevision.class));
            this.put(MAttributeDao.class, Ut.instance(AttributeRevision.class));
        }
    };
}
