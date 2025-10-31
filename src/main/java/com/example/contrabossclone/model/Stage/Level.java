package com.example.contrabossclone.model.Stage;

import com.example.contrabossclone.model.Boss.Boss;
import com.example.contrabossclone.model.Enemy.Enemy;
import com.example.contrabossclone.model.Items.PowerUp;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.List;

public class Level {

    private List<Boss> bosses;
    private List<Enemy> enemies;
    private List<Platform> platforms;
    private List<PowerUp> powerUps;
    private Image backgroundImage;

    public Level(List<Boss> bosses,List<Enemy> enemies, List<Platform> platforms, List<PowerUp> powerUps, String backgroundImagePath) {
        this.bosses = bosses;
        this.enemies = enemies;
        this.platforms = platforms;
        this.powerUps = powerUps;
        try {
            this.backgroundImage = new Image(getClass().getResourceAsStream(backgroundImagePath));
        } catch (Exception e) {
            System.out.println("Error loading background image: " + backgroundImagePath);
            this.backgroundImage = null;
        }
    }

    public void render(GraphicsContext gc) {
        if (backgroundImage != null) {
            gc.drawImage(backgroundImage, 0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        } else {
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        }

        for (Boss boss : bosses) {
            boss.render(gc);
        }
        for (Platform platform : platforms) {
            platform.render(gc);
        }
        for (PowerUp powerUp : powerUps) {
            powerUp.render(gc);
        }
        for (Enemy enemy : enemies) {
            enemy.render(gc);
        }
    }

    public List<Boss> getBosses() {
        return bosses;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }
    public List<Platform> getPlatforms() {
        return platforms;
    }

    public List<PowerUp> getPowerUps() {
        return powerUps;
    }
}
