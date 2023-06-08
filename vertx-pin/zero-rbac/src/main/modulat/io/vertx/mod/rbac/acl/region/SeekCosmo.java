package io.vertx.mod.rbac.acl.region;

import io.horizon.eon.em.EmAop;
import io.horizon.uca.cache.Cc;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.acl.rapier.Quest;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class SeekCosmo implements Cosmo {
    private static final Cc<String, Cosmo> CC_COSMO = Cc.openThread();
    private static final Cc<String, Cosmo> CC_COSMO_EXTERNAL = Cc.openThread();

    /*
     *
     * Here are two critical concept for
     * 1) AclTime: When to fetch acl information from database ?
     *    This workflow is in `DataAcl.visitAcl` method.
     *      BEFORE: before(Envelop, matrix) will call BEFORE time here, it means fetch acl before do action on
     *        database and the acl could be picked up from database if it's ok
     *        When the parameters are match `syntax` such as provide all parameters, this workflow is
     *        ok because all the required condition will be matched.
     *      AFTER: after(Envelop, matrix) will call AFTER time here, it means fetch acl after do action on
     *        database and the acl could be picked up from database if it's ok
     *        When the parameters are often `key` only, after database that you could get all record here
     *
     * 2) RunPhase: How to use acl
     *      EAGER: The acl information should be used in current request
     *      DELAY: The acl information should be returned only for future usage in front and it does not
     *        impact current request
     */
    @Override
    public Future<Envelop> before(final Envelop request, final JsonObject matrix) {
        return this.runAop(request, matrix, EmAop.Effect.BEFORE, () -> Quest.syntax()
            /*
             * Before -> beforeAsync
             * 1）以条件判断是否读取资源访问者（带缓存访问）
             * 2）使用资源访问者构造Acl
             * 3）Acl用于 List 和 Form
             */
            .beforeAsync(request, matrix).compose(processed -> {

                /* Projection Modification */
                DataIn.visitProjection(processed, matrix);

                /* Criteria Modification */
                DataIn.visitCriteria(processed, matrix);

                return Ux.future(processed);
            })
        ).otherwise(Ux.otherwise());
    }

    @Override
    public Future<Envelop> after(final Envelop response, final JsonObject matrix) {
        return this.runAop(response, matrix, EmAop.Effect.AFTER, () -> Quest.syntax()
            /*
             * After -> afterAsync
             * 1）以条件判断是否读取资源访问者（带缓存访问）
             * 2）使用资源访问者构造Acl
             * 3）Acl用于 List 和 Form
             */
            .afterAsync(response, matrix).compose(processed -> {

                /* Projection */
                DataOut.dwarfRecord(processed, matrix);

                /* Rows */
                DataOut.dwarfRows(processed, matrix);

                /* Projection For Array */
                DataOut.dwarfCollection(processed, matrix);

                /* Infusion for */
                DataOut.dwarfAddon(processed, matrix);

                return Ux.future(processed);
            })
        ).otherwise(Ux.otherwise());
    }

    private Future<Envelop> runAop(final Envelop envelop, final JsonObject matrix, final EmAop.Effect phase,
                                   final Supplier<Future<Envelop>> executor) {
        /*
         * 检查 seeker:
         *    理论上，如果是Zero Extension内部调用，由于之前做了seeker检查（DataRegion中）
         *    if(matrix.containsKey("seeker")){
         *        SeekCosmo流程
         *    }
         *    但为了防止当前流程单独调用或配置，所以独立调用时，若 seeker 不存在，则直接走 CommonCosmo 流程
         */
        final JsonObject seeker = Ut.valueJObject(matrix, KName.SEEKER);
        if (Ut.isNil(seeker)) {
            final Cosmo common = CC_COSMO.pick(CommonCosmo::new);
            return (EmAop.Effect.BEFORE == phase) ? common.before(envelop, matrix) : common.after(envelop, matrix);
        }


        /*
         * 检查 component:
         *    如果定义了 component 则执行自定义流程而跳过 executor 的标准化流程
         */
        final Class<?> componentCls = Ut.valueCI(seeker, KName.COMPONENT, Cosmo.class);
        if (Objects.nonNull(componentCls)) {
            final Cosmo external = CC_COSMO_EXTERNAL.pick(() -> Ut.instance(componentCls));
            return (EmAop.Effect.BEFORE == phase) ? external.before(envelop, matrix) : external.after(envelop, matrix);
        }

        /*
         * 检查 syntax:
         *    如果定义了 seeker，则表示走核心流程，有两种情况不执行
         *    1) 如果 syntax 未定义则不执行
         *    2) 如果 syntax 中定义的 phase 和输入的 phase 不匹配也跳过不执行
         */
        final JsonObject syntax = Ut.valueJObject(seeker, KName.SYNTAX);
        if (Ut.isNil(syntax)) {
            // syntax 未定义，则自定义流程不执行
            return Ux.future(envelop);
        }
        final EmAop.Effect expected = Ut.toEnum(() -> syntax.getString(KName.PHASE), EmAop.Effect.class, EmAop.Effect.BEFORE);
        if (expected != phase) {
            // syntax 中定义的 phase 和预期不符合，不执行
            return Ux.future(envelop);
        }

        return executor.get();
    }
}
