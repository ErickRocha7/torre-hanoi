package com.hanoi;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        // Fundo oficial Catppuccin Macchiato Base
        root.setStyle("-fx-background-color: #24273a;"); 
        
        Scene scene = new Scene(root, 1200, 800);
        
        primaryStage.setTitle("Torre de Hanói Premium");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
