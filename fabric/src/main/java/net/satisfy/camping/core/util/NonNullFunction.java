package net.satisfy.camping.core.util;

@FunctionalInterface
public interface NonNullFunction<T, R> {
    R apply(T var1);
}
