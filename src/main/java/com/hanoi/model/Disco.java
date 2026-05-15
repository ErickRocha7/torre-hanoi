package com.hanoi.model;

public record Disco(DiscoId id, int tamanho) {
    public Disco(int tamanho) {
        this(DiscoId.gerar(), tamanho);
    }

    public Disco {
        if (tamanho <= 0) {
            throw new IllegalArgumentException("O tamanho do disco deve ser maior que zero.");
        }
    }
}
