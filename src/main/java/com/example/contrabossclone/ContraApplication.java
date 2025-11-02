package com.example.contrabossclone;

import com.example.contrabossclone.controller.GameController;
import com.example.contrabossclone.model.GameModel;
import com.example.contrabossclone.model.Stage.MenuView;
import com.example.contrabossclone.view.GameView;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ContraApplication extends Application {

    private AnimationTimer gameTimer;
    private StackPane rootPane;
    private Pane gamePane;
    private MenuView menuView;

    @Override
    public void start(Stage primaryStage) {
        double screenWidth = 800;
        double screenHeight = 600;

        try {
            Font.loadFont(getClass().getResourceAsStream("/GameAssets/PressStart2P-Regular.ttf"), 20);
        } catch (Exception e) {
            System.err.println("'PressStart2P-Regular.ttf' /GameAssets/");
            System.err.println("resources/GameAssets");
        }

        rootPane = new StackPane();
        Scene scene = new Scene(rootPane, screenWidth, screenHeight);

        Canvas canvas = new Canvas(screenWidth, screenHeight);
        gamePane = new StackPane(canvas);
        GameModel gameModel = new GameModel(screenWidth, screenHeight);

        GameView gameView = new GameView(gameModel, screenWidth, screenHeight);

        GameController gameController = new GameController(gameModel, scene);

        gameTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gameController.handleInput();
                gameModel.update();

                gameView.render(canvas.getGraphicsContext2D(), canvas);
            }
        };

        createMenuView();

        rootPane.getChildren().add(menuView);

        primaryStage.setTitle("Contra Boss Clone");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createMenuView() {
        menuView = new MenuView(() -> {
            rootPane.getChildren().remove(menuView);
            rootPane.getChildren().add(gamePane);
            gameTimer.start();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}

