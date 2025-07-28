package net.satisfy.camping.fabric.core.util;

@FunctionalInterface
public interface NonNullFunction<T, R> {
    R apply(T var1);
}
