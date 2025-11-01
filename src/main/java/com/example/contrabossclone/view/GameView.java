package com.example.contrabossclone.view;

import com.example.contrabossclone.model.Enemy.Enemy;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import com.example.contrabossclone.model.GameModel;
import com.example.contrabossclone.model.MachanicShoot.Bullet;
import javafx.scene.text.TextAlignment;
import javafx.scene.image.Image;

public class GameView {

    private final GameModel model;
    private Image backgroundImage;  // Will be used for background
    private double width;
    private double height;

    public GameView(GameModel model, double width, double height) {
        this.model = model;
        this.width = width;
        this.height = height;
    }


    public void render(GraphicsContext gc, Canvas canvas) {
        // Clear the canvas
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        // Clear the canvas
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Draw background
        if (backgroundImage != null) {
            gc.drawImage(backgroundImage, 0, 0, canvas.getWidth(), canvas.getHeight());
        } else {
            // Fallback background
            gc.setFill(Color.LIGHTBLUE);
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        }

        // Draw current level
        model.getLevels().get(model.getCurrentLevelIndex()).render(gc);

        // Draw player
        model.getPlayer().render(gc);

        // Draw all bullets
        for (Bullet bullet : model.getPlayerBullets()) {
            bullet.render(gc);
        }
        for (Bullet bullet : model.getBossBullets()) {
            bullet.render(gc);
        }
        for (Bullet bullet : model.getEnemyBullets()) {
            gc.setFill(Color.RED);  // Ensure enemy bullets are red
            bullet.render(gc);
        }

        // Draw enemies
        for (Enemy enemy : model.getLevels().get(model.getCurrentLevelIndex()).getEnemies()) {
            if (enemy.isAlive()) {
                enemy.render(gc);
            }
        }

        // Draw HUD
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(16));  // Set a default font
        gc.fillText("Lives: " + model.getPlayer().getLives(), 10, 20);
        gc.fillText("Score: " + model.getPlayer().getScore(), 10, 40);

        // Draw game over message if needed
        if (model.isGameOver()) {
            gc.setFill(Color.WHITE);
            gc.setFont(new Font(40));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.fillText(model.getGameOverMessage(), canvas.getWidth()/2, canvas.getHeight()/2);
        }
    }

    public void resize(double newWidth, double newHeight) {
        this.width = newWidth;
        this.height = newHeight;
    }
}
