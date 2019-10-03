package org.axtin.container.facade;

import org.axtin.container.AxtinContainer;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;

public class Container {
    private static AxtinContainer container;

    public static <T> T get(Class<T> clazz) {
        return container.get(clazz);
    }

    public static <T> Optional<T> optional(Class<T> clazz) {
        return container.optional(clazz);
    }

    public static <T> void put(Class<T> clazz, T instance) {
        container.put(clazz, instance);
    }

    public static <T> void put(Class<T> clazz, Supplier<T> factory) {
        container.put(clazz, factory);
    }

    public static Collection<Object> getAll() {
        return container.getAll();
    }

    @SuppressWarnings({"unused", "unchecked"})
    private static <T> T cast(Object obj) {
        return (T) obj;
    }

    public static void swap(AxtinContainer instance) {
        container = instance;
    }
}
