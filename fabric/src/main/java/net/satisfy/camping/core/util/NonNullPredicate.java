package net.satisfy.camping.core.util;

@FunctionalInterface
public interface NonNullPredicate<T> {
    boolean test(T var1);
}
