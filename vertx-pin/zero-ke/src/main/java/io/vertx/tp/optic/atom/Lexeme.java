package io.vertx.tp.optic.atom;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.up.eon.Strings;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

/*
 * Definition for each channel here and parsed configuration.
 */
public class Lexeme implements Serializable {
    private static final Annal LOGGER = Annal.get(Lexeme.class);
    /* Default package for channel class here */
    private static final String DEFAULT_PACKAGE = "io.vertx.tp.optic";
    private static final String BUSINESS_PACKAGE = "io.vertx.tp.optic.business";
    /*
     * Parameters
     */
    private final transient List<String> params = new ArrayList<>();
    /*
     * Interface class definition
     */
    private transient Class<?> interfaceCls;
    /*
     * Implementation class definition
     */
    private transient Class<?> implCls;

    public Lexeme(final JsonObject input) {
        /* Interface class */
        this.parseInterface(input.getString("key"));
        /* Impl class */
        if (Objects.nonNull(this.interfaceCls)) {
            this.parseImpl(input.getString("value"),
                    this.interfaceCls.getPackage().getName());
        }
        /* Parameter names */
        this.parseParams();
        /* Valid channel building */
        Fn.safeSemi(this.isValid(), () -> Ke.infoKe(LOGGER, "Channel connect to: {0} -> {1}",
                this.interfaceCls.getName(), this.implCls.getName()));

    }

    private String parseClass(final String clsName, final String packageName) {
        if (clsName.contains(Strings.DOT)) {
            return clsName;
        } else {
            /*
             * Business package of fixed channel
             * When the class name start with `Ex` ( Extension )
             * It means that the class must use package name
             * `io.vertx.tp.optic.business` instead of widely
             * `io.vertx.tp.optic` here.
             * The interface must be in the same package
             */
            if (Ut.isNil(packageName)) {
                /*
                 * Interface workflow here
                 */
                if (clsName.startsWith("Ex")) {
                    return BUSINESS_PACKAGE + Strings.DOT + clsName;
                } else {
                    return DEFAULT_PACKAGE + Strings.DOT + clsName;
                }
            } else {
                /*
                 * Implementation class here
                 */
                return packageName + Strings.DOT + clsName;
            }
        }
    }

    private void parseInterface(final String name) {
        final String interfaceName = this.parseClass(name, null);
        final Class<?> interfaceCls = Ut.clazz(interfaceName);
        if (Objects.nonNull(interfaceCls)) {
            this.interfaceCls = interfaceCls;
        }
    }

    private void parseImpl(final String name, final String packageName) {
        final String implName = this.parseClass(name, packageName);
        final Class<?> implCls = Ut.clazz(implName, null);
        if (Objects.nonNull(implCls)) {
            this.implCls = implCls;
        } else {
            LOGGER.warn("The clazz is null, could not be found: {0}", implName);
        }
    }

    private void parseParams() {
        /* Interface definition */
        final Field[] constants = this.interfaceCls.getDeclaredFields();
        final Set<String> fieldSet = new TreeSet<>();
        Arrays.stream(constants).map(Field::getName)
                .filter(item -> item.startsWith("ARG"))
                .forEach(fieldSet::add);
        fieldSet.forEach(field -> {
            /* Sequence should be here define */
            final Object value = Ut.field(this.interfaceCls, field);
            this.params.add((String) value);
        });
    }

    public Class<?> getInterfaceCls() {
        return this.interfaceCls;
    }

    public Class<?> getImplCls() {
        return this.implCls;
    }

    public boolean isValid() {
        if (Objects.nonNull(this.interfaceCls) && Objects.nonNull(this.implCls)) {
            /* Check default and check valid */
            return Ut.isImplement(this.implCls, this.interfaceCls);
        } else {
            /* Null Checking */
            return false;
        }
    }

    public List<String> params() {
        return this.params;
    }
}
