package io.vertx.up.uca.di;

import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.Plugins;
import io.vertx.up.exception.zero.InjectionLimeKeyException;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.plugin.Infix;
import io.vertx.up.runtime.ZeroAmbient;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.uca.yaml.ZeroUniform;
import io.vertx.up.util.Ut;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Set;

class DiAnchor {

    private transient final Class<?> clazz;
    private transient final Annal logger;

    DiAnchor(final Class<?> clazz) {
        this.clazz = clazz;
        this.logger = Annal.get(clazz);
    }

    private Class<? extends Annotation> search(
            final Field field
    ) {
        final Annotation[] annotations = field.getDeclaredAnnotations();
        final Set<Class<? extends Annotation>> annotationCls = Plugins.INFIX_MAP.keySet();
        Class<? extends Annotation> hitted = null;
        for (final Annotation annotation : annotations) {
            if (annotationCls.contains(annotation.annotationType())) {
                hitted = annotation.annotationType();
                break;
            }
        }
        return hitted;
    }

    Object initialize(final Field field) {
        final Class<? extends Annotation> key = this.search(field);
        final String pluginKey = Plugins.INFIX_MAP.get(key);
        final Class<?> infixCls = ZeroAmbient.getPlugin(pluginKey);
        Object ret = null;
        if (null != infixCls) {
            if (Ut.isImplement(infixCls, Infix.class)) {
                // Config checking
                final Node<JsonObject> node = Ut.instance(ZeroUniform.class);
                final JsonObject options = node.read();

                Fn.outUp(!options.containsKey(pluginKey), this.logger,
                        InjectionLimeKeyException.class,
                        this.clazz, infixCls, pluginKey);

                final Infix reference = Ut.singleton(infixCls);

                ret = Ut.invoke(reference, "get");
            } else {
                this.logger.warn(Info.INFIX_IMPL, infixCls.getName(), Infix.class.getName());
            }
        } else {
            this.logger.warn(Info.INFIX_NULL, pluginKey, field.getName(), field.getDeclaringClass().getName());
        }
        return ret;
    }
}
