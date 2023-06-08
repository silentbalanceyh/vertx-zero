package io.vertx.mod.battery.uca.dock;

import cn.vertxup.battery.domain.tables.pojos.BBag;
import cn.vertxup.battery.domain.tables.pojos.BBlock;
import cn.vertxup.battery.service.BagArgService;
import cn.vertxup.battery.service.BagArgStub;
import io.horizon.eon.VValue;
import io.modello.eon.em.EmModel;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.battery.uca.configure.Combiner;
import io.vertx.mod.ke.cv.em.TypeBag;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractArk implements Ark {
    protected JsonObject buildQr(final String input, final TypeBag typeBag) {
        return this.buildQr(input, Set.of(typeBag), EmModel.By.BY_ID);
    }

    protected JsonObject buildQr(final String input, final TypeBag typeBag, final EmModel.By by) {
        return this.buildQr(input, Set.of(typeBag), by);
    }


    protected JsonObject buildQr(final String input, final Set<TypeBag> bags) {
        return this.buildQr(input, bags, EmModel.By.BY_ID);
    }

    protected JsonObject buildQr(final String input, final Set<TypeBag> bags, final EmModel.By by) {
        final JsonObject conditionJ = Ux.whereAnd();
        switch (by) {
            case BY_KEY -> conditionJ.put(KName.APP_KEY, input);
            case BY_SIGMA -> conditionJ.put(KName.SIGMA, input);
            case BY_TENANT -> conditionJ.put(KName.Tenant.ID, input);
            default -> conditionJ.put(KName.APP_ID, input);
        }
        if (VValue.ONE == bags.size()) {
            final TypeBag bag = bags.iterator().next();
            conditionJ.put(KName.TYPE, bag.key());
        } else {
            final Set<String> bagNames = bags.stream()
                .map(TypeBag::key).collect(Collectors.toSet());
            // type,i = [bag1, bag2, bag3]
            conditionJ.put(KName.TYPE + ",i", Ut.toJArray(bagNames));
        }
        return conditionJ;
    }

    protected Future<JsonObject> configureBag(final BBag bag) {
        final BagArgStub stub = Ut.singleton(BagArgService.class);
        return stub.seekBlocks(bag).compose(blocks -> {
            final Combiner<BBag, List<BBlock>> combiner = Combiner.forBlock();
            return combiner.configure(bag, blocks);
        });
    }
}
