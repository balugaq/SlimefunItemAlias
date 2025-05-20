package com.balugaq.sia.injectors;

import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

public class Rollback {
    public static final Map<String, Runnable> ROLLBACKS = new HashMap<>();

    @SneakyThrows
    public static void rollback(String id) {
        Runnable rollback = ROLLBACKS.get(id);
        if (rollback != null) {
            rollback.run();
        }
    }

    public static void addRollback(String id, Runnable rollback) {
        ROLLBACKS.merge(id, rollback, (a, b) -> () -> {
            a.run();
            b.run();
        });
    }
}
