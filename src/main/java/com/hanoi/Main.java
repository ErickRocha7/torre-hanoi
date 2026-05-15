package com.hanoi;

import com.hanoi.config.LayoutConfig;
import com.hanoi.config.Tema;
import com.hanoi.state.Store;
import com.hanoi.view.RenderScheduler;
import com.hanoi.view.Renderer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 1. Instancia as configurações geométricas imutáveis e injetáveis
        LayoutConfig cfg = LayoutConfig.DEFAULT;

        // 2. Cria o contêiner visual limpo que permite o posicionamento absoluto via
        // pixel dos discos e pinos
        Pane root = new Pane();
        root.setPrefSize(cfg.boardWidth(), cfg.boardHeight());

        // 3. Inicializa a única fonte de verdade (Store) com 4 discos, o tema
        // Catppuccin e a geometria definida
        Store store = new Store(4, Tema.CATPPUCCIN_MACCHIATO, cfg);

        // 4. Inicializa o escalonador assíncrono controlado que gerencia a fila de
        // transições concorrentes
        RenderScheduler scheduler = new RenderScheduler();

        // 5. Instancia o renderizador injetando o nó gráfico raiz, a store de despacho
        // e o barramento de animações
        Renderer renderer = new Renderer(root, store, cfg, scheduler);

        // 6. Inscreve o renderizador de forma reativa para disparar uma atualização a
        // cada mutação de estado
        store.inscrever(renderer::renderizar);

        // 7. Configura a janela principal do JavaFX travando o redimensionamento para
        // proteger a integridade do layout
        Scene scene = new Scene(root, cfg.boardWidth(), cfg.boardHeight());

        primaryStage.setTitle("Torre de Hanói - Arquitetura Premium");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
