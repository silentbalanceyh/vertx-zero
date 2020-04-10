Old Api code of Ux segments:


    /*
    // -> IfElse true -> Future<T>, false -> Future<F>
    public static <T, F, R> Future<R> thenOtherwise(final Future<Boolean> condition, final Supplier<Future<T>> trueFuture, final Function<T, R> trueFun, final Supplier<Future<F>> falseFuture, final Function<F, R> falseFun) {
        return Fluctuate.thenOtherwise(condition, trueFuture, trueFun, falseFuture, falseFun);
    } */

    /*
    // -> IfOr true -> Future<T>, false -> Future<R>
    public static <T, R> Future<R> thenError(final Future<Boolean> condition, final Supplier<Future<T>> trueFuture, final Function<T, R> trueFun, final Class<? extends WebException> clazz, final Object... args) {
        return Fluctuate.thenOtherwise(condition, trueFuture, trueFun, clazz, args);
    }

    // -> IfOr true -> Future<JsonObject>, false -> Future<JsonObject>
    public static Future<JsonObject> thenError(final Future<Boolean> condition, final Supplier<Future<JsonObject>> trueFuture, final Class<? extends WebException> clazz, final Object... args) {
        return Fluctuate.thenOtherwise(condition, trueFuture, item -> item, clazz, args);
    }*/

    /*
    // -> If only true -> Future<T>
    public static <T, R> Future<R> thenTrue(final Future<Boolean> condition, final Supplier<Future<T>> trueFuture, final Function<T, R> trueFun) {
        return Fluctuate.thenOtherwise(condition, trueFuture, trueFun, null);
    } */

    // -> Merge multi Future<> to single one, one for all module.
    public static <F, S, T> Future<T> thenComposite(final Future<F> source, final BiFunction<F, List<S>, T> mergeFun, final Function<F, Future<S>>... functions) {
        return Fluctuate.thenComposite(source, mergeFun, functions);
    }

    public static <F, S, T> Future<T> thenComposite(final Future<F> source, final BiFunction<F, List<S>, T> mergeFun, final Supplier<Future<S>>... suppliers) {
        return Fluctuate.thenComposite(source, mergeFun, suppliers);
    }

    public static Future<JsonObject> thenParallelJson(final Future<JsonObject> source, final Function<JsonObject, List<Future>> generateFun, final BiConsumer<JsonObject, JsonObject>... operatorFun) {
        return Fluctuate.thenParallelJson(source, generateFun, operatorFun);
    }

    public static <F, S, T> Future<List<T>> thenParallel(final Future<List<F>> source, final Function<F, Future<S>> generateFun, final BiFunction<F, S, T> mergeFun) {
        return Fluctuate.thenParallel(source, generateFun, mergeFun);
    }

    public static Future<JsonObject> thenParallelArray(final Future<JsonArray>... sources) {
        return Fluctuate.thenParallelArray(sources);
    }

    public static Future<JsonObject> thenParallelJson(final Future<JsonObject>... sources) {
        return Fluctuate.thenParallelJson(sources);
    }
    /**
     * Scatter generate
     * Source ->
     * source1 -> Future<List<1>>
     * source2 -> Future<List<2>>
     * Fore each element mergage 1, List<2> -> 3
     *
     * @param source      JsonArray
     * @param generateFun JsonObject -> Future<JsonArray>
     * @param mergeFun    Each element: JsonObject + JsonArray -> JsonObject
     * @return JsonArray
     */
    public static Future<JsonArray> thenScatterJson(final Future<JsonArray> source, final Function<JsonObject, Future<JsonArray>> generateFun, final BiFunction<JsonObject, JsonArray, JsonObject> mergeFun) {
        return Fluctuate.thenScatterJson(source, generateFun, mergeFun);
    }

    public static <T, I> Future<Set<T>> thenSet(final List<I> data, final Function<I, Future<T>> fun) {
        return Fluctuate.thenSet(data, fun);
    }
    /**
     * Merge multi Future<> to single one, one for all module.
     * source ->
     * supplier1
     * supplier2
     * supplier3
     * .....
     * All suppliers will be executed after source, then return the final Future.
     *
     * @param mergeFun  How to merge source result and all supplier's results into final result:
     * @param source    Single Future --> F
     * @param suppliers Multi Futures --> List<S>
     * @param <F>       Type of source element
     * @param <S>       Type of supplier's list element
     * @param <T>       Type of return
     * @return Future<T> for final result.
     */

    public static <T> Future<JsonObject> thenJsonOne(final List<T> list, final String pojo) {
        return Future.succeededFuture(To.toUnique(new JsonArray(list), pojo));
    }

    public static <T> Future<JsonObject> thenJsonOne(final T entity, final String pojo) {
        return Future.succeededFuture(To.toJson(entity, pojo));
    }

    public static <T> Future<Envelop> thenMore(final List<T> list, final String pojo) {
        return Future.succeededFuture(Envelop.success(To.toArray(list, pojo)));
    }

    public static <T> Future<Envelop> thenOne(final T entity, final String pojo) {
        return Future.succeededFuture(Envelop.success(To.toJson(entity, pojo)));
    }

    public static <T> Future<JsonObject> thenUpsert(final T entity,
                                                    final Supplier<Future<JsonObject>> supplier) {
        return Async.toUpsertFuture(entity, "", supplier, null);
    }

    public static <T> Future<JsonObject> thenUpsert(final T entity,
                                                    final Supplier<Future<JsonObject>> supplier,
                                                    final Function<JsonObject, JsonObject> updateFun) {
        return Async.toUpsertFuture(entity, "", supplier, updateFun);
    }

    public static <T> Future<JsonObject> thenUpsert(final T entity, final String pojo,
                                                    final Supplier<Future<JsonObject>> supplier) {
        return Async.toUpsertFuture(entity, pojo, supplier, null);
    }

    public static <T> Future<JsonObject> thenUpsert(final T entity, final String pojo,
                                                    final Supplier<Future<JsonObject>> supplier,
                                                    final Function<JsonObject, JsonObject> updateFun) {
        return Async.toUpsertFuture(entity, pojo, supplier, updateFun);
    }

    public static <T> Future<JsonArray> thenJsonMore(final List<T> list) {
        return Future.succeededFuture(To.toArray(list, ""));
    }

    public static <T> Future<JsonArray> thenJsonMore(final List<T> list, final String pojo) {
        return Future.succeededFuture(To.toArray(list, pojo));
    }

    public static <T> Future<Envelop> then(final T entity) {
        return Future.succeededFuture(to(entity));
    }


    // -> Message<Envelop> -> String ( Security )
    @Deprecated
    public static String getUserID(final Envelop envelop, final String field) {
        return In.requestUser(envelop, field);
    }
    /*
    public static String getUserID(final Message<Envelop> message, final String field) {
        return In.requestUser(message, field);
    }

    // -> Message<Envelop> -> UUID ( Security )
    public static UUID getUserUUID(final Message<Envelop> message, final String field) {
        return UUID.fromString(getUserID(message, field));
    }

    public static UUID getUserUUID(final Envelop envelop, final String field) {
        return UUID.fromString(getUserID(envelop, field));
    }*/

    // -> Message<Envelop> -> Session ( Key )
    /*
    public static Object getSession(final Message<Envelop> message, final String field) {
        return In.requestSession(message, field);
    }

    public static Object getSession(final Envelop envelop, final String field) {
        return In.requestSession(envelop, field);
    }*/

    public static <T, R> Future<R> toFuture(final T entity, final Supplier<R> defaultFun, final Function<T, R> actualFun) {
        return null == entity ? Future.succeededFuture(defaultFun.get()) : Future.succeededFuture(actualFun.apply(entity));
    }

    // ---------------------- If/Else Future ----------------------------
    public static <T> Future<T> toFuture(final Supplier<Future<T>> caseLine) {
        return Fn.future(caseLine);
    }

    public static <T> Future<T> toFuture(final Actuator executor, final Supplier<Future<T>> caseLine) {
        return Fn.future(executor, caseLine);
    }

    // -> JsonArray -> Envelop ( To JsonObject, Result length must be 1 )
    public static Envelop toOne(final JsonArray array) {
        return Envelop.success(To.toUnique(array, ""));
    }

    // -> List<T> -> JsonObject ( Result length must be 1 )
    public static <T> JsonObject toUnique(final List<T> list) {
        return To.toUnique(Ux.toArray(list), "");
    }

    public static <T> JsonObject toUnique(final List<T> list, final String pojo) {
        return To.toUnique(Ux.toArray(list), pojo);
    }

    // -> JsonArray -> JsonObject ( Result length must be 1 )
    public static <T> JsonObject toUnique(final JsonArray array) {
        return To.toUnique(array, "");
    }

    // ---------------------- Web Error Returned --------------------------
    // -> WebException direct
    public static WebException toError(final Class<? extends WebException> clazz, final Object... args) {
        return To.toError(clazz, args);
    }

    public static void timer(final Class<?> clazz, final Actuator actuator) {
        Debug.timer(clazz, actuator);
    }

    public static <T> T timer(final Class<?> clazz, final Supplier<T> supplier) {
        return Debug.timer(clazz, supplier);
    }

    // -> Message<Envelop> -> JsonObject ( Interface mode )

    public static JsonObject getJson3(final Envelop envelop) {
        return In.request(envelop, 3, JsonObject.class);
    }

    public static JsonObject fromEnvelop3(final Envelop envelop, final String pojo) {
        return From.fromJson(Ux.getJson3(envelop), pojo);
    }

    public static <T> T fromEnvelop3(final Envelop envelop, final Class<T> clazz, final String pojo) {
        return From.fromJson(getJson3(envelop), clazz, pojo);
    }

    // -> Message<Envelop> -> Integer ( Interface mode )
    public static Integer getInteger3(final Envelop envelop) {
        return In.request(envelop, 3, Integer.class);
    }

    // -> Message<Envelop> -> Long ( Interface mode )
    public static Long getLong3(final Envelop envelop) {
        return In.request(envelop, 3, Long.class);
    }

    // -> Message<Envelop> -> T ( Interface mode )

    public static <T> T getT3(final Envelop envelop, final Class<T> clazz) {
        return In.request(envelop, 3, clazz);
    }

    // ---------------------- Request Data Ending --------------------------
        public static <E, T> Future<E> rxContainer(final Refer container, final E entity) {
            return Functions.fnSupplier(container, entity, null);
        }

        public static <E, T> Future<E> rxContainer(final Refer container, final E entity, final Supplier<T> supplier) {
            return Functions.fnSupplier(container, entity, supplier);
        }

        public static <E, T> Future<E> rxContainer(final Refer container, final E entity, final Consumer<T> consumer) {
            return Functions.fnConsumer(container, entity, consumer);
        }

        public static <E, T> Future<E> rxContainer(final Refer container, final E entity, final Function<T, E> function) {
            return Functions.fnConsumer(container, entity, item -> function.apply((T) item));
        }

        public static <E, T> Future<E> rxContainer(final Refer container, final E entity, final T target) {
            return Functions.fnSupplier(container, entity, () -> target);
        }

    // -> Message<Envelop> -> String ( Interface mode )
    public static String getString3(final Envelop envelop) {
        return In.request(envelop, 3, String.class);
    }