package com.example.contrabossclone;

import com.example.contrabossclone.model.Stage.MenuView;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane; // ⭐️ (1) เปลี่ยนจาก Pane เป็น StackPane
import javafx.scene.text.Font; // ⭐️ (2) เพิ่ม Import
import javafx.stage.Stage;
import com.example.contrabossclone.controller.GameController;
import com.example.contrabossclone.model.GameModel;
import com.example.contrabossclone.util.ResourceLoader;
import com.example.contrabossclone.view.GameView;

public class Main extends Application {

    private GameModel model;
    private GameView view;
    private GameController controller;

    // ⭐️ --- (3) เพิ่มตัวแปรสำหรับ "สลับฉาก" ---
    private AnimationTimer gameTimer;
    private StackPane rootPane; // (StackPane ใช้สลับฉากทับกัน)
    private Pane gamePane;     // (Pane นี้จะเก็บ Canvas ของเกม)
    private MenuView menuView;   // (Pane ของหน้าเมนู)
    private Canvas canvas;
    // ⭐️ --- สิ้นสุด ---

    @Override
    public void start(Stage primaryStage) {

        double screenWidth = 800;
        double screenHeight = 600;



        rootPane = new StackPane();
        Scene scene = new Scene(rootPane, screenWidth, screenHeight);


        canvas = new Canvas(screenWidth, screenHeight);
        gamePane = new StackPane(canvas);

        model = new GameModel(canvas.getWidth(), canvas.getHeight());
        view = new GameView(model, canvas.getWidth(), canvas.getHeight());

        // (Bind canvas)
        canvas.widthProperty().bind(gamePane.widthProperty());
        canvas.heightProperty().bind(gamePane.heightProperty());

        // (Resize listeners)
        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            model.resize(newVal.doubleValue(), scene.getHeight());
            view.resize(newVal.doubleValue(), scene.getHeight());
        });
        scene.heightProperty().addListener((obs, oldVal, newVal) -> {
            model.resize(scene.getWidth(), newVal.doubleValue());
            view.resize(scene.getWidth(), newVal.doubleValue());
        });

        controller = new GameController(model, scene);


        gameTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                controller.handleInput();
                if (!model.isGameOver()) {
                    model.update();
                }
                view.render(canvas.getGraphicsContext2D(), canvas);
            }
        };



        menuView = new MenuView(() -> {
            //
            rootPane.getChildren().remove(menuView); //
            rootPane.getChildren().add(gamePane);  // เ
            gameTimer.start(); //
        });

        // ⭐️ --- (8) ---
        rootPane.getChildren().add(menuView);

        primaryStage.setTitle("Contra Boss Clone");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
