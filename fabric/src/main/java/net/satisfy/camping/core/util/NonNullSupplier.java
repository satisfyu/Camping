package net.satisfy.camping.core.util;

@FunctionalInterface
public interface NonNullSupplier<T> {
    T get();
}
