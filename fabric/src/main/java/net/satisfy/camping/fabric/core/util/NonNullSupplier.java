package net.satisfy.camping.fabric.core.util;

@FunctionalInterface
public interface NonNullSupplier<T> {
    T get();
}
