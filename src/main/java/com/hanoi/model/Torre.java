package com.hanoi.model;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Representa uma das três torres lógicas do jogo.
 * Encapsula o estado puro sem qualquer acoplamento com JavaFX ou rendering visual.
 */
public class Torre {
    private final int id;
    private final String nome;
    private final Stack<Disco> discos;

    public Torre(int id, String nome) {
        this.id = id;
        this.nome = nome;
        this.discos = new Stack<>();
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public List<Disco> getDiscos() {
        return Collections.unmodifiableList(discos);
    }

    public boolean estaVazia() {
        return discos.isEmpty();
    }

    public Disco espiarTopo() {
        if (estaVazia()) {
            return null;
        }
        return discos.peek();
    }

    public void adicionarDisco(Disco disco) {
        if (!estaVazia() && espiarTopo().tamanho() < disco.tamanho()) {
            throw new IllegalStateException("Regra violada: Discos maiores nunca podem ficar sobre menores.");
        }
        discos.push(disco);
    }

    public Disco removerDisco() {
        if (estaVazia()) {
            throw new IllegalStateException("A torre ja esta vazia.");
        }
        return discos.pop();
    }

    public int quantidadeDiscos() {
        return discos.size();
    }

    public void limpar() {
        this.discos.clear();
    }
}
