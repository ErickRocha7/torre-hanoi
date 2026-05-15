package com.hanoi.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Jogo {
    private final List<Torre> torres;
    private final int quantidadeTotalDiscos;
    private int contadorMovimentos;

    public Jogo(int quantidadeTotalDiscos) {
        if (quantidadeTotalDiscos < 3 || quantidadeTotalDiscos > 8) {
            throw new IllegalArgumentException("Quantidade de discos permitida: entre 3 e 8.");
        }
        this.quantidadeTotalDiscos = quantidadeTotalDiscos;
        this.torres = new ArrayList<>();
        this.inicializarJogo();
    }

    private void inicializarJogo() {
        torres.clear();
        torres.add(new Torre(0, "Origem"));
        torres.add(new Torre(1, "Auxiliar"));
        torres.add(new Torre(2, "Destino"));

        Torre origem = torres.get(0);
        for (int i = quantidadeTotalDiscos; i > 0; i--) {
            origem.adicionarDisco(new Disco(i));
        }
        this.contadorMovimentos = 0;
    }

    public void resetar() {
        this.inicializarJogo();
    }

    public boolean validarEMover(int indiceOrigem, int indiceDestino) {
        if (indiceOrigem < 0 || indiceOrigem > 2 || indiceDestino < 0 || indiceDestino > 2) {
            return false;
        }

        Torre origem = torres.get(indiceOrigem);
        Torre destino = torres.get(indiceDestino);

        if (origem.estaVazia())
            return false;

        Disco discoMover = origem.espiarTopo();

        if (!destino.estaVazia() && destino.espiarTopo().tamanho() < discoMover.tamanho()) {
            return false;
        }

        origem.removerDiscoTopo();
        destino.adicionarDisco(discoMover);
        contadorMovimentos++;
        return true;
    }

    public int calcularMovimentosMinimos() {
        return (int) Math.pow(2, quantidadeTotalDiscos) - 1;
    }

    public boolean verificarCondConditionVitoria() {
        return torres.get(2).getQuantidadeDiscos() == quantidadeTotalDiscos;
    }

    public List<Torre> getTorres() {
        return Collections.unmodifiableList(torres);
    }

    public int getContadorMovimentos() {
        return contadorMovimentos;
    }

    public int getQuantidadeTotalDiscos() {
        return quantidadeTotalDiscos;
    }
}
