package net.satisfy.camping.fabric.core.util;

@FunctionalInterface
public interface NonNullPredicate<T> {
    boolean test(T var1);
}
