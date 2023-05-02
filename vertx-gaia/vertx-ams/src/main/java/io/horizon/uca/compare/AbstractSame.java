package io.horizon.uca.compare;

import io.horizon.atom.modeler.MetaField;
import io.horizon.spi.typed.VsExtension;
import io.horizon.util.HaS;

import java.util.Objects;
import java.util.ServiceLoader;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
abstract class AbstractSame implements VsSame {
    protected transient final Class<?> type;
    protected transient MetaField fieldType;
    private transient VsExtension found;

    public AbstractSame(final Class<?> type) {
        this.type = type;
        final ServiceLoader<VsExtension> loader = ServiceLoader.load(VsExtension.class, VsExtension.class.getClassLoader());
        for (final VsExtension TEquation : loader) {
            this.found = TEquation;
            if (Objects.nonNull(this.found)) {
                break;
            }
        }
    }

    public VsSame bind(final MetaField fieldType) {
        if (Objects.nonNull(fieldType)) {
            this.fieldType = fieldType;
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
        return Objects.nonNull(value) && HaS.isNotNil(value.toString());
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
