package com.hanoi.view;

import com.hanoi.config.LayoutConfig;
import com.hanoi.config.Tema;
import com.hanoi.model.Disco;
import com.hanoi.model.DiscoId;
import com.hanoi.state.Store;
import com.hanoi.state.UIEvent;
import com.hanoi.state.UIState;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

public class Renderer {
    private final Pane root;
    private final Store store;
    private final LayoutConfig cfg;
    private final RenderScheduler scheduler;

    // Cache de nós gráficos mapeados por identidade forte para evitar
    // re-renderização agressiva
    private final Map<DiscoId, Rectangle> nosDiscos = new HashMap<>();
    private final Group grupoPinos = new Group();
    private final Group grupoDiscos = new Group();
    private final Group grupoInterface = new Group();

    private Label labelStatus;
    private Button botaoReset;

    public Renderer(Pane root, Store store, LayoutConfig cfg, RenderScheduler scheduler) {
        this.root = root;
        this.store = store;
        this.cfg = cfg;
        this.scheduler = scheduler;

        this.root.getChildren().addAll(grupoPinos, grupoDiscos, grupoInterface);
        this.inicializarEstruturaEstatica();
    }

    private void inicializarEstruturaEstatica() {
        double setorLargura = cfg.boardWidth() / 3.0;

        // Construção geométrica reativa dos 3 pinos base
        for (int i = 0; i < 3; i++) {
            double centroX = (i * setorLargura) + (setorLargura / 2.0);

            Rectangle pino = new Rectangle(cfg.pinoWidth(), cfg.pinoHeight());
            pino.setX(centroX - (cfg.pinoWidth() / 2.0));
            pino.setY(cfg.pinoY());
            pino.setArcWidth(8);
            pino.setArcHeight(8);

            // Região invisível ampliada para facilitar a captura de cliques do mouse
            Rectangle regiaoClique = new Rectangle(setorLargura, cfg.boardHeight());
            regiaoClique.setX(i * setorLargura);
            regiaoClique.setY(0);
            regiaoClique.setFill(Color.TRANSPARENT);

            final int torreId = i;
            regiaoClique.setOnMouseClicked(e -> store.despachar(new UIEvent.InteragirComTorre(torreId)));

            grupoPinos.getChildren().addAll(pino, regiaoClique);
        }

        // Elementos textuais e funcionais superiores
        labelStatus = new Label();
        labelStatus.setStyle("-fx-font-family: 'monospace'; -fx-font-size: 16px; -fx-font-weight: bold;");
        labelStatus.setLayoutX(40);
        labelStatus.setLayoutY(30);

        botaoReset = new Button("Resetar Universo");
        botaoReset.setStyle("-fx-font-family: 'monospace'; -fx-font-weight: bold; -fx-padding: 8 16;");
        botaoReset.setLayoutX(cfg.boardWidth() - 200);
        botaoReset.setLayoutY(25);
        botaoReset.setOnMouseClicked(e -> store.despachar(new UIEvent.ResetarJogo()));

        grupoInterface.getChildren().addAll(labelStatus, botaoReset);
    }

    public void renderizar(UIState estado) {
        Tema tema = estado.tema();

        // Sincronização cromática baseada na paleta injetada
        root.setStyle("-fx-background-color: " + tema.getBackground() + ";");
        labelStatus.setTextFill(Color.web(tema.getTexto()));
        labelStatus.setText(String.format("Movimentos: %d / Mínimo Ideal: %d",
                estado.movimentosAtuais(), estado.movimentosMinimos()));

        if (estado.vitoria()) {
            labelStatus.setText("🎉 VITÓRIA ABSOLUTA! Sistema ordenado com sucesso.");
            labelStatus.setTextFill(Color.web(tema.getVitoria()));
        }

        // Atualização de cores dos pinos lógicos com base na seleção corrente
        double setorLargura = cfg.boardWidth() / 3.0;
        for (int i = 0; i < 3; i++) {
            // O pino físico é o primeiro elemento inserido em pares no loop estático
            Rectangle pinoVisual = (Rectangle) grupoPinos.getChildren().get(i * 2);

            if (estado.torreSelecionadaId() != null && estado.torreSelecionadaId() == i) {
                pinoVisual.setFill(Color.web(tema.getSelecao()));
            } else {
                pinoVisual.setFill(switch (i) {
                    case 0 -> Color.web(tema.getPinoOrigem());
                    case 1 -> Color.web(tema.getPinoAuxiliar());
                    default -> Color.web(tema.getPinoDestino());
                });
            }
        }

        // Renderização e interpolação posicional dos discos ativos
        for (int i = 0; i < estado.torres().size(); i++) {
            for (Disco disco : estado.torres().get(i)) {
                UIState.EspaçoCartesiano posAlvo = estado.posicoes().get(disco.id());

                if (posAlvo == null)
                    continue;

                // Criação Lazy se o nó gráfico associado à identidade pura não existir no cache
                Rectangle nóDisco = nosDiscos.computeIfAbsent(disco.id(), id -> {
                    double w = cfg.discoWidthBase() + (disco.tamanho() * cfg.discoWidthIncrement());
                    Rectangle r = new Rectangle(w, cfg.discoHeight());
                    r.setArcWidth(10);
                    r.setArcHeight(10);
                    grupoDiscos.getChildren().add(r);

                    // Posição física absoluta inicial (sem translação)
                    r.setX(0);
                    r.setY(0);
                    r.translateXProperty().setValue(posAlvo.x());
                    r.translateYProperty().setValue(posAlvo.y());

                    return r;
                });

                // Muta a cor do disco com base no tema puro
                nóDisco.setFill(Color.web(tema.getTexto()).deriveColor(0, 1, 1, 1 - (disco.tamanho() * 0.08)));

                // Despacha a interpolação para o Fiber Scheduler se a posição diferir do vetor
                // atual
                if (nóDisco.getTranslateX() != posAlvo.x() || nóDisco.getTranslateY() != posAlvo.y()) {
                    TranslateTransition transicao = new TranslateTransition(Duration.millis(250), nóDisco);
                    transicao.setToX(posAlvo.x());
                    transicao.setToY(posAlvo.y());
                    scheduler.agendar(transicao);
                }
            }
        }

        // Limpeza de nós órfãos de discos deletados (se houver reset ou
        // redimensionamento)
        nosDiscos.keySet().removeIf(id -> {
            boolean existeNoEstado = estado.torres().stream()
                    .flatMap(java.util.Collection::stream)
                    .anyMatch(d -> d.id().equals(id));
            if (!existeNoEstado) {
                grupoDiscos.getChildren().remove(nosDiscos.get(id));
            }
            return !existeNoEstado;
        });
    }
}
