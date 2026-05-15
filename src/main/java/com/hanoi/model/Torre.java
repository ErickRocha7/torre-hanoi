package com.hanoi.model;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Torre {
    private final int id;
    private final String nome;
    private final Stack<Disco> discos;

    public Torre(int id, String nome) {
        this.id = id;
        this.nome = nome;
        this.discos = new Stack<>();
    }

    public void adicionarDisco(Disco disco) {
        discos.push(disco);
    }

    public Disco removerDiscoTopo() {
        return discos.pop();
    }

    public Disco espiarTopo() {
        return discos.peek();
    }

    public boolean estaVazia() {
        return discos.isEmpty();
    }

    public int getQuantidadeDiscos() {
        return discos.size();
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
}
