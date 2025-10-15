package com.example.contrabossclone.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import com.example.contrabossclone.model.GameModel;
import com.example.contrabossclone.model.Level;
import com.example.contrabossclone.model.Bullet;

public class GameView {

    private GameModel model;

    public GameView(GameModel model) {
        this.model = model;
    }

    public void render(GraphicsContext gc) {
        Level currentLevel = model.getLevels().get(model.getCurrentLevelIndex());
        currentLevel.render(gc);

        if (model.isGameOver()) {
            gc.setFill(Color.WHITE);
            gc.fillText(model.getGameOverMessage(), 400 - 50, 300);
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
}
