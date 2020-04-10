package io.vertx.quiz;

import io.vertx.up.runtime.ZeroPack;

import java.util.HashSet;
import java.util.Set;

public class ScanBase {

    private final transient Set<Class<?>> classes;

    public ScanBase() {
        this.classes = ZeroPack.getClasses();
    }

    protected Set<Class<?>> getClasses() {
        return new HashSet<>(this.classes);
    }
}
