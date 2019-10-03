package org.axtin.container;

import java.util.*;
import java.util.function.Supplier;

public class AxtinContainer {
    private final Map<Class<?>, Object> container;

    public AxtinContainer() {
        container = new HashMap<>();
    }

    public <T> T get(Class<T> clazz) {
        if (!container.containsKey(clazz)) {
            throw new IllegalArgumentException("No mapping was found for class " + clazz.getName());
        }

        Object obj = container.get(clazz);

        if (obj instanceof Supplier) {
            return cast(((Supplier<?>) obj).get());
        } else {
            return cast(obj);
        }
    }

    public <T> Optional<T> optional(Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class is null");
        }

        if (!container.containsKey(clazz)) {
            return Optional.empty();
        }

        Object obj = container.get(clazz);

        if (obj instanceof Supplier) {
            return Optional.ofNullable(cast(((Supplier<?>) obj).get()));
        } else {
            return Optional.ofNullable(cast(obj));
        }
    }

    public <T> void put(Class<T> clazz, T instance) {
        container.put(clazz, instance);
    }

    public <T> void put(Class<T> clazz, Supplier<T> factory) {
        container.put(clazz, factory);
    }

    public Collection<Object> getAll() {
        return Collections.unmodifiableCollection(container.values());
    }

    @SuppressWarnings("unchecked")
    private <T> T cast(Object obj) {
        return (T) obj;
    }

    public void clear() {
        container.clear();
    }
}
