package io.vertx.tp.atom.modeling.data;

import cn.vertxup.atom.domain.tables.pojos.MAttribute;
import io.vertx.tp.atom.modeling.Model;
import io.vertx.up.util.Ut;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 * 模型基本信息
 */
class MetaInfo {

    private static final Set<Class<?>> TYPE_DATE = new HashSet<Class<?>>() {
        {
            this.add(LocalDateTime.class);
            this.add(LocalDate.class);
            this.add(LocalTime.class);
            this.add(Date.class);
        }
    };
    private transient final Model modelRef;
    private transient final String identifier;

    MetaInfo(final Model modelRef) {
        /* 模型引用信息 */
        this.modelRef = modelRef;
        /* 直接从模型中读取 identifier */
        this.identifier = modelRef.identifier();
    }

    /* 读取底层存储的模型信息 */
    Model reference() {
        return this.modelRef;
    }

    /* 模型标识符 */
    String identifier() {
        return this.identifier;
    }

    /* 返回统一标识符 */
    String sigma() {
        return this.reference().getModel().getSigma();
    }

    /* 读取模型中的属性信息 */
    Set<String> attributes() {
        return this.modelRef.getAttributes().stream()
                .map(MAttribute::getName)
                .filter(Ut::notNil)
                .collect(Collectors.toSet());
    }

    // ------------------ 计算型处理 -----------------

    ConcurrentMap<String, Class<?>> type() {
        return this.modelRef.types();
    }

    Class<?> type(final String field) {
        return this.modelRef.types().getOrDefault(field, null);
    }

    boolean isDateType(final String field) {
        return this.typeDate().containsKey(field);
    }

    private ConcurrentMap<String, Class<?>> typeDate() {
        final ConcurrentMap<String, Class<?>> typeMap = new ConcurrentHashMap<>();
        this.type().forEach((field, type) -> {
            if (TYPE_DATE.contains(type)) {
                typeMap.put(field, type);
            }
        });
        return typeMap;
    }
}
