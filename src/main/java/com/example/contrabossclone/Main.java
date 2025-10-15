package com.example.contrabossclone;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import com.example.contrabossclone.controller.GameController;
import com.example.contrabossclone.model.GameModel;
import com.example.contrabossclone.view.GameView;

public class Main extends Application {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private GameModel model;
    private GameView view;
    private GameController controller;

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        model = new GameModel();
        view = new GameView(model);

        Pane root = new Pane(canvas);
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        controller = new GameController(model, scene);

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                controller.handleInput();
                if (!model.isGameOver()) {
                    model.update();
                }
                view.render(gc);
            }
        }.start();

        primaryStage.setTitle("Contra Boss Clone");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
