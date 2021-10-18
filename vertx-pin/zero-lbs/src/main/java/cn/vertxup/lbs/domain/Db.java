/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.lbs.domain;


import cn.vertxup.lbs.domain.tables.*;
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
     * The table <code>DB_ETERNAL.L_CITY</code>.
     */
    public final LCity L_CITY = LCity.L_CITY;

    /**
     * The table <code>DB_ETERNAL.L_COUNTRY</code>.
     */
    public final LCountry L_COUNTRY = LCountry.L_COUNTRY;

    /**
     * The table <code>DB_ETERNAL.L_FLOOR</code>.
     */
    public final LFloor L_FLOOR = LFloor.L_FLOOR;

    /**
     * The table <code>DB_ETERNAL.L_LOCATION</code>.
     */
    public final LLocation L_LOCATION = LLocation.L_LOCATION;

    /**
     * The table <code>DB_ETERNAL.L_REGION</code>.
     */
    public final LRegion L_REGION = LRegion.L_REGION;

    /**
     * The table <code>DB_ETERNAL.L_STATE</code>.
     */
    public final LState L_STATE = LState.L_STATE;

    /**
     * The table <code>DB_ETERNAL.L_TENT</code>.
     */
    public final LTent L_TENT = LTent.L_TENT;

    /**
     * The table <code>DB_ETERNAL.L_YARD</code>.
     */
    public final LYard L_YARD = LYard.L_YARD;

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
            LCity.L_CITY,
            LCountry.L_COUNTRY,
            LFloor.L_FLOOR,
            LLocation.L_LOCATION,
            LRegion.L_REGION,
            LState.L_STATE,
            LTent.L_TENT,
            LYard.L_YARD
        );
    }
}
