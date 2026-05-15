package com.hanoi.model;

import java.util.UUID;

public record DiscoId(UUID value) {
    public DiscoId() {
        this(UUID.randomUUID());
    }

    public static DiscoId gerar() {
        return new DiscoId();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
