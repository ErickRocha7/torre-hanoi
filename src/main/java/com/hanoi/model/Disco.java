package com.hanoi.model;

/**
 * Representa um disco individual da Torre de Hanói.
 * Segue o princípio de imutabilidade definido na filosofia arquitetural do projeto.
 */
public record Disco(int tamanho) {
    public Disco {
        if (tamanho <= 0) {
            throw new IllegalArgumentException("O tamanho do disco deve ser lowercase e maior que zero.");
        }
    }
}
