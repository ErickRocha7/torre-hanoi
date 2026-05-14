package com.hanoi.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Orquestrador das regras funcionais e do estado lógico do jogo.
 * Centraliza a validação matemática e gerencia as três torres bases.
 */
public class Jogo {
    private final List<Torre> torres;
    private final int quantidadeTotalDiscos;
    private int contadorMovimentos;

    public Jogo(int quantidadeTotalDiscos) {
        if (quantidadeTotalDiscos < 3 || quantidadeTotalDiscos > 8) {
            throw new IllegalArgumentException("Quantidade de discos permitida para o MVP: entre 3 e 8.");
        }
        this.quantidadeTotalDiscos = quantidadeTotalDiscos;
        this.torres = new ArrayList<>();
        this.contadorMovimentos = 0;
        inicializarJogo();
    }

    private void inicializarJogo() {
        torres.add(new Torre(0, "torre origem"));
        torres.add(new Torre(1, "torre auxiliar"));
        torres.add(new Torre(2, "torre destino"));

        // Preenche a torre de origem do maior para o menor disco
        for (int i = quantidadeTotalDiscos; i > 0; i--) {
            torres.get(0).adicionarDisco(new Disco(i));
        }
    }

    public boolean validarEMover(int idOrigem, int idDestino) {
        if (idOrigem < 0 || idOrigem > 2 || idDestino < 0 || idDestino > 2) {
            return false;
        }
        if (idOrigem == idDestino) {
            return false;
        }

        Torre origem = torres.get(idOrigem);
        Torre destino = torres.get(idDestino);

        if (origem.estaVazia()) {
            return false; // Apenas o disco do topo pode ser movido
        }

        Disco discoParaMover = origem.espiarTopo();
        Disco discoDestino = destino.espiarTopo();

        if (discoDestino != null && discoDestino.tamanho() < discoParaMover.tamanho()) {
            return false; // Discos maiores nunca sobre menores
        }

        // Executa o movimento lógico puro
        destino.adicionarDisco(origem.removerDisco());
        contadorMovimentos++;
        return true;
    }

    public boolean verificarCondicaoVitoria() {
        // Vitória: Todos os discos na torre de destino (id: 2)
        return torres.get(2).quantidadeDiscos() == quantidadeTotalDiscos;
    }

    public int calcularMovimentosMinimos() {
        // Formula oficial do documento de requisitos: M = 2^n - 1
        return (int) Math.pow(2, quantidadeTotalDiscos) - 1;
    }

    public int getContadorMovimentos() {
        return contadorMovimentos;
    }

    public List<Torre> getTorres() {
        return torres;
    }

    public int getQuantidadeTotalDiscos() {
        return quantidadeTotalDiscos;
    }

    public void resetar() {
        this.contadorMovimentos = 0;
        for (Torre torre : torres) {
            torre.limpar();
        }
        for (int i = quantidadeTotalDiscos; i > 0; i--) {
            torres.get(0).adicionarDisco(new Disco(i));
        }
    }
}
