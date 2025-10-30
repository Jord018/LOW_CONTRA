package com.example.contrabossclone.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import com.example.contrabossclone.model.GameModel;
import com.example.contrabossclone.model.Stage.Level;
import com.example.contrabossclone.model.MachanicShoot.Bullet;

public class GameView {

    private GameModel model;
    private double width;
    private double height;

    public GameView(GameModel model, double width, double height) {
        this.model = model;
        this.width = width;
        this.height = height;
    }

    public void render(GraphicsContext gc) {
        Level currentLevel = model.getLevels().get(model.getCurrentLevelIndex());
        currentLevel.render(gc);

        gc.setFill(Color.WHITE);
        gc.fillText("Lives: " + model.getPlayer().getLives(), 10, 20);
        gc.fillText("Score: " + model.getPlayer().getScore(), 10, 40);

        if (model.isGameOver()) {
            gc.setFill(Color.WHITE);
            gc.fillText(model.getGameOverMessage(), width / 2 - 50, height / 2);
        } else {
            model.getPlayer().render(gc);

            for (Bullet bullet : model.getPlayerBullets()) {
                bullet.render(gc);
            }

            for (Bullet bullet : model.getBossBullets()) {
                bullet.render(gc);
            }
        }
    }

    public void resize(double newWidth, double newHeight) {
        this.width = newWidth;
        this.height = newHeight;
    }
}
