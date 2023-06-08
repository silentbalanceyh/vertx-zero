package io.vertx.mod.rbac.acl.relation;

import cn.vertxup.rbac.domain.tables.pojos.SUser;
import io.horizon.eon.VString;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractBind<T> implements IdcBinder<T> {

    protected final String sigma;

    AbstractBind(final String sigma) {
        this.sigma = sigma;
    }

    protected Future<ConcurrentMap<String, List<T>>> mapAsync(final JsonArray inputData, final Class<?> daoCls, final String field) {
        // inputData 必须有数据
        return Ux.Jooq.on(daoCls).<T>fetchAsync(KName.SIGMA, this.sigma).compose(tList -> {
            // 根据用户输入数据执行分组
            final ConcurrentMap<String, String> vector = Ut.elementMap(tList, this.valueFn(), this.keyFn());
            /*
             * Calculated for name = A,B,C
             */
            final ConcurrentMap<String, List<T>> grouped = new ConcurrentHashMap<>();
            Ut.itJArray(inputData).forEach(user -> {
                final String literal = user.getString(field);
                if (Ut.isNotNil(literal)) {
                    final Set<String> validSet = Arrays.stream(literal.split(VString.COMMA))
                        .map(String::trim)
                        .filter(Ut::isNotNil)
                        .filter(vector::containsKey)
                        .collect(Collectors.toSet());
                    final String username = user.getString(KName.USERNAME);

                    grouped.put(username, tList.stream()
                        .filter(Objects::nonNull)
                        .filter(item -> validSet.contains(this.valueFn().apply(item)))
                        .collect(Collectors.toList())
                    );
                }
            });
            return Ux.future(grouped);
        });
    }

    protected Future<Boolean> purgeAsync(final List<SUser> users, final Class<?> daoCls, final String field) {
        final Set<String> userKeys = users.stream().map(SUser::getKey).collect(Collectors.toSet());
        final JsonObject condition = new JsonObject();
        /*
         * Remove old relation ship between ( role - user )
         */
        condition.put(field + ",i", Ut.toJArray(userKeys));
        return Ux.Jooq.on(daoCls).deleteByAsync(condition);
    }

    protected abstract Function<T, String> keyFn();

    protected abstract Function<T, String> valueFn();
}
