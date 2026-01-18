package dev.mvrk.blog.utils;

import java.util.function.Consumer;

public class PatchUtils {
    public static <T> void updateIfPresent(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
