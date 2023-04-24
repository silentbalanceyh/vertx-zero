package io.horizon.function;

interface EConsumer<T, E extends Throwable> {
    void accept(T t) throws E;
}

interface ESupplier<T, E extends Throwable> {
    T get() throws E;
}

interface EActuator<E extends Throwable> {
    void execute() throws E;
}

interface EFunction<I, O, E extends Throwable> {
    O apply(I i) throws E;
}

interface EBiConsumer<L, R, E extends Throwable> {

    void accept(L input, R second) throws E;
}

