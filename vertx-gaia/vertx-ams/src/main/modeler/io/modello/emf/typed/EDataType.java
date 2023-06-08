package io.modello.emf.typed;

import io.modello.emf.NameEType;
import io.modello.emf.VEmf;
import io.modello.emf.atom.KXml;
import io.modello.emf.specification.EType;

import java.io.Serializable;

/**
 * 支持两种格式的核心定义
 * <pre><code>
 *     1. 原生定义：
 *        eType="ecore:EDataType http://www.eclipse.org/emf/2002/Ecore#//EString"
 *     2. 自定义（子类扩展和实现）：
 *        type="ecore:EDataType" instanceClassName="xxx"
 * </code></pre>
 *
 * @author lang : 2023-05-12
 */
public abstract class EDataType<T> implements EType<T>, Serializable {

    protected final NameEType dataType;
    protected final KXml xml;

    public EDataType(final NameEType dataType) {
        this.xml = KXml.of(VEmf.DATA_TYPE, dataType.uri(), VEmf.PREFIX.ECORE);
        this.dataType = dataType;
    }

    @Override
    public String name() {
        return this.xml.name();
    }

    @Override
    public String nsUri() {
        return this.xml.nsUri();
    }

    @Override
    public String nsPrefix() {
        return this.xml.nsPrefix();
    }
}
