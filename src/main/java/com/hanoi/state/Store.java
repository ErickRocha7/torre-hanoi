package com.hanoi.state;

import com.hanoi.config.LayoutConfig;
import com.hanoi.config.Tema;
import com.hanoi.model.Jogo;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Store {
    private final Jogo jogo;
    private final LayoutConfig layoutConfig;
    private UIState estadoAtual;
    private final List<Consumer<UIState>> assinantes = new CopyOnWriteArrayList<>();

    private Integer torreSelecionadaId = null;
    private Tema temaAtual;

    public Store(int discosIniciais, Tema temaInicial, LayoutConfig layoutConfig) {
        this.jogo = new Jogo(discosIniciais);
        this.layoutConfig = layoutConfig;
        this.temaAtual = temaInicial;
        this.estadoAtual = UIStateDeriver.derivar(jogo, null, temaAtual, layoutConfig);
    }

    public void inscrever(Consumer<UIState> listener) {
        this.assinantes.add(listener);
        listener.accept(estadoAtual);
    }

    public UIState getEstado() {
        return estadoAtual;
    }

    public synchronized void despachar(UIEvent evento) {
        if (evento instanceof UIEvent.InteragirComTorre ev) {
            processarInteracaoTorre(ev.torreId());
        } else if (evento instanceof UIEvent.MudarTema ev) {
            this.temaAtual = ev.novoTema();
        } else if (evento instanceof UIEvent.ResetarJogo) {
            this.torreSelecionadaId = null;
            this.jogo.resetar();
        }

        this.estadoAtual = UIStateDeriver.derivar(jogo, torreSelecionadaId, temaAtual, layoutConfig);
        assinantes.forEach(a -> a.accept(estadoAtual));
    }

    private void processarInteracaoTorre(int torreId) {
        if (torreSelecionadaId == null) {
            if (!jogo.getTorres().get(torreId).estaVazia()) {
                this.torreSelecionadaId = torreId;
            }
        } else {
            int origem = torreSelecionadaId;
            this.torreSelecionadaId = null;
            jogo.validarEMover(origem, torreId);
        }
    }
}
