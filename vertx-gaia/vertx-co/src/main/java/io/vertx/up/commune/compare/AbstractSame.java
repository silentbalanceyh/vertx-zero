package io.vertx.up.commune.compare;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.element.JVs;
import io.vertx.up.util.Ut;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
abstract class AbstractSame implements VsSame {
    protected transient JVs subset;
    protected transient final Class<?> type;
    private transient VsExtension found;

    public AbstractSame(final Class<?> type) {
        this.type = type;
        final ServiceLoader<VsExtension> loader = ServiceLoader.load(VsExtension.class, VsExtension.class.getClassLoader());
        for (final VsExtension vsExtension : loader) {
            this.found = vsExtension;
            if (Objects.nonNull(this.found)) {
                break;
            }
        }
    }

    @Override
    public VsSame bind(final JVs subset) {
        if (Objects.nonNull(subset)) {
            this.subset = subset;
        }
        return this;
    }

    @Override
    public boolean is(final Object valueOld, final Object valueNew) {
        final boolean checked = this.isAnd(valueOld, valueNew);
        if (checked) {
            /*
             * Internal Checking Passed
             */
            return Boolean.TRUE;
        } else {
            /*
             * VsExtension Loading
             */
            if (Objects.isNull(this.found)) {
                return Boolean.FALSE;
            } else {
                /*
                 * VsExtension Found
                 */
                return this.found.is(valueOld, valueNew, this.type);
            }
        }
    }

    @Override
    public boolean ok(final Object value) {
        /*
         * Bug Fix:
         * 删除类的旧记录中必须包含非空的值
         * 过滤空的字符串，空字符串不纳入到新增历史中
         *
         * [NOT NULL]
         */
        return Objects.nonNull(value) && Ut.notNil(value.toString());
    }

    public boolean isAnd(final Object valueOld, final Object valueNew) {
        final boolean isSame = valueOld.equals(valueNew);
        if (isSame) {
            return Boolean.TRUE;
        } else {
            return valueOld.toString().equals(valueNew.toString());
        }
    }
}

interface Pool {
    ConcurrentMap<Class<?>, VsSame> POOL_SAME = new ConcurrentHashMap<Class<?>, VsSame>() {
        {
            this.put(String.class, new VsString());
            this.put(Integer.class, new VsInteger());
            this.put(Long.class, new VsLong());
            this.put(Boolean.class, new VsBoolean());
            this.put(BigDecimal.class, new VsBigDecimal());
            this.put(LocalTime.class, new VsLocalTime());
            this.put(LocalDate.class, new VsLocalDate());
            this.put(LocalDateTime.class, new VsLocalDateTime());
            this.put(Instant.class, new VsInstant());
            this.put(JsonObject.class, new VsJsonObject());
            this.put(JsonArray.class, new VsJsonArray());
        }
    };
}
