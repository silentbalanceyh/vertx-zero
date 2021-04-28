package io.vertx.tp.modular.plugin;

import cn.vertxup.atom.domain.tables.pojos.MAttribute;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.Model;
import io.vertx.tp.atom.modeling.element.DataTpl;
import io.vertx.up.commune.element.JComponent;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * ## Manager of ...
 *
 * This class will parse DataTpl for standard workflow
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class IoArranger {
    private static final String PLUGIN_CONFIG = "plugin.config";

    /**
     * Extract `IComponent` here.
     *
     * @param tpl {@link DataTpl}
     *
     * @return {@link java.util.concurrent.ConcurrentMap} The JComponent map for each field.
     */
    static ConcurrentMap<String, JComponent> pluginIn(final DataTpl tpl) {
        return extractPlugin(tpl, MAttribute::getInComponent, IComponent.class);
    }

    /**
     * Extract `OComponent` here.
     *
     * @param tpl {@link DataTpl}
     *
     * @return {@link java.util.concurrent.ConcurrentMap} The JComponent map for each field.
     */
    static ConcurrentMap<String, JComponent> pluginOut(final DataTpl tpl) {
        return extractPlugin(tpl, MAttribute::getOutComponent, OComponent.class);
    }

    /**
     * Extract `INormalizer` here.
     *
     * @param tpl {@link DataTpl}
     *
     * @return {@link java.util.concurrent.ConcurrentMap} The JComponent map for each field.
     */
    static ConcurrentMap<String, JComponent> pluginNormalize(final DataTpl tpl) {
        return extractPlugin(tpl, MAttribute::getNormalize, INormalizer.class);
    }

    /**
     * Extract `OExpression` here.
     *
     * @param tpl {@link DataTpl}
     *
     * @return {@link java.util.concurrent.ConcurrentMap} The JComponent map for each field.
     */
    static ConcurrentMap<String, JComponent> pluginExpression(final DataTpl tpl) {
        return extractPlugin(tpl, MAttribute::getExpression, OExpression.class);
    }


    /**
     * @param tpl          {@link DataTpl} The model definition template in data event
     * @param fnComponent  {@link java.util.function.Function} The component name extract from.
     * @param interfaceCls {@link java.lang.Class} required interface class
     *
     * @return {@link java.util.concurrent.ConcurrentMap} The JComponent map for each field.
     */
    private static ConcurrentMap<String, JComponent> extractPlugin(
            final DataTpl tpl, final Function<MAttribute, String> fnComponent, final Class<?> interfaceCls) {
        /*
         * 1. Iterate tpl attributes.
         */
        final Model model = tpl.atom().getModel();
        final ConcurrentMap<String, JComponent> pluginMap = new ConcurrentHashMap<>();
        model.getAttributes().forEach(attribute -> {
            /*
             * 2. Attribute
             */
            final String componentName = fnComponent.apply(attribute);
            final Class<?> componentCls = Ut.clazz(componentName, null);
            if (Objects.nonNull(componentCls)) {
                /*
                 * 3. SourceConfig
                 */
                final JComponent component = new JComponent(attribute.getName(), componentCls);
                if (component.valid(interfaceCls)) {
                    final JsonObject config = componentConfig(attribute, componentCls);
                    pluginMap.put(attribute.getName(), component.bind(config));
                }
            }
        });
        return pluginMap;
    }

    /**
     * Extract component configuration from attribute `sourceConfig` instead of other place.
     *
     * 1. sourceConfig stored component configuration.
     * 2. The json configuration came from `field = componentCls`.
     *
     * @param attribute    {@link MAttribute} Processed attribute that are related to `M_ATTRIBUTE`
     * @param componentCls {@link java.lang.Class} The component class of type
     *
     * @return {@link JsonObject} The extracted configuration of current component.
     */
    private static JsonObject componentConfig(final MAttribute attribute, final Class<?> componentCls) {
        final JsonObject sourceConfig = Ut.toJObject(attribute.getSourceConfig());
        if (sourceConfig.containsKey(PLUGIN_CONFIG)) {
            final JsonObject pluginConfig = Ut.toJObject(sourceConfig.getValue(PLUGIN_CONFIG));
            if (pluginConfig.containsKey(componentCls.getName())) {
                return Ut.toJObject(pluginConfig.getValue(componentCls.getName()));
            } else return new JsonObject();
        } else {
            return new JsonObject();
        }
    }
}
